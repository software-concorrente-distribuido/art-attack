import { createElement } from './createElement';
import { toolTypes } from '../../../constants';
import { store } from '../../../store/store';
import { setElements } from '../whiteboardSlice';
import socketService from '../../../services/socket';
import { generateSprayPoints } from './generateSprayPoints';
import _ from 'lodash';

const throttledSendUpdates = _.throttle((salaUUID, data) => {
    socketService.sendElementUpdate(salaUUID, data);
}, 100);

const formatDrawingData = (element, arteId, usuarioId) => {
    return {
        arteId: arteId,
        usuarioId: usuarioId,
        delta: element,
    };
};

export const updateElement = (
    { id, x1, x2, y1, y2, type, index, lineWidth },
    elements,
    userId,
    arteId,
    salaUUID
) => {
    const elementsCopy = [...elements];

    try {
        switch (type) {
            case toolTypes.CIRCLE:
                // Calcular o raio com base nos novos x2 e y2
                const dx = x2 - x1;
                const dy = y2 - y1;
                const radius = Math.sqrt(dx * dx + dy * dy);
                console.log('raio no update element', radius, x1, x2, y1, y2);

                const updatedCircleElement = createElement({
                    id,
                    x1,
                    y1,
                    x2,
                    y2,
                    toolType: type,
                    lineWidth,
                    color: elementsCopy[index].color,
                    radius, // Adiciona o raio ao elemento
                });

                elementsCopy[index] = updatedCircleElement;

                store.dispatch(setElements(elementsCopy));

                socketService.sendElementUpdate(
                    salaUUID,
                    formatDrawingData(updatedCircleElement, arteId, userId)
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
                    salaUUID,
                    formatDrawingData(updatedElement, arteId, userId)
                );
                break;
            case toolTypes.PENCIL:
                const newPoint = { x: x2, y: y2 };
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

                // throttledSendUpdates(
                //     salaUUID,
                //     formatDrawingData(
                //         {
                //             id: updatedPencilElement.id,
                //             type: updatedPencilElement.type,
                //             points: [newPoint],
                //             lineWidth: updatedPencilElement.lineWidth,
                //             color: updatedPencilElement.color,
                //         },
                //         arteId,
                //         userId
                //     )
                // );

                socketService.sendElementUpdate(
                    salaUUID,
                    formatDrawingData(
                        {
                            id: updatedPencilElement.id,
                            type: updatedPencilElement.type,
                            points: [newPoint],
                            lineWidth: updatedPencilElement.lineWidth,
                            color: updatedPencilElement.color,
                        },
                        arteId,
                        userId
                    )
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
                    salaUUID,
                    formatDrawingData(updatedSprayElement, 1, userId)
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
                    salaUUID,
                    formatDrawingData(updatedEraserElement, arteId, userId)
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
