import { createElement } from './createElement';
import { toolTypes } from '../../../constants';
import { store } from '../../../store/store';
import { setElements } from '../whiteboardSlice';
import socketService from '../../../services/socket';
import { generateSprayPoints } from './generateSprayPoints';

const formatDrawingData = (element, arteId, usuarioId) => {
    return {
        arteId: arteId,
        usuarioId: usuarioId,
        delta: element,
    };
};

export const updateElement = (
    { id, x1, x2, y1, y2, type, index, lineWidth },
    elements
) => {
    const elementsCopy = [...elements];

    try {
        switch (type) {
            case toolTypes.CIRCLE:
                // Recalcular o raio com base nos novos x2 e y2
                const dx = x2 - elementsCopy[index].x1;
                const dy = y2 - elementsCopy[index].y1;
                const radius = Math.sqrt(dx * dx + dy * dy);

                // Atualizar o elemento círculo com as novas propriedades
                const updatedCircleElement = createElement({
                    id,
                    x1: elementsCopy[index].x1,
                    y1: elementsCopy[index].y1,
                    x2,
                    y2,
                    toolType: type,
                    lineWidth: elementsCopy[index].lineWidth,
                    color: elementsCopy[index].color,
                });

                updatedCircleElement.element.radius = radius; // Garantir que o raio esteja atualizado

                elementsCopy[index] = updatedCircleElement;

                store.dispatch(setElements(elementsCopy));

                socketService.sendElementUpdate(
                    formatDrawingData(updatedCircleElement, 1, 1)
                );
                break;
            case toolTypes.TRIANGLE:
            case toolTypes.LINE:
            case toolTypes.SQUARE:
                const updatedElement = createElement({
                    id,
                    x1,
                    y1,
                    x2,
                    y2,
                    toolType: type,
                    lineWidth: elementsCopy[index].lineWidth,
                    color: elementsCopy[index].color,
                });

                elementsCopy[index] = updatedElement;

                store.dispatch(setElements(elementsCopy));

                socketService.sendElementUpdate(
                    formatDrawingData(updatedElement, 1, 1)
                );
                break;
            case toolTypes.PENCIL:
                elementsCopy[index] = {
                    ...elementsCopy[index],
                    points: [
                        ...elementsCopy[index].points,
                        {
                            x: x2,
                            y: y2,
                        },
                    ],
                };

                const updatedPencilElement = elementsCopy[index];

                store.dispatch(setElements(elementsCopy));

                socketService.sendElementUpdate(
                    formatDrawingData(updatedPencilElement, 1, 1)
                );
                break;
            case toolTypes.SPRAY:
                const newSprayPoints = generateSprayPoints(x2, y2, lineWidth);
                elementsCopy[index] = {
                    ...elementsCopy[index],
                    points: [
                        ...elementsCopy[index].points,
                        {
                            x: x2,
                            y: y2,
                        },
                    ],
                    sprayPoints: [
                        ...elementsCopy[index].sprayPoints,
                        ...newSprayPoints,
                    ],
                };

                const updatedSprayElement = elementsCopy[index];

                store.dispatch(setElements(elementsCopy));

                socketService.sendElementUpdate(
                    formatDrawingData(updatedSprayElement, 1, 1)
                );
                break;
            case toolTypes.ERASER:
                elementsCopy[index] = {
                    ...elementsCopy[index],
                    points: [
                        ...elementsCopy[index].points,
                        {
                            x: x2,
                            y: y2,
                        },
                    ],
                };
                const updatedEraserElement = elementsCopy[index];
                store.dispatch(setElements(elementsCopy));
                socketService.sendElementUpdate(
                    formatDrawingData(updatedEraserElement, 1, 1)
                );
                break;
            default:
                throw new Error('Algo deu errado ao atualizar o elemento');
        }
    } catch (error) {
        console.error(error);
        return null;
    }
};
