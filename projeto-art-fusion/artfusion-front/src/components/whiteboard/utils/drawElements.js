import { toolTypes } from "../../../constants";
import { getStroke } from 'perfect-freehand';
import { getSvgPathFromStroke } from "./getSvgPathFromStroke";

const drawRectangle = (context, { x1, y1, x2, y2, lineWidth }) => {
    console.log("!!!!!!!!!!!!!!!!!! line width ", lineWidth);
    context.lineWidth = lineWidth;
    context.beginPath();
    context.rect(x1, y1, x2 - x1, y2 - y1);
    context.stroke();
};

const drawLine = (context, { x1, y1, x2, y2, lineWidth }) => {
    context.beginPath(); // Começa um novo caminho no desenho
    context.moveTo(x1, y1); // Move o ponto de início do caminho para (x1, y1)
    context.lineTo(x2, y2); // Adiciona um novo ponto ao caminho em (x2, y2)
    context.strokeStyle = '#000'; // Define a cor da linha
    context.lineWidth = lineWidth; // Define a largura da linha
    context.stroke(); // Desenha a linha definida pelo caminho
};

const drawPencilElement = (context, element) => {
    const myStroke = getStroke(element.points, {
        size: element.lineWidth, // Tamanho fixo para todos os pontos
        thinning: 0, // Desabilita o afinamento baseado na velocidade ou pressão
    });

    const pathData = getSvgPathFromStroke(myStroke);

    const myPath = new Path2D(pathData);
    context.fill(myPath);
};

export const drawElement = ({ context, element }) => {
    try {
        switch (element.type) {
            case toolTypes.SQUARE:
                drawRectangle(context, element);
                break;
            case toolTypes.LINE:
                drawLine(context, element);
                break;
            case toolTypes.PENCIL:
                drawPencilElement(context, element);
                break;
            default:
                throw new Error("Algo deu erado ao desenhar o elemento");
        }
    } catch (error) {
        console.error(error);
        return null;
    }
}