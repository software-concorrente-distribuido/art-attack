import React, { useRef, useLayoutEffect, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { toolTypes, actions } from '../../constants';
import { clearElements } from './whiteboardSlice';
import { jwtDecode } from 'jwt-decode';
import {
    createElement,
    updateElement,
    drawElement,
    adjustmentRequired,
    adjustElementCoordinates,
} from './utils';
import { v4 as uuid } from 'uuid';
import { updateElement as updateElementInStore } from './whiteboardSlice';
import { useUserId } from '../../hooks/useUserId';
import { flushBuffer } from './utils/updateElement';

let selectedElement;

const setSelectedElement = (el) => {
    selectedElement = el;
};

const Whiteboard = () => {
    const canvasRef = useRef();
    const toolType = useSelector((state) => state.whiteboard.tool);
    const elements = useSelector((state) => state.whiteboard.elements);
    const lineWidth = useSelector((state) => state.whiteboard.lineWidth);
    const color = useSelector((state) => state.whiteboard.color);
    const lineWidthRef = useRef(lineWidth);
    const colorRef = useRef(color);
    const userId = useUserId();
    const { arteId, salaUUID } = useParams();

    const [action, setAction] = useState(null);

    const dispatch = useDispatch();

    //pegando a posição do mouse relativo ao canvas e não à janela
    const getMousePosition = (event) => {
        const canvasRect = canvasRef.current.getBoundingClientRect();
        return {
            clientX: event.clientX - canvasRect.left,
            clientY: event.clientY - canvasRect.top,
        };
    };

    useLayoutEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');

        ctx.globalCompositeOperation = 'source-over';
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        // redesenha os elementos sempre que o estado elements do Redux é alterado
        elements.forEach((element) => {
            if (element.fromSocket === true) {
                // Desenha apenas elementos recebidos via socket
                drawElement({ context: ctx, element });
            }
        });
    }, [elements]);

    useEffect(() => {
        lineWidthRef.current = lineWidth;
        colorRef.current = color;
    }, [lineWidth, color]);

    // Limpa os elementos quando o componente é desmontado
    useEffect(() => {
        return () => {
            console.log('SAIU');
            dispatch(clearElements());
        };
    }, [dispatch]);

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

            const element = createElement({
                x1: clientX,
                y1: clientY,
                x2: clientX,
                y2: clientY,
                toolType,
                id: uuid(),
                lineWidth: lineWidthRef.current,
                color: colorRef.current,
            });

            setSelectedElement(element);

            dispatch(updateElementInStore(element));

            const index = elements.length;
            const updatedElements = [...elements, element];

            // Adicionar ponto inicial ao buffer
            updateElement(
                {
                    id: element.id,
                    x1: clientX,
                    x2: clientX,
                    y1: clientY,
                    y2: clientY,
                    type: toolType,
                    index: index,
                    color: element.color,
                    lineWidth: element.lineWidth,
                },
                updatedElements,
                userId,
                arteId,
                salaUUID
            );
        }
    };

    const handleMouseUp = () => {
        const selectedElementIndex = elements.findIndex(
            (el) => el.id === selectedElement?.id
        );

        if (selectedElementIndex !== -1) {
            // ajustar a coordenada de alguns elementos para implementar
            // a lógica de mover/redimensionar elementos posteriormente
            if (adjustmentRequired(elements[selectedElementIndex].type)) {
                const { x1, y1, x2, y2 } = adjustElementCoordinates(
                    elements[selectedElementIndex]
                );

                updateElement(
                    {
                        id: selectedElement.id,
                        index: selectedElementIndex,
                        x1,
                        x2,
                        y1,
                        y2,
                        type: elements[selectedElementIndex].type,
                        lineWidth: elements[selectedElementIndex].lineWidth,
                        color: elements[selectedElementIndex].color,
                    },
                    elements,
                    userId,
                    arteId,
                    salaUUID
                );
            }

            flushBuffer(salaUUID, arteId, userId, true);
        }

        setAction(null);
        setSelectedElement(null);
    };

    const handleMouseMove = (event) => {
        event.preventDefault();

        const { clientX, clientY } = getMousePosition(event);

        //está desenhando?
        if (action === actions.DRAWING) {
            // procurando o index do elemento selecionado
            const index = elements.findIndex(
                (el) => el.id === selectedElement.id
            );

            if (index !== -1) {
                updateElement(
                    {
                        index,
                        id: elements[index].id,
                        x1: elements[index].x1,
                        y1: elements[index].y1,
                        x2: clientX,
                        y2: clientY,
                        type: elements[index].type,
                        lineWidth: elements[index].lineWidth,
                        color: elements[index].color,
                    },
                    elements,
                    userId,
                    arteId,
                    salaUUID
                );
            }
        }
    };

    return (
        <>
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
        </>
    );
};

export default Whiteboard;
