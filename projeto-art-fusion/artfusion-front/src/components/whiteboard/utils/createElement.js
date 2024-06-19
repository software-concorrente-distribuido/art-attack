import { toolTypes } from "../../../constants";

const generateSquare = ({ x1, y1, x2, y2 }) => {
    return {
        x: x1,
        y: y1,
        width: x2 - x1,
        height: y2 - y1,
      };
};

const generateLine = ({ x1, y1, x2, y2 }) => {
    return {
        x1: x1,
        y1: y1,
        x2: x2,
        y2: y2
      };
};

export const createElement = ({ x1, y1, x2, y2, toolType, id, lineWidth }) => {
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
                    lineWidth: lineWidth
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
                    lineWidth: lineWidth
                };
            case toolTypes.PENCIL:
                return {
                    id,
                    type: toolType,
                    points: [{ x: x1, y: y1 }],
                    lineWidth: lineWidth
                }
            default:
                throw new Error('Algo deu errado ao criar o elemento');
        }
    }catch (error) {
        console.error(error);
        return null;
    }
}