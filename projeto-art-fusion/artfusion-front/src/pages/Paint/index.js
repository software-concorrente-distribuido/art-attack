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

const formatDrawingData = (element, arteId, usuarioId) => {
    return {
        arteId: arteId,
        usuarioId: usuarioId,
        delta: element,
    };
};

const Paint = () => {
    const mainCanvasRef = useRef(null);
    const [isDrawing, setIsDrawing] = useState(false);
    const [points, setPoints] = useState([]);
    const { arteId, salaUUID } = useParams();
    const toolType = useSelector((state) => state.whiteboard.tool);
    const lineWidth = useSelector((state) => state.whiteboard.lineWidth);
    const color = useSelector((state) => state.whiteboard.color);
    const userId = useUserId();

    useEffect(() => {
        const token = Cookies.get('user_token');

        if (token) {
            setupSocket(token);
        } else {
            console.error('JWT token não encontrado.');
        }

        return () => {
            socketService.disconnect();
        };
    }, [salaUUID]);

    const setupSocket = (token) => {
        if (salaUUID) {
            socketService.connect(() => {
                let payload = jwtDecode(token);
                let usuario = JSON.parse(payload.usuario);
                const idUser = usuario.id;
                console.log(
                    `SE INSCREVENDO EM /topic/alteracoes/${salaUUID}/${idUser}`
                );
                socketService.subscribe(
                    `/topic/alteracoes/${salaUUID}/${idUser}`,
                    (message) => {
                        console.log('Raw message received:', message);

                        if (!message || !message.delta) {
                            console.error(
                                'Received a message without a delta.'
                            );
                            return;
                        }

                        try {
                            const ctx = mainCanvasRef.current.getContext('2d');
                            drawElement({
                                context: ctx,
                                element: message.delta,
                            });
                        } catch (error) {
                            console.error('Erro ao obter o delta:', error);
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

        const mainCanvas = mainCanvasRef.current;
        const mainContext = mainCanvas.getContext('2d');

        mainContext.strokeStyle = color;
        mainContext.lineWidth = lineWidth;
        mainContext.beginPath();
        mainContext.moveTo(offsetX, offsetY);
    };

    const handleMouseMove = (e) => {
        if (!isDrawing) return;

        const { offsetX, offsetY } = e.nativeEvent;

        const mainCanvas = mainCanvasRef.current;
        const mainContext = mainCanvas.getContext('2d');

        const newPoint = { x: offsetX, y: offsetY };

        const element = {
            id: uuid(),
            points: [...points, newPoint],
            type: toolType,
            lineWidth,
            color,
        };

        setPoints((prevPoints) => [...prevPoints, { x: offsetX, y: offsetY }]);

        const formatedData = formatDrawingData(element, arteId, userId);
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
            <Header />
            <Sidebar />
            <canvas
                ref={mainCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{
                    marginLeft: '60px',
                    marginTop: '60px',
                    backgroundColor: '#fff',
                    position: 'absolute',
                    zIndex: 0,
                }}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
            />
        </div>
    );
};

export default Paint;
