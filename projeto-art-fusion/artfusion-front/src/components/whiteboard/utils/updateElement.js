import { createElement } from './createElement';
import { toolTypes } from '../../../constants';
import { store } from '../../../store/store';
import { setElements } from '../whiteboardSlice';
import socketService from '../../../services/socket';
import { generateSprayPoints } from './generateSprayPoints';
import { updateElement as updateElementInStore } from '../whiteboardSlice';
import _ from 'lodash';

const formatDrawingData = (element, arteId, usuarioId) => {
    return {
        arteId: arteId,
        usuarioId: usuarioId,
        delta: element,
    };
};

const buffer = {};
const BUFFER_LIMIT = 1;

export const flushBuffer = (salaUUID, arteId, userId, forceFlush = false) => {
    Object.keys(buffer).forEach((id) => {
        if (
            buffer[id].points.length > 0 &&
            (buffer[id].points.length >= BUFFER_LIMIT || forceFlush)
        ) {
            const pointsToSend = buffer[id].points.splice(0, BUFFER_LIMIT);
            const element = {
                id,
                points: pointsToSend,
                type: buffer[id].type,
                lineWidth: buffer[id].lineWidth,
                color: buffer[id].color,
            };

            socketService.sendElementUpdate(
                salaUUID,
                formatDrawingData(element, arteId, userId)
            );

            if (buffer[id].points.length === 0) {
                delete buffer[id];
            }
        }
    });
};

export const updateElement = (
    { id, x1, x2, y1, y2, type, index, lineWidth, color },
    elements,
    userId,
    arteId,
    salaUUID
) => {
    const elementsCopy = [...elements];

    try {
        switch (type) {
            case toolTypes.ERASER:
            case toolTypes.PENCIL:
                const newPoint = { x: x2, y: y2 };
                if (!buffer[id]) {
                    buffer[id] = { points: [], lineWidth, color, type };
                }
                buffer[id].points.push(newPoint);

                if (buffer[id].points.length >= BUFFER_LIMIT) {
                    flushBuffer(salaUUID, arteId, userId);
                }

                // elementsCopy[index] = {
                //     ...elementsCopy[index],
                //     points: [
                //         ...elementsCopy[index].points,
                //         {
                //             x: x2,
                //             y: y2,
                //         },
                //     ],
                // };
                // store.dispatch(setElements(elementsCopy));
                break;
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

                // elementsCopy[index] = updatedCircleElement;

                // store.dispatch(setElements(elementsCopy));

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
                    fromSocket: false,
                    toolType: type,
                    lineWidth: elementsCopy[index].lineWidth,
                    color: elementsCopy[index].color,
                });

                // elementsCopy[index] = updatedElement;

                // store.dispatch(setElements(elementsCopy));

                socketService.sendElementUpdate(
                    salaUUID,
                    formatDrawingData(updatedElement, arteId, userId)
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

export const flushAllBuffers = (salaUUID, arteId, userId) => {
    flushBuffer(salaUUID, arteId, userId, true);
};
