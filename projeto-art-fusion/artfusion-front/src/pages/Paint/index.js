import React, { useState, useRef, useEffect } from 'react';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import * as C from './styles';
import socketService from '../../services/socket';
import Cookies from 'js-cookie';

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
    const [socket, setSocket] = useState(null);
    const [points, setPoints] = useState([]);

    useEffect(() => {
        const token = Cookies.get('user_token');
        if (token) {
            socketService.connect(() => {
                console.log('Subscribing to /topic/alteracoes...');
                socketService.subscribe('/topic/alteracoes', handleRemoteDrawing);
            });
        } else {
            console.error('JWT token not found. Redirecting to login.');
        }

        return () => {
            socketService.disconnect();
        };
    }, []);

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
        setPoints([{ x: offsetX, y: offsetY }]);

        if (tool === 'pencil' || tool === 'brush' || tool === 'eraser') {
            mainContext.beginPath();
            mainContext.moveTo(offsetX, offsetY);
        } else if (tool === 'bucket') {
            fill(mainContext, offsetX, offsetY, color);
            const drawingData = {
                arteId: 1,
                usuarioId: 1,
                delta: { tool, color, lineWidth, points: [{ x: offsetX, y: offsetY }] },
            };
            socketService.send('/envio/alteracoes', drawingData);
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

        let newPoints = [];
        switch (tool) {
            case 'pencil':
            case 'brush':
                mainContext.lineTo(offsetX, offsetY);
                mainContext.stroke();
                newPoints = points.concat([{ x: offsetX, y: offsetY }]);
                setPoints(newPoints);
                break;
            case 'eraser':
                mainContext.strokeStyle = '#fff';
                mainContext.lineTo(offsetX, offsetY);
                mainContext.stroke();
                newPoints = points.concat([{ x: offsetX, y: offsetY }]);
                setPoints(newPoints);
                break;
            case 'circle':
                drawCircle(previewContext, startPos, { x: offsetX, y: offsetY });
                break;
            case 'square':
                drawRect(previewContext, startPos, { x: offsetX, y: offsetY });
                break;
            case 'triangle':
                drawTriangle(previewContext, startPos, { x: offsetX, y: offsetY });
                break;
            case 'line':
                drawLine(previewContext, startPos, { x: offsetX, y: offsetY });
                break;
            default:
                break;
        }

        // Enviar alterações para o servidor via WebSocket
        const drawingData = {
            arteId: 1,
            usuarioId: 1,
            delta: { tool, color, lineWidth, points: newPoints },
        };
        socketService.send('/envio/alteracoes', drawingData);
    };

    const handleMouseUp = (e) => {
        setIsDrawing(false);

        const mainCanvas = mainCanvasRef.current;
        const previewCanvas = previewCanvasRef.current;
        const mainContext = mainCanvas.getContext('2d');
        const previewContext = previewCanvas.getContext('2d');
        const { offsetX, offsetY } = e.nativeEvent;

        previewContext.clearRect(0, 0, previewCanvas.width, previewCanvas.height);

        if (['circle', 'square', 'triangle', 'line'].includes(tool)) {
            const drawingData = {
                arteId: 1,
                usuarioId: 1,
                delta: { tool, color, lineWidth, points: [...points, { x: offsetX, y: offsetY }] },
            };
            socketService.send('/envio/alteracoes', drawingData);
        }

        setPoints([]);
    };

    const sendDrawingData = (drawingData) => {
        const data = {
            arteId: 1,
            usuarioId: 1,
            delta: drawingData
        };
        socketService.send("/envio/alteracoes", data);
    };

    const drawCircle = (context, start, end) => {
        const radius = Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
        context.beginPath();
        context.arc(start.x, start.y, radius, 0, Math.PI * 2);
        context.stroke();
    };

    const drawRect = (context, start, end) => {
        const width = end.x - start.x;
        const height = end.y - start.y;
        context.beginPath();
        context.rect(start.x, start.y, width, height);
        context.stroke();
    };

    const drawTriangle = (context, start, end) => {
        context.beginPath();
        context.moveTo(start.x, start.y);
        context.lineTo(end.x, end.y);
        context.lineTo(start.x - (end.x - start.x), end.y);
        context.closePath();
        context.stroke();
    };

    const drawLine = (context, start, end) => {
        context.beginPath();
        context.moveTo(start.x, start.y);
        context.lineTo(end.x, end.y);
        context.stroke();
    };

    const fill = (context, startX, startY, fillColor) => {
        const canvasWidth = context.canvas.width;
        const canvasHeight = context.canvas.height;
        const imageData = context.getImageData(0, 0, canvasWidth, canvasHeight);
        const data = imageData.data;
        const targetColor = getColorAtPixel(data, startX, startY);
        const fillColorRgb = hexToRgb(fillColor);
    
        if (!targetColor || colorsMatch(targetColor, fillColorRgb)) {
            return;
        }
    
        const stack = [{ x: startX, y: startY }];
        const visited = new Set();
    
        const floodFill = () => {
            const { x, y } = stack.pop();
            const key = `${x},${y}`;
    
            if (visited.has(key)) {
                if (stack.length > 0) {
                    requestAnimationFrame(floodFill);
                }
                return;
            }
    
            visited.add(key);
    
            let currentY = y;
    
            while (currentY >= 0 && colorsMatch(getColorAtPixel(data, x, currentY), targetColor)) {
                currentY--;
            }
    
            currentY++;
            let reachLeft = false;
            let reachRight = false;
    
            while (currentY < canvasHeight && colorsMatch(getColorAtPixel(data, x, currentY), targetColor)) {
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
    
                if (x < canvasWidth - 1) {
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
    
            if (stack.length > 0) {
                requestAnimationFrame(floodFill);
            } else {
                context.putImageData(imageData, 0, 0);
            }
        };
    
        floodFill();
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

    const handleRemoteDrawing = (message) => {
        const { tool, color, lineWidth, points } = message.delta;
        const context = mainCanvasRef.current.getContext('2d');
        context.strokeStyle = color;
        context.lineWidth = lineWidth;

        switch (tool) {
            case 'pencil':
            case 'brush':
                context.beginPath();
                context.moveTo(points[0].x, points[0].y);
                for (let i = 1; i < points.length; i++) {
                    context.lineTo(points[i].x, points[i].y);
                }
                context.stroke();
                break;
            case 'circle':
                drawCircle(context, points[0], points[1]);
                break;
            case 'square':
                drawRect(context, points[0], points[1]);
                break;
            case 'triangle':
                drawTriangle(context, points[0], points[1]);
                break;
            case 'line':
                drawLine(context, points[0], points[1]);
                break;
            case 'bucket':
                fill(context, points[0].x, points[0].y, color);
                break;
            case 'eraser':
                context.globalCompositeOperation = 'destination-out';
                context.beginPath();
                context.moveTo(points[0].x, points[0].y);
                for (let i = 1; i < points.length; i++) {
                    context.lineTo(points[i].x, points[i].y);
                }
                context.stroke();
                context.globalCompositeOperation = 'source-over';
                break;
            default:
                break;
        }
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
