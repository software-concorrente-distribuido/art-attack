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
import { updateElement } from '../../components/whiteboard/utils/updateElement';
import { useUserId } from '../../hooks/useUserId';
import { flushBuffer } from '../../components/whiteboard/utils/updateElement';
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
    const [isDrawing, setIsDrawing] = useState(false);
    const [points, setPoints] = useState([]);
    const { arteId, salaUUID } = useParams();
    const [title, setTitle] = useState('Sem título');
    const toolType = useSelector((state) => state.whiteboard.tool);
    const lineWidth = useSelector((state) => state.whiteboard.lineWidth);
    const color = useSelector((state) => state.whiteboard.color);
    const userId = useUserId();

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
        const { offsetX, offsetY } = e.nativeEvent;
        setIsDrawing(true);
        setPoints([{ x: offsetX, y: offsetY }]);

        const previewCanvas = previewCanvasRef.current;
        const previewContext = previewCanvas.getContext('2d');

        previewContext.strokeStyle = color;
        previewContext.lineWidth = lineWidth;
        previewContext.beginPath();
        previewContext.moveTo(offsetX, offsetY);
    };

    const handleMouseMove = (e) => {
        if (!isDrawing) return;

        const { offsetX, offsetY } = e.nativeEvent;

        const previewCanvas = previewCanvasRef.current;
        const previewContext = previewCanvas.getContext('2d');

        const newPoint = { x: offsetX, y: offsetY };

        let element;

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
    };

    const handleMouseUp = (e) => {
        setIsDrawing(false);

        const { offsetX, offsetY } = e.nativeEvent;
        const newPoints = [...points, { x: offsetX, y: offsetY }];

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
            <canvas
                ref={previewCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{
                    marginLeft: '60px',
                    marginTop: '60px',
                    backgroundColor: 'transparent',
                    position: 'absolute',
                    pointerEvents: 'auto',
                    zIndex: 0,
                }}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
            />
            <canvas
                ref={mainCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{
                    marginLeft: '60px',
                    marginTop: '60px',
                    backgroundColor: '#fff',
                    position: 'absolute',
                    pointerEvents: 'none',
                    zIndex: 1,
                }}
            />
        </div>
    );
};

export default Paint;
