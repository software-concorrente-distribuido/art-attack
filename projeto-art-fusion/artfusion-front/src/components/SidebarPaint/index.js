import React, { useState, useRef, useEffect } from 'react';
import squareIcon from '../../assets/icons/square.svg';
import lineIcon from '../../assets/icons/line.svg';
import pencilIcon from '../../assets/icons/pencil.svg';
import brushIcon from '../../assets/icons/brush.svg';
import paletteIcon from '../../assets/icons/palette.svg';
import eraserIcon from '../../assets/icons/eraser.svg';
import lineThicknessIcon from '../../assets/icons/line-thickness.svg';
import triangleIcon from '../../assets/icons/triangle.svg';
import sprayIcon from '../../assets/icons/spray.svg';
import circleIcon from '../../assets/icons/circle.svg';
import { SketchPicker } from 'react-color';
import { toolTypes } from '../../constants';
import { useDispatch, useSelector } from 'react-redux';
import * as S from './styles';
import './styles.css';
import {
    setToolType,
    setLineWidth,
    setColor,
} from '../whiteboard/whiteboardSlice';

const lineWidths = [1, 2, 3, 4, 5, 10, 20];

const colors = [
    '#ff0000',
    '#00ff00',
    '#0000ff',
    '#ffff00',
    '#ff00ff',
    '#00ffff',
    '#000000',
    '#ffffff',
];

const ToolButton = ({ src, type }) => {
    const dispatch = useDispatch();

    const selectedToolType = useSelector((state) => state.whiteboard.tool);

    const handleToolChange = () => {
        dispatch(setToolType(type));
    };

    const className =
        selectedToolType === type ? 'botao_sidebar_active' : 'botao_sidebar';

    return (
        <button className={className} onClick={handleToolChange}>
            <img width="80%" src={src} />
        </button>
    );
};

const IconButton = ({ src, onClick }) => {
    return (
        <button className={'botao_sidebar'} onClick={onClick}>
            <img width="80%" src={src} />
        </button>
    );
};

const ColorPickerButton = ({ src, onClick, color }) => {
    const buttonStyle = {
        backgroundColor: color || '#FFFFFF',
    };

    return (
        <button
            className={'botao_color_picker'}
            onClick={onClick}
            style={buttonStyle}
        >
            <img width="80%" src={src} />
        </button>
    );
};

const Sidebar = () => {
    const [showLineWidths, setShowLineWidths] = useState(false);
    const [showColorPicker, setShowColorPicker] = useState(false);
    const popupRef = useRef(null);
    const colorPickerRef = useRef(null);
    const dispatch = useDispatch();
    const lineWidth = useSelector((state) => state.whiteboard.lineWidth); // Obtendo a espessura da linha do estado
    const color = useSelector((state) => state.whiteboard.color);

    useEffect(() => {
        // Adicionar listener para cliques fora do popup
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            // Limpar listener
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleLineWidthChange = (lineWidth) => {
        dispatch(setLineWidth(lineWidth));
        setShowLineWidths(false); // Esconder o popup após a seleção
    };

    const handleLineWidthClick = () => {
        setShowLineWidths(!showLineWidths);
    };

    const handleClickOutside = (event) => {
        if (popupRef.current && !popupRef.current.contains(event.target)) {
            setShowLineWidths(false);
        }

        if (
            colorPickerRef.current &&
            !colorPickerRef.current.contains(event.target)
        ) {
            setShowColorPicker(false);
        }
    };

    const handleColorChange = (color) => {
        dispatch(setColor(color.hex));
    };

    const handlePaletteClick = () => {
        setShowColorPicker(!showColorPicker);
    };

    return (
        <S.SidebarContainer>
            <ToolButton src={brushIcon} type={toolTypes.PENCIL} />
            {/*<ToolButton src={sprayIcon} type={toolTypes.SPRAY} />*/}
            <ToolButton src={eraserIcon} type={toolTypes.ERASER} />
            <ToolButton src={circleIcon} type={toolTypes.CIRCLE} />
            <ToolButton src={squareIcon} type={toolTypes.SQUARE} />
            <ToolButton src={triangleIcon} type={toolTypes.TRIANGLE} />
            <ToolButton src={lineIcon} type={toolTypes.LINE} />

            <IconButton
                src={lineThicknessIcon}
                onClick={handleLineWidthClick}
            />

            <S.Divider />

            <S.SelectedColorIndicator>
                <ColorPickerButton
                    src={paletteIcon}
                    onClick={handlePaletteClick}
                    color={color}
                />

                {showColorPicker && (
                    <S.ColorPickerPopup ref={colorPickerRef}>
                        <SketchPicker
                            color={color}
                            onChange={handleColorChange}
                        />
                    </S.ColorPickerPopup>
                )}
            </S.SelectedColorIndicator>

            <S.ColorBoxContainer>
                {colors.map((color) => (
                    <S.ColorBox
                        key={color}
                        color={color}
                        title={`Cor ${color}`}
                        onClick={() => handleColorChange({ hex: color })}
                    />
                ))}
            </S.ColorBoxContainer>

            {showLineWidths && (
                <S.LineWidthPopup ref={popupRef}>
                    {lineWidths.map((width) => (
                        <S.LineWidthContainer
                            key={width}
                            onClick={() => handleLineWidthChange(width)}
                        >
                            <S.LineWidthLabel>{`${width}px`}</S.LineWidthLabel>
                            <S.LineWidthButton width={width} />
                        </S.LineWidthContainer>
                    ))}
                </S.LineWidthPopup>
            )}
        </S.SidebarContainer>
    );
};

export default Sidebar;
