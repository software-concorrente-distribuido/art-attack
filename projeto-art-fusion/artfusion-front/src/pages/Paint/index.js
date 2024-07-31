import React, { useState, useRef, useEffect } from 'react';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import socketService from '../../services/socket';
import Cookies from 'js-cookie';
import { useParams } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { drawElement } from '../../components/whiteboard/utils';
import { useSelector } from 'react-redux';
import { toolTypes, actions } from '../../constants';
import { useUserId } from '../../hooks/useUserId';
import { v4 as uuid } from 'uuid';
import apiServices from '../../services/apiServices';

const getAlteracaoEntradaDtoObject = (element, arteId, usuarioId) => {
    return {
        arteId: arteId,
        usuarioId: usuarioId,
        delta: element,
    };
};

const Paint = () => {
    const mainCanvasRef = useRef(null);
    const previewCanvasRef = useRef(null);
    const formCanvasRef = useRef(null);
    const [isDrawing, setIsDrawing] = useState(false);
    const [isFormDrawing, setIsFormDrawing] = useState(false);
    const [points, setPoints] = useState([]);
    const { arteId, salaUUID } = useParams();
    const [startPos, setStartPos] = useState({ x: 0, y: 0 });
    const [title, setTitle] = useState('Sem título');
    const toolType = useSelector((state) => state.whiteboard.tool);
    const lineWidth = useSelector((state) => state.whiteboard.lineWidth);
    const color = useSelector((state) => state.whiteboard.color);
    const userId = useUserId();
    const containerRef = useRef(null);

    const [zoomLevel, setZoomLevel] = useState(1);

    const handleZoom = (event) => {
        event.preventDefault();
        const scaleAdjustment = event.deltaY > 0 ? 0.9 : 1.1;
        const newZoomLevel = zoomLevel * scaleAdjustment;
        setZoomLevel(Math.max(1, newZoomLevel));
    };

    useEffect(() => {
        const handleResize = () => {
            if (zoomLevel < 1) setZoomLevel(1);
        };

        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, [zoomLevel]);

    useEffect(() => {
        const token = Cookies.get('user_token');

        if (token) {
            carregarTituloArte();

            setupSocket(token);
        } else {
            console.error('JWT token não encontrado.');
        }

        return () => {
            socketService.disconnect();
        };
    }, [salaUUID]);

    async function carregarTituloArte() {
        const data = await apiServices.obterSala(salaUUID);
        setTitle(data.titulo);
    }

    async function editarTitulo(nome, arteId) {
        const data = await apiServices.editarTitulo(nome, arteId);
        setTitle(data.titulo);
    }

    const setupSocket = (token) => {
        if (salaUUID) {
            socketService.connect(() => {
                let payload = jwtDecode(token);
                let usuario = JSON.parse(payload.usuario);
                const idUser = usuario.id;

                socketService.subscribe(
                    `/topic/alteracoes/${salaUUID}/${idUser}`,
                    (message) => {
                        function desenhar(alteracao) {
                            if (!alteracao || !alteracao.delta) {
                                console.error(
                                    'Received a message without a delta.'
                                );
                                return;
                            }
                            try {
                                const ctx =
                                    mainCanvasRef.current.getContext('2d');
                                drawElement({
                                    context: ctx,
                                    element: alteracao.delta,
                                });
                            } catch (error) {
                                console.error('Erro ao obter o delta:', error);
                            }
                        }
                        if (message['numeroLote']) {
                            message['alteracoes'].forEach((alteracao) => {
                                desenhar(alteracao);
                            });
                        } else {
                            desenhar(message);
                        }
                    }
                );
            });
        } else {
            console.error(
                'Não foi possível conectar ao Socket, pois não há uma sala criada.'
            );
        }
    };

    const handleMouseDown = (e) => {
        if (!toolType) {
            console.log('Nenhuma ferramenta selecionada!');
            return;
        }

        const { offsetX, offsetY } = e.nativeEvent;
        setIsDrawing(true);
        setStartPos({ x: offsetX, y: offsetY });
        setPoints([{ x: offsetX, y: offsetY }]);

        if (
            [
                toolTypes.CIRCLE,
                toolTypes.LINE,
                toolTypes.SQUARE,
                toolTypes.TRIANGLE,
            ].includes(toolType)
        ) {
            setIsFormDrawing(true);
        }
    };

    const handleMouseMove = (e) => {
        if (!isDrawing) return;

        const { offsetX, offsetY } = e.nativeEvent;

        const previewContext = previewCanvasRef.current.getContext('2d');

        const formCanvas = formCanvasRef.current;
        const formContext = formCanvas.getContext('2d');

        const newPoint = { x: offsetX, y: offsetY };

        let element;

        if (toolType === toolTypes.PENCIL || toolType === toolTypes.ERASER) {
            if (points.length < 25) {
                element = {
                    id: uuid(),
                    points: [...points, newPoint],
                    type: toolType,
                    lineWidth,
                    color,
                };

                setPoints((prevPoints) => [
                    ...prevPoints,
                    { x: offsetX, y: offsetY },
                ]);
            } else {
                let newArr = [...points.slice(-3), newPoint];

                element = {
                    id: uuid(),
                    points: newArr,
                    type: toolType,
                    lineWidth,
                    color,
                };

                setPoints(newArr);
            }

            drawElement({
                context: previewContext,
                element: element,
            });

            const formatedData = getAlteracaoEntradaDtoObject(
                element,
                arteId,
                userId
            );

            socketService.sendElementUpdate(salaUUID, formatedData);
        }

        if (
            [
                toolTypes.CIRCLE,
                toolTypes.LINE,
                toolTypes.SQUARE,
                toolTypes.TRIANGLE,
            ].includes(toolType)
        ) {
            element = {
                id: uuid(),
                x1: startPos.x,
                y1: startPos.y,
                x2: offsetX,
                y2: offsetY,
                type: toolType,
                lineWidth,
                color,
            };

            formContext.clearRect(0, 0, formCanvas.width, formCanvas.height);

            drawElement({
                context: formContext,
                element: element,
            });
        }
    };

    const handleMouseUp = (e) => {
        setIsDrawing(false);

        const { offsetX, offsetY } = e.nativeEvent;
        const newPoints = [...points, { x: offsetX, y: offsetY }];

        const previewCanvas = previewCanvasRef.current;
        const previewContext = previewCanvas.getContext('2d');

        const formCanvas = formCanvasRef.current;
        const formContext = formCanvas.getContext('2d');

        if (!isFormDrawing) return;
        if (
            [
                toolTypes.CIRCLE,
                toolTypes.LINE,
                toolTypes.SQUARE,
                toolTypes.TRIANGLE,
            ].includes(toolType)
        ) {
            const element = {
                id: uuid(),
                x1: startPos.x,
                y1: startPos.y,
                x2: offsetX,
                y2: offsetY,
                type: toolType,
                lineWidth,
                color,
            };

            formContext.clearRect(0, 0, formCanvas.width, formCanvas.height);

            drawElement({
                context: previewContext,
                element: element,
            });

            const formatedData = getAlteracaoEntradaDtoObject(
                element,
                arteId,
                userId
            );

            socketService.sendElementUpdate(salaUUID, formatedData);

            setIsFormDrawing(false);
        }

        setStartPos([]);
        setPoints([]);
    };

    return (
        <div>
            <Header
                title={title}
                setTitle={setTitle}
                editarTitulo={editarTitulo}
                arteId={arteId}
            />
            <Sidebar />
            <div
                ref={containerRef}
                style={{
                    overflow: 'auto',
                    position: 'relative',
                    marginLeft: '60px',
                    marginTop: '60px',
                    width: '1280px',
                    height: '650px',
                    backgroundColor: '#000', // Cor de fundo para destacar a área do canvas
                }}
                onWheel={handleZoom}
            >
                <div
                    style={{
                        width: `${1280 * zoomLevel}px`,
                        height: `${650 * zoomLevel}px`,
                        position: 'relative',
                    }}
                >
                    <canvas
                        ref={previewCanvasRef}
                        width="1280"
                        height="650"
                        style={{
                            // marginLeft: '60px',
                            // marginTop: '60px',
                            backgroundColor: '#fff',
                            position: 'absolute',
                            pointerEvents: 'auto',
                            zIndex: 0, // previewCanvasRef fica abaixo, usado para desenhos temporários/local
                            transform: `scale(${zoomLevel})`,
                            transformOrigin: 'top left',
                        }}
                        onMouseDown={handleMouseDown}
                        onMouseMove={handleMouseMove}
                        onMouseUp={handleMouseUp}
                        onMouseLeave={handleMouseUp}
                        onWheel={handleZoom}
                    />
                    <canvas
                        ref={formCanvasRef}
                        width="1280"
                        height="650"
                        style={{
                            // marginLeft: '60px',
                            // marginTop: '60px',
                            backgroundColor: 'transparent',
                            position: 'absolute',
                            pointerEvents: 'auto',
                            zIndex: 2, // usdo para formas, fica abaixo do canva principal
                            transform: `scale(${zoomLevel})`,
                            transformOrigin: 'top left',
                        }}
                        onWheel={handleZoom}
                        onMouseDown={handleMouseDown}
                        onMouseMove={handleMouseMove}
                        onMouseUp={handleMouseUp}
                        onMouseLeave={handleMouseUp}
                    />
                    <canvas
                        ref={mainCanvasRef}
                        width="1280"
                        height="650"
                        style={{
                            // marginLeft: '60px',
                            // marginTop: '60px',
                            backgroundColor: 'transparent',
                            position: 'absolute',
                            pointerEvents: 'none',
                            zIndex: 1,
                            transform: `scale(${zoomLevel})`,
                            transformOrigin: 'top left',
                        }}
                        onWheel={handleZoom}
                    />
                </div>
            </div>
        </div>
    );
};

export default Paint;
