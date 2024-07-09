import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { observer } from 'mobx-react';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import socketService from '../../services/socket';
import Cookies from 'js-cookie';
import Whiteboard from '../../components/whiteboard/whiteboard';
import store from '../../components/whiteboard/whiteboardStore';

const Paint = observer(() => {
    const { arteId, salaUUID } = useParams();

    useEffect(() => {
        const token = Cookies.get('user_token');

        if (token) {
            setupSocket();
        } else {
            console.error('JWT token não encontrado.');
        }

        return () => {
            socketService.disconnect();
        };
    }, [salaUUID]);

    const setupSocket = () => {
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
                            store.updateElement(message.delta);
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
            <Header />
            <Sidebar />
            <Whiteboard />
        </div>
    );
});

export default Paint;
