import React, { useState, useRef, useEffect } from 'react';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import * as C from './styles';

const Paint = () => {
    const mainCanvasRef = useRef(null);
    const previewCanvasRef = useRef(null);
    const [tool, setTool] = useState('pencil');
    const [color, setColor] = useState('#000000');
    const [lineWidth, setLineWidth] = useState(2);
    const [isDrawing, setIsDrawing] = useState(false);
    const [startPos, setStartPos] = useState({ x: 0, y: 0 });
    const [undoStack, setUndoStack] = useState([]);
    const [redoStack, setRedoStack] = useState([]);

    useEffect(() => {
        const mainCanvas = mainCanvasRef.current;
        const context = mainCanvas.getContext('2d');
        context.lineWidth = lineWidth;
        context.strokeStyle = color;

        if (tool === 'brush') {
            context.lineCap = 'round';
        } else if (tool === 'pencil') {
            context.lineCap = 'butt';
        }
    }, [color, tool, lineWidth]);

    const handleMouseDown = (e) => {
        const mainCanvas = mainCanvasRef.current;
        const previewCanvas = previewCanvasRef.current;
        const mainContext = mainCanvas.getContext('2d');
        const previewContext = previewCanvas.getContext('2d');

        mainContext.strokeStyle = color;
        previewContext.strokeStyle = color;
        mainContext.lineWidth = lineWidth;
        previewContext.lineWidth = lineWidth;

        saveState();

        setIsDrawing(true);
        const { offsetX, offsetY } = e.nativeEvent;
        setStartPos({ x: offsetX, y: offsetY });

        if (tool === 'pencil' || tool === 'brush' || tool === 'eraser') {
            mainContext.beginPath();
            mainContext.moveTo(offsetX, offsetY);
        } else if (tool === 'bucket') {
            fill(mainContext, offsetX, offsetY, color);
        }
    };

    const handleMouseMove = (e) => {
        if (!isDrawing) return;
        const mainCanvas = mainCanvasRef.current;
        const previewCanvas = previewCanvasRef.current;
        const mainContext = mainCanvas.getContext('2d');
        const previewContext = previewCanvas.getContext('2d');
        const { offsetX, offsetY } = e.nativeEvent;

        previewContext.clearRect(0, 0, previewCanvas.width, previewCanvas.height);

        switch (tool) {
            case 'pencil':
            case 'brush':
                mainContext.lineTo(offsetX, offsetY);
                mainContext.stroke();
                break;
            case 'eraser':
                mainContext.strokeStyle = '#fff';
                mainContext.lineTo(offsetX, offsetY);
                mainContext.stroke();
                break;
            case 'circle':
                drawCircle(previewContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            case 'square':
                drawRect(previewContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            case 'triangle':
                drawTriangle(previewContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            case 'line':
                drawLine(previewContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            default:
                break;
        }
    };

    const handleMouseUp = (e) => {
        const mainCanvas = mainCanvasRef.current;
        const previewCanvas = previewCanvasRef.current;
        const mainContext = mainCanvas.getContext('2d');
        const { offsetX, offsetY } = e.nativeEvent;

        if (!isDrawing) return;

        previewCanvas.getContext('2d').clearRect(0, 0, previewCanvas.width, previewCanvas.height);

        switch (tool) {
            case 'circle':
                drawCircle(mainContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            case 'square':
                drawRect(mainContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            case 'triangle':
                drawTriangle(mainContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            case 'line':
                drawLine(mainContext, startPos.x, startPos.y, offsetX, offsetY);
                break;
            default:
                break;
        }

        setIsDrawing(false);
    };

    const drawCircle = (context, x1, y1, x2, y2) => {
        const radius = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        context.beginPath();
        context.arc(x1, y1, radius, 0, Math.PI * 2);
        context.stroke();
    };

    const drawRect = (context, x1, y1, x2, y2) => {
        const width = x2 - x1;
        const height = y2 - y1;
        context.beginPath();
        context.rect(x1, y1, width, height);
        context.stroke();
    };

    const drawTriangle = (context, x1, y1, x2, y2) => {
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineTo(x1 - (x2 - x1), y2);
        context.closePath();
        context.stroke();
    };

    const drawLine = (context, x1, y1, x2, y2) => {
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.stroke();
    };

    const fill = (context, x, y, fillColor) => {
        const imageData = context.getImageData(0, 0, context.canvas.width, context.canvas.height);
        const data = imageData.data;
        const targetColor = getColorAtPixel(data, x, y);
        const fillColorRgb = hexToRgb(fillColor);

        if (!targetColor || colorsMatch(targetColor, fillColorRgb)) {
            return;
        }

        const stack = [{ x, y }];

        while (stack.length) {
            const { x, y } = stack.pop();
            let currentY = y;

            while (currentY >= 0 && colorsMatch(getColorAtPixel(data, x, currentY), targetColor)) {
                currentY--;
            }

            currentY++;
            let reachLeft = false;
            let reachRight = false;

            while (currentY < context.canvas.height && colorsMatch(getColorAtPixel(data, x, currentY), targetColor)) {
                colorPixel(data, x, currentY, fillColorRgb);

                if (x > 0) {
                    if (colorsMatch(getColorAtPixel(data, x - 1, currentY), targetColor)) {
                        if (!reachLeft) {
                            stack.push({ x: x - 1, y: currentY });
                            reachLeft = true;
                        }
                    } else if (reachLeft) {
                        reachLeft = false;
                    }
                }

                if (x < context.canvas.width - 1) {
                    if (colorsMatch(getColorAtPixel(data, x + 1, currentY), targetColor)) {
                        if (!reachRight) {
                            stack.push({ x: x + 1, y: currentY });
                            reachRight = true;
                        }
                    } else if (reachRight) {
                        reachRight = false;
                    }
                }

                currentY++;
            }
        }

        context.putImageData(imageData, 0, 0);
    };

    const getColorAtPixel = (data, x, y) => {
        const index = (y * mainCanvasRef.current.width + x) * 4;
        return [data[index], data[index + 1], data[index + 2], data[index + 3]];
    };

    const colorPixel = (data, x, y, color) => {
        const index = (y * mainCanvasRef.current.width + x) * 4;
        data[index] = color[0];
        data[index + 1] = color[1];
        data[index + 2] = color[2];
        data[index + 3] = 255;
    };

    const hexToRgb = (hex) => {
        const bigint = parseInt(hex.slice(1), 16);
        return [(bigint >> 16) & 255, (bigint >> 8) & 255, bigint & 255];
    };

    const colorsMatch = (a, b) => {
        return a[0] === b[0] && a[1] === b[1] && a[2] === b[2] && a[3] === b[3];
    };

    const handleToolChange = (selectedTool) => {
        setTool(selectedTool);
    };

    const handleColorChange = (selectedColor) => {
        setColor(selectedColor);
    };

    const handleLineWidthChange = (width) => {
        setLineWidth(parseInt(width, 10));
    };

    const saveState = () => {
        const mainCanvas = mainCanvasRef.current;
        const dataURL = mainCanvas.toDataURL();
        setUndoStack((prev) => [...prev, dataURL]);
        setRedoStack([]);
    };

    const handleUndo = () => {
        if (undoStack.length > 0) {
            const lastState = undoStack.pop();
            setRedoStack((prev) => [...prev, lastState]);
            restoreState(undoStack[undoStack.length - 1]);
        }
    };

    const handleRedo = () => {
        if (redoStack.length > 0) {
            const nextState = redoStack.pop();
            setUndoStack((prev) => [...prev, nextState]);
            restoreState(nextState);
        }
    };

    const restoreState = (dataURL) => {
        const mainCanvas = mainCanvasRef.current;
        const context = mainCanvas.getContext('2d');
        const image = new Image();
        image.src = dataURL;
        image.onload = () => {
            context.clearRect(0, 0, mainCanvas.width, mainCanvas.height);
            context.drawImage(image, 0, 0);
        };
    };

    return (
        <div>
            <Header onUndo={handleUndo} onRedo={handleRedo} />
            <Sidebar
                onToolChange={handleToolChange}
                onColorChange={handleColorChange}
                onLineWidthChange={handleLineWidthChange}
            />
            <canvas
                ref={previewCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{ marginLeft: '60px', marginTop: '60px', backgroundColor: 'transparent', position: 'absolute', pointerEvents: 'none', zIndex: 1 }}
            />
            <canvas
                ref={mainCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{ marginLeft: '60px', marginTop: '60px', backgroundColor: '#fff', position: 'absolute', zIndex: 0 }}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
            />
        </div>
    );
};

export default Paint;
