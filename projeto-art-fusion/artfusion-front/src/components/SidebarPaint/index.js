import React, { useState, useRef, useEffect } from 'react';
import { FaArrowsAltH } from "react-icons/fa";
import squareIcon from '../../assets/icons/square.svg';
import lineIcon from '../../assets/icons/line.svg';
import pencilIcon from '../../assets/icons/pencil.svg';
import brushIcon from '../../assets/icons/brush.svg';
import colorSelectorIcon from '../../assets/icons/color-selector.svg';
import eraserIcon from '../../assets/icons/eraser.svg';
import lineThicknessIcon from '../../assets/icons/line-thickness.svg';
import triangleIcon from '../../assets/icons/triangle.svg';
import circleIcon from '../../assets/icons/circle.svg';
import { SketchPicker } from 'react-color';
import { toolTypes } from '../../constants'; 
import { useDispatch, useSelector } from 'react-redux';
import * as S from './styles';
import './styles.css';
import { setToolType, setLineWidth } from '../whiteboard/whiteboardSlice';

const lineWidths = [1, 2, 3, 4, 5, 10, 20];

const IconButton = ({ src, type }) => {

    const dispatch = useDispatch();

    const selectedToolType = useSelector(state => state.whiteboard.tool);

    const handleToolChange =() => {
        dispatch(setToolType(type))
    };

    const className = selectedToolType === type ? 'botao_sidebar_active' : 'botao_sidebar';

    return <button className={className} onClick={handleToolChange}>
        <img width='70%'src={src} />
    </button>
}

const Sidebar = ({ onColorChange}) => {
    const [showLineWidths, setShowLineWidths] = useState(false);
    //const [showColorPicker, setShowColorPicker] = useState(false);
    // const [selectedColor, setSelectedColor] = useState('#000000');
    const popupRef = useRef(null);
    // const colorPickerRef = useRef(null);

    useEffect(() => {
        // Adicionar listener para cliques fora do popup
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            // Limpar listener
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const dispatch = useDispatch();
    const lineWidth = useSelector(state => state.whiteboard.lineWidth); // Obtendo a espessura da linha do estado

    const handleLineWidthChange = (width) => {
        dispatch(setLineWidth(width));
        setShowLineWidths(false); // Esconder o popup após a seleção
    };

    const handleLineWidthClick = () => {
        setShowLineWidths(!showLineWidths);
    };

    const handleClickOutside = (event) => {
        if (popupRef.current && !popupRef.current.contains(event.target)) {
            setShowLineWidths(false);
        }
    };

    // const handleClickOutside = (event) => {
    //     if (popupRef.current && !popupRef.current.contains(event.target)) {
    //         setShowLineWidths(false);
    //     }
    //     // if (colorPickerRef.current && !colorPickerRef.current.contains(event.target)) {
    //     //     setShowColorPicker(false);
    //     // }
    // };

    // const handleColorChange = (color) => {
    //     setSelectedColor(color.hex);
    //     onColorChange(color.hex);
    // };

    // const handlePaletteClick = () => {
    //     setShowColorPicker(!showColorPicker);
    // };

    // useEffect(() => {
    //     document.addEventListener('mousedown', handleClickOutside);
    //     return () => {
    //         document.removeEventListener('mousedown', handleClickOutside);
    //     };
    // }, []);

    return (
        <S.SidebarContainer>
            <IconButton src={pencilIcon} type={toolTypes.PENCIL}  />
            <IconButton src={eraserIcon} type={toolTypes.ERASER}  />
            <IconButton src={circleIcon} type={toolTypes.CIRCLE}  />
            <IconButton src={squareIcon} type={toolTypes.SQUARE}  />
            <IconButton src={triangleIcon} type={toolTypes.TRIANGLE}  />
            <IconButton src={lineIcon} type={toolTypes.LINE}  />
            
            <S.ToolButton title="Espessura" onClick={handleLineWidthClick}>
                <FaArrowsAltH />
            </S.ToolButton>

            <S.Divider />
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

        //     <S.SelectedColorIndicator>
        //         <S.SelectedColor color={selectedColor} />
        //         <S.ToolButton title="Escolher cor" onClick={handlePaletteClick}>
        //             <FaPalette />
        //         </S.ToolButton>
        //         {showColorPicker && (
        //             <S.ColorPickerPopup ref={colorPickerRef}>
        //                 <SketchPicker color={selectedColor} onChange={handleColorChange} />
        //             </S.ColorPickerPopup>
        //         )}
        //     </S.SelectedColorIndicator>
        //     <S.Divider />
        //     <S.ColorBoxContainer>
        //         {colors.map((color) => (
        //             <S.ColorBox
        //                 key={color}
        //                 color={color}
        //                 title={`Cor ${color}`}
        //                 onClick={() => handleColorChange({ hex: color })}
        //             />
        //         ))}
        //     </S.ColorBoxContainer>
        
    );
};

export default Sidebar;
