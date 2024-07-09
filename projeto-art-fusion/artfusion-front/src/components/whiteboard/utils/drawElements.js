import { toolTypes } from '../../../constants';
import { getStroke } from 'perfect-freehand';
import { getSvgPathFromStroke } from './getSvgPathFromStroke';

const drawRectangle = (context, { x1, y1, x2, y2, lineWidth, color }) => {
    context.beginPath();
    context.rect(x1, y1, x2 - x1, y2 - y1);
    context.lineWidth = lineWidth;
    context.strokeStyle = color;
    context.stroke();
};

const drawCircle = (context, { x1, y1, x2, y2, lineWidth, color }) => {
    const dx = x2 - x1;
    const dy = y2 - y1;
    const radius = Math.sqrt(dx * dx + dy * dy);

    if (radius > 0) {
        context.beginPath();
        context.arc(x1, y1, radius, 0, 2 * Math.PI);
        context.strokeStyle = color;
        context.lineWidth = lineWidth;
        context.stroke();
    }
};

const drawTriangle = (
    context,
    { x1, y1, x2, y2, x3, y3, lineWidth, color }
) => {
    context.beginPath();
    context.moveTo(x1, y1);
    context.lineTo(x2, y2);
    context.lineTo(x3, y3);
    context.closePath(); // Fecha o caminho e conecta com o ponto de início
    context.strokeStyle = color;
    context.lineWidth = lineWidth;
    context.stroke();
};

const drawLine = (context, { x1, y1, x2, y2, lineWidth, color }) => {
    context.beginPath(); // Começa um novo caminho no desenho
    context.moveTo(x1, y1); // Move o ponto de início do caminho para (x1, y1)
    context.lineTo(x2, y2); // Adiciona um novo ponto ao caminho em (x2, y2)
    context.strokeStyle = color; // Define a cor da linha
    context.lineWidth = lineWidth; // Define a largura da linha
    context.stroke(); // Desenha a linha definida pelo caminho
};

const drawPencilElement = (context, element) => {
    context.globalCompositeOperation =
        element.type === toolTypes.ERASER ? 'destination-out' : 'source-over';

    const myStroke = getStroke(element.points, {
        size: element.lineWidth, // Tamanho fixo para todos os pontos
        thinning: 0, // Desabilita o afinamento baseado na velocidade ou pressão
    });

    const pathData = getSvgPathFromStroke(myStroke);

    const myPath = new Path2D(pathData);
    context.fillStyle = element.color;
    context.fill(myPath);
};

const drawSprayElement = (context, element) => {
    context.fillStyle = element.color;
    context.globalAlpha = 0.5;

    element.sprayPoints.forEach((point) => {
        context.fillRect(point.x, point.y, 1, 1);
    });

    context.globalAlpha = 1.0;
};

export const drawElement = ({ context, element }) => {
    try {
        if (element.type === toolTypes.ERASER) {
            context.globalCompositeOperation = 'destination-out'; // Define para apagar
            context.lineWidth = element.lineWidth; // Define a largura da "borracha"
        } else {
            context.globalCompositeOperation = 'source-over'; // Padrão para desenhar
        }

        switch (element.type) {
            case toolTypes.SQUARE:
                drawRectangle(context, element);
                break;
            case toolTypes.LINE:
                drawLine(context, element);
                break;
            case toolTypes.PENCIL:
            case toolTypes.ERASER:
                drawPencilElement(context, element);
                break;
            case toolTypes.SPRAY:
                drawSprayElement(context, element);
                break;
            case toolTypes.CIRCLE:
                drawCircle(context, element);
                break;
            case toolTypes.TRIANGLE:
                drawTriangle(context, element);
                break;
            default:
                throw new Error('Algo deu erado ao desenhar o elemento');
        }
    } catch (error) {
        console.error(error);
        return null;
    }
};
