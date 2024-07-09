import { toolTypes } from '../../../constants';
import { generateSprayPoints } from './generateSprayPoints';

const generateSquare = ({ x1, y1, x2, y2 }) => {
    return {
        x: x1,
        y: y1,
        width: x2 - x1,
        height: y2 - y1,
    };
};

const generateCircle = ({ x1, y1, x2, y2 }) => {
    const dx = x2 - x1;
    const dy = y2 - y1;
    const radius = Math.sqrt(dx * dx + dy * dy);
    console.log('raiooooooooooooooooooooo', radius, x1, x2, y1, y2);
    return {
        x: x1,
        y: y1,
        radius,
    };
};

const generateTriangle = ({ x1, y1, x2, y2 }) => {
    const dx = x2 - x1;
    const dy = y2 - y1;

    // Calcular o terceiro ponto do triângulo
    const x3 = x1 - dx;
    const y3 = y2;

    return {
        x1,
        y1,
        x2,
        y2,
        x3,
        y3,
    };
};

const generateLine = ({ x1, y1, x2, y2 }) => {
    return {
        x1: x1,
        y1: y1,
        x2: x2,
        y2: y2,
    };
};

export const createElement = ({
    x1,
    y1,
    x2,
    y2,
    toolType,
    id,
    lineWidth,
    color,
}) => {
    let element;

    try {
        switch (toolType) {
            case toolTypes.SQUARE:
                element = generateSquare({ x1, y1, x2, y2 });
                return {
                    id: id,
                    element,
                    type: toolType,
                    x1,
                    y1,
                    x2,
                    y2,
                    lineWidth,
                    color,
                };
            case toolTypes.CIRCLE:
                element = generateCircle({ x1, y1, x2, y2 });
                console.log('generate circle', element.radius);
                return {
                    id,
                    element,
                    type: toolType,
                    x1,
                    y1,
                    x2,
                    y2,
                    radius: element.radius,
                    lineWidth,
                    color,
                };
            case toolTypes.TRIANGLE:
                element = generateTriangle({ x1, y1, x2, y2 });
                return {
                    id: id,
                    element,
                    type: toolType,
                    x1,
                    y1,
                    x2,
                    y2,
                    x3: element.x3,
                    y3: element.y3,
                    lineWidth,
                    color,
                };
            case toolTypes.LINE:
                element = generateLine({ x1, x2, y1, y2 });
                return {
                    id: id,
                    element,
                    type: toolType,
                    x1,
                    y1,
                    x2,
                    y2,
                    lineWidth,
                    color,
                };
            case toolTypes.PENCIL:
                return {
                    id,
                    type: toolType,
                    points: [{ x: x1, y: y1 }],
                    lineWidth: lineWidth,
                    color,
                };
            case toolTypes.SPRAY:
                const sprayPoints = generateSprayPoints(x1, y1, lineWidth);
                return {
                    id,
                    type: toolType,
                    points: [{ x: x1, y: y1 }],
                    sprayPoints,
                    lineWidth: lineWidth,
                    color,
                };
            case toolTypes.ERASER:
                return {
                    id,
                    type: toolType,
                    points: [{ x: x1, y: y1 }],
                    lineWidth: lineWidth, // Garantir uma largura suficiente para a borracha
                };
            default:
                throw new Error('Algo deu errado ao criar o elemento');
        }
    } catch (error) {
        console.error(error);
        return null;
    }
};
