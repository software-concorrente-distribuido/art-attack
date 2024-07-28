import React, { useRef, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { toolTypes, actions } from '../../constants';
import { jwtDecode } from 'jwt-decode';
import {
    createElement,
    updateElement,
    drawElement,
    adjustmentRequired,
    adjustElementCoordinates,
} from './utils';
import { v4 as uuid } from 'uuid';
import { useUserId } from '../../hooks/useUserId';
import { flushBuffer } from './utils/updateElement';

const Whiteboard = React.forwardRef((props, canvasRef) => {
    const toolType = useSelector((state) => state.whiteboard.tool);
    const elements = useSelector((state) => state.whiteboard.elements);
    const lineWidth = useSelector((state) => state.whiteboard.lineWidth);
    const color = useSelector((state) => state.whiteboard.color);
    const lineWidthRef = useRef(lineWidth);
    const colorRef = useRef(color);
    const userId = useUserId();
    const { arteId, salaUUID } = useParams();

    const [action, setAction] = useState(null);
    const [points, setPoints] = useState([]);

    //pegando a posição do mouse relativo ao canvas e não à janela
    const getMousePosition = (event) => {
        const canvasRect = canvasRef.current.getBoundingClientRect();
        return {
            clientX: event.clientX - canvasRect.left,
            clientY: event.clientY - canvasRect.top,
        };
    };

    useEffect(() => {
        const canvas = canvasRef.current;
        if (canvas) {
            const context = canvas.getContext('2d');
            // Qualquer inicialização de contexto adicional pode ser feita aqui
        }
    }, []);

    useEffect(() => {
        lineWidthRef.current = lineWidth;
        colorRef.current = color;
    }, [lineWidth, color]);

    const handleMouseDown = (event) => {
        const { clientX, clientY } = getMousePosition(event);

        if (!toolType) {
            console.log('Nenhuma ferramenta selecionada!');
            return;
        }

        if (
            toolType === toolTypes.SQUARE ||
            toolType === toolTypes.LINE ||
            toolType === toolTypes.PENCIL ||
            toolType === toolTypes.SPRAY ||
            toolType === toolTypes.ERASER ||
            toolType === toolTypes.CIRCLE ||
            toolType === toolTypes.TRIANGLE
        ) {
            setAction(actions.DRAWING);

            const element = {
                points: [{ x: clientX, y: clientY }],
                toolType,
                lineWidth: lineWidthRef.current,
                color: colorRef.current,
            };

            updateElement(
                {
                    id: uuid(),
                    points: [{ x: clientX, y: clientY }],
                    type: toolType,
                    color: element.color,
                    lineWidth: element.lineWidth,
                },
                [],
                userId,
                arteId,
                salaUUID
            );

            setPoints(element.points);
        }
    };

    const handleMouseUp = () => {
        if (action === actions.DRAWING) {
            flushBuffer(salaUUID, arteId, userId, true);
            setAction(null);
            setPoints([]);
        }
    };

    const handleMouseMove = (event) => {
        if (action === actions.DRAWING) {
            const { clientX, clientY } = getMousePosition(event);
            const newPoints = points.concat([{ x: clientX, y: clientY }]);

            if (newPoints.length >= 50) {
                updateElement(
                    {
                        id: uuid(),
                        points: newPoints,
                        type: toolType,
                        color: colorRef.current,
                        lineWidth: lineWidthRef.current,
                    },
                    [],
                    userId,
                    arteId,
                    salaUUID
                );

                setPoints([]);
            } else {
                setPoints(newPoints);
            }
        }
    };

    return (
        <canvas
            onMouseDown={handleMouseDown}
            onMouseUp={handleMouseUp}
            onMouseMove={handleMouseMove}
            ref={canvasRef}
            width={window.innerWidth - 80}
            height={window.innerHeight - 60}
            style={{
                marginLeft: '80px',
                marginTop: '60px',
                backgroundColor: 'transparent',
                position: 'absolute',
                zIndex: 1,
            }}
        />
    );
});

export default Whiteboard;
