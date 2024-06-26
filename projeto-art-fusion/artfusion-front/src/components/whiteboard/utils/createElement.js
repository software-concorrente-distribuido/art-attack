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
    const radius = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    console.log('raiooooooooooooooooooooo', radius, x1, x2, y1, y2);
    return {
        x: x1,
        y: y1,
        radius,
    };
};

const generateTriangle = ({ x1, y1, x2, y2 }) => {
    let dx = x2 - x1;
    let dy = y2 - y1;
    let dist = Math.sqrt(dx * dx + dy * dy);
    let angle = Math.atan2(dy, dx);

    let height = dist / Math.sqrt(3); // Altura do triângulo equilátero
    let x3 = x1 + dx / 2 - height * Math.sin(angle);
    let y3 = y1 + dy / 2 + height * Math.cos(angle);

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

    console.log('!!!!!!!!x1, x2, y1, y2', x1, y1, x2, y2);

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
                    x: element.x,
                    y: element.y,
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
