import React, { useState, useRef, useEffect } from 'react';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import socketService from '../../services/socket';
import ApiServices from '../../services/apiServices';
import Cookies from 'js-cookie';
import Whiteboard from '../../components/whiteboard/whiteboard';
import { updateElement as updateElementInStore } from '../../components/whiteboard/whiteboardSlice';
import { useDispatch } from 'react-redux';

const Paint = () => {
    const dispatch = useDispatch();
    const [salaUUID, setSalaUUID] = useState(null);

    useEffect(() => {
        const token = Cookies.get('user_token');

        if (token) {
            ApiServices.abrirSala(1, token)
                .then((salaUUID) => {
                    console.debug('Sala uid', salaUUID);
                    setSalaUUID(salaUUID); // Diretamente seta o UUID retornado
                    setupSocket(salaUUID);
                })
                .catch((error) => {
                    console.error('Erro ao abrir a sala:', error);
                });
        } else {
            console.error('JWT token não encontrado.');
        }

        return () => {
            socketService.disconnect();
        };
    }, [dispatch]);

    const setupSocket = (salaUUID) => {
        if (salaUUID) {
            socketService.connect(() => {
                console.log('Subscribing to /topic/alteracoes...', salaUUID);
                socketService.subscribe(
                    `/topic/alteracoes/${salaUUID}`,
                    (message) => {
                        console.log('Raw message received:', message);

                        if (!message || !message.delta) {
                            console.error(
                                'Received a message without a delta.'
                            );
                            return;
                        }

                        try {
                            console.log('Delta received:', message.delta);
                            dispatch(updateElementInStore(message.delta));
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

    return (
        <div>
            {/* <Header onUndo={handleUndo} onRedo={handleRedo} /> */}
            <Header />
            <Sidebar />
            <Whiteboard />
        </div>
    );
};

export default Paint;
