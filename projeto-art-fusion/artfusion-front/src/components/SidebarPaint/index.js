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
import * as S from './styles';
import './styles.css';
import store from '../whiteboard/whiteboardStore';
import { observer } from 'mobx-react';

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

const ToolButton = observer(({ src, type }) => {
    const selectedToolType = store.tool;

    const handleToolChange = () => {
        store.setToolType(type);
    };

    const className =
        selectedToolType === type ? 'botao_sidebar_active' : 'botao_sidebar';

    return (
        <button className={className} onClick={handleToolChange}>
            <img width="80%" src={src} />
        </button>
    );
});

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

const Sidebar = observer(() => {
    const [showLineWidths, setShowLineWidths] = useState(false);
    const [showColorPicker, setShowColorPicker] = useState(false);
    const popupRef = useRef(null);
    const colorPickerRef = useRef(null);
    const lineWidth = store.lineWidth; // Obtendo a espessura da linha da store
    const color = store.color;

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleLineWidthChange = (lineWidth) => {
        store.setLineWidth(lineWidth);
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
        store.setColor(color.hex);
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
});

export default Sidebar;
