import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import socketService from '../../services/socket';
import Cookies from 'js-cookie';
import Whiteboard from '../../components/whiteboard/whiteboard';
import { updateElement as updateElementInStore } from '../../components/whiteboard/whiteboardSlice';
import { useDispatch } from 'react-redux';
import { jwtDecode } from 'jwt-decode';

const Paint = () => {
    const dispatch = useDispatch();
    const { arteId, salaUUID } = useParams();

    //TODO: fazer a reconexão
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
    }, [dispatch, salaUUID]);

    // Esse método conecta ao socket e se inscreve no tópico de alterações,
    // atualizando o estado do Redux quando uma mensagem é recebida.
    const setupSocket = (token) => {
        if (salaUUID) {
            socketService.connect(() => {
                 let payload = jwtDecode(token);
                 let usuario = JSON.parse(payload.usuario);
                 const idUser = usuario.id;
                console.log(`SE INSCREVENDO EM /topic/alteracoes/${salaUUID}/${idUser}`);
                socketService.subscribe(
                    `/topic/alteracoes/${salaUUID}/${idUser}`,
                    (message) => {
                        //console.log('Raw message received:', message);

                        if (!message || !message.delta) {
                            console.error(
                                'Received a message without a delta.'
                            );
                            return;
                        }

                        try {
                            //atualiza o redux para desenhar os elementos recebidos
                            dispatch(
                                updateElementInStore({
                                    ...message.delta,
                                    fromSocket: true,
                                })
                            );
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
