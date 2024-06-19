import { createElement } from "./createElement";
import { toolTypes } from "../../../constants";
import { store } from "../../../store/store";
import { setElements } from "../whiteboardSlice";
import socketService from '../../../services/socket';

const formatDrawingData = (element, arteId, usuarioId) => {
    return {
        arteId: arteId,
        usuarioId: usuarioId,
        delta: element
    };
};

export const updateElement = ({ id, x1, x2, y1, y2, type, index, lineWidth }, elements) => {
    const elementsCopy = [...elements];

    try{
    switch (type) {
        case toolTypes.LINE:
        case toolTypes.SQUARE:
            const updatedElement = createElement({
                id,
                x1,
                y1,
                x2,
                y2,
                toolType: type
            });

            elementsCopy[index] = updatedElement;

            store.dispatch(setElements(elementsCopy));

            socketService.sendElementUpdate(formatDrawingData(updatedElement, 1, 1));
            break;
        case toolTypes.PENCIL:
            elementsCopy[index] = {
                ...elementsCopy[index],
                points: [
                    ...elementsCopy[index].points,
                    {
                        x: x2,
                        y: y2,
                    }
                ]
            }

            const updatedPencilElement = elementsCopy[index]

            store.dispatch(setElements(elementsCopy));

            socketService.sendElementUpdate(formatDrawingData(updatedPencilElement, 1, 1));
            break;
        default:
            throw new Error('Algo deu errado ao atualizar o elemento');
        }
    } catch (error) {
        console.error(error);
        return null;
    }
}