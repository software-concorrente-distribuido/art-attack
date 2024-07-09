import React, { useRef, useLayoutEffect, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { observer } from 'mobx-react';
import { toolTypes, actions } from '../../constants';
import {
    createElement,
    updateElement,
    drawElement,
    adjustmentRequired,
    adjustElementCoordinates,
} from './utils';
import { v4 as uuid } from 'uuid';
import { useUserId } from '../../hooks/useUserId';
import store from './whiteboardStore';
import { autorun } from 'mobx';

let selectedElement;

const setSelectedElement = (el) => {
    selectedElement = el;
};

const Whiteboard = observer(() => {
    const canvasRef = useRef();
    const userId = useUserId();
    const { arteId, salaUUID } = useParams();
    const [action, setAction] = useState(null);

    const getMousePosition = (event) => {
        const canvasRect = canvasRef.current.getBoundingClientRect();
        return {
            clientX: event.clientX - canvasRect.left,
            clientY: event.clientY - canvasRect.top,
        };
    };

    // useLayoutEffect(() => {
    //     const canvas = canvasRef.current;
    //     const ctx = canvas.getContext('2d');

    //     ctx.globalCompositeOperation = 'source-over';
    //     ctx.clearRect(0, 0, canvas.width, canvas.height);

    //     console.log(
    //         'Redrawing elements:',
    //         store.elements.map((e) => e.id)
    //     );

    //     store.elements.forEach((element) => {
    //         drawElement({ context: ctx, element });
    //     });
    // }, [store.elements]);

    useEffect(() => {
        return () => {
            store.clearElements();
        };
    }, []);

    useEffect(() => {
        const disposer = autorun(() => {
            // Esta função será chamada sempre que `store.elements` mudar
            const canvas = canvasRef.current;
            if (canvas) {
                const ctx = canvas.getContext('2d');
                ctx.globalCompositeOperation = 'source-over';
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                store.elements.forEach((element) => {
                    drawElement({ context: ctx, element });
                });
            }
        });

        return () => disposer(); // Limpar o autorun quando o componente desmontar
    }, []); // Dependências vazias significam que isso só é configurado uma vez

    const handleMouseDown = (event) => {
        const { clientX, clientY } = getMousePosition(event);

        if (!store.tool) {
            console.log('Nenhuma ferramenta selecionada!');
            return;
        }

        if (
            store.tool === toolTypes.SQUARE ||
            store.tool === toolTypes.LINE ||
            store.tool === toolTypes.PENCIL ||
            store.tool === toolTypes.SPRAY ||
            store.tool === toolTypes.ERASER ||
            store.tool === toolTypes.CIRCLE ||
            store.tool === toolTypes.TRIANGLE
        ) {
            setAction(actions.DRAWING);

            const element = createElement({
                x1: clientX,
                y1: clientY,
                x2: clientX,
                y2: clientY,
                toolType: store.tool,
                id: uuid(),
                lineWidth: store.lineWidth,
                color: store.color,
            });

            setSelectedElement(element);
            store.updateElement(element);
        }
    };

    const handleMouseUp = () => {
        const selectedElementIndex = store.elements.findIndex(
            (el) => el.id === selectedElement?.id
        );

        if (selectedElementIndex !== -1) {
            if (adjustmentRequired(store.elements[selectedElementIndex].type)) {
                const { x1, y1, x2, y2 } = adjustElementCoordinates(
                    store.elements[selectedElementIndex]
                );

                updateElement(
                    {
                        id: selectedElement.id,
                        index: selectedElementIndex,
                        x1,
                        x2,
                        y1,
                        y2,
                        type: store.elements[selectedElementIndex].type,
                        lineWidth:
                            store.elements[selectedElementIndex].lineWidth,
                        color: store.elements[selectedElementIndex].color,
                    },
                    store.elements,
                    userId,
                    arteId,
                    salaUUID
                );
            }
        }

        setAction(null);
        setSelectedElement(null);
    };

    const handleMouseMove = (event) => {
        event.preventDefault();

        const { clientX, clientY } = getMousePosition(event);

        if (action === actions.DRAWING) {
            const index = store.elements.findIndex(
                (el) => el.id === selectedElement.id
            );

            if (index !== -1) {
                updateElement(
                    {
                        index,
                        id: store.elements[index].id,
                        x1: store.elements[index].x1,
                        y1: store.elements[index].y1,
                        x2: clientX,
                        y2: clientY,
                        type: store.elements[index].type,
                        lineWidth: store.elements[index].lineWidth,
                        color: store.elements[index].color,
                    },
                    store.elements,
                    userId,
                    arteId,
                    salaUUID
                );
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
