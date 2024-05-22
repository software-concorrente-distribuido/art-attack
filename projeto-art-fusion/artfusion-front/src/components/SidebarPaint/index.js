import React, { useState, useRef, useEffect } from 'react';
import {
    FaPaintBrush,
    FaEraser,
    FaFillDrip,
    FaCircle,
    FaSquare,
    FaDrawPolygon,
    FaSlash,
    FaArrowsAltH,
    FaPalette,
} from 'react-icons/fa';
import { SketchPicker } from 'react-color';
import * as S from './styles';

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

const lineWidths = [1, 2, 3, 4, 5, 10, 20];

const Sidebar = ({ onToolChange, onColorChange, onLineWidthChange }) => {
    const [showLineWidths, setShowLineWidths] = useState(false);
    const [showColorPicker, setShowColorPicker] = useState(false);
    const [selectedColor, setSelectedColor] = useState('#000000');
    const popupRef = useRef(null);
    const colorPickerRef = useRef(null);

    const handleLineWidthClick = () => {
        setShowLineWidths(!showLineWidths);
    };

    const handleLineWidthChange = (width) => {
        onLineWidthChange(width);
        setShowLineWidths(false); // Esconder os botões após a seleção
    };

    const handleClickOutside = (event) => {
        if (popupRef.current && !popupRef.current.contains(event.target)) {
            setShowLineWidths(false);
        }
        if (colorPickerRef.current && !colorPickerRef.current.contains(event.target)) {
            setShowColorPicker(false);
        }
    };

    const handleColorChange = (color) => {
        setSelectedColor(color.hex);
        onColorChange(color.hex);
    };

    const handlePaletteClick = () => {
        setShowColorPicker(!showColorPicker);
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <S.SidebarContainer>
            <S.ToolButton title="Pincel" onClick={() => onToolChange('brush')}>
                <FaPaintBrush />
            </S.ToolButton>
            <S.ToolButton title="Borracha" onClick={() => onToolChange('eraser')}>
                <FaEraser />
            </S.ToolButton>
            <S.ToolButton title="Lata de Tinta" onClick={() => onToolChange('bucket')}>
                <FaFillDrip />
            </S.ToolButton>
            <S.ToolButton title="Círculo" onClick={() => onToolChange('circle')}>
                <FaCircle />
            </S.ToolButton>
            <S.ToolButton title="Quadrado" onClick={() => onToolChange('square')}>
                <FaSquare />
            </S.ToolButton>
            <S.ToolButton title="Triângulo" onClick={() => onToolChange('triangle')}>
                <FaDrawPolygon />
            </S.ToolButton>
            <S.ToolButton title="Linha" onClick={() => onToolChange('line')}>
                <FaSlash />
            </S.ToolButton>
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
            <S.SelectedColorIndicator>
                <S.SelectedColor color={selectedColor} />
                <S.ToolButton title="Escolher cor" onClick={handlePaletteClick}>
                    <FaPalette />
                </S.ToolButton>
                {showColorPicker && (
                    <S.ColorPickerPopup ref={colorPickerRef}>
                        <SketchPicker color={selectedColor} onChange={handleColorChange} />
                    </S.ColorPickerPopup>
                )}
            </S.SelectedColorIndicator>
            <S.Divider />
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
        </S.SidebarContainer>
    );
};

export default Sidebar;
