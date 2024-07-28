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
const BUFFER_LIMIT = 5;

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

            const formatedData = formatDrawingData(element, arteId, userId);

            console.log('DATAAAA', formatedData);

            socketService.sendElementUpdate(salaUUID, formatedData);

            if (buffer[id].points.length === 0) {
                delete buffer[id];
            }
        }
    });
};

export const updateElement = (
    { id, points, type, lineWidth, color },
    userId,
    arteId,
    salaUUID
) => {
    console.log('AQUI');
    if (!buffer[id]) {
        buffer[id] = { points: [], lineWidth, color, type };
    }
    buffer[id].points.push(...points);

    if (buffer[id].points.length >= BUFFER_LIMIT) {
        flushBuffer(salaUUID, arteId, userId);
    }
};
