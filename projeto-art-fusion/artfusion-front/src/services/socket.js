import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import Cookies from 'js-cookie';

class SocketService {
    constructor() {
        this.stompClient = null;
        this.connected = false;
    }

    connect(callback) {
        const token = Cookies.get('user_token');

        if (token) {
            const socketUrl = 'http://localhost:8089/artsocket';

            this.stompClient = Stomp.over(() => new SockJS(socketUrl));
            this.stompClient.reconnect_delay = 50;

            this.stompClient.connect(
                { 'X-Authorization': `Bearer ${token}` },
                (frame) => {
                    console.log('Connected: ' + frame);
                    this.connected = true;
                    if (callback) callback();
                },
                (error) => {
                    console.error('Connection error: ', error);
                    setTimeout(
                        () => this.connect(callback),
                        this.stompClient.reconnect_delay
                    );
                }
            );
        } else {
            console.error('JWT token not found');
        }
    }

    subscribe(topic, callback) {
        if (this.stompClient && this.connected) {
            this.stompClient.subscribe(topic, (message) => {
                callback(JSON.parse(message.body));
            });
        } else {
            console.error('Socket is not connected');
        }
    }

    sendElementUpdate(salaUUID, elementData) {
        if (this.stompClient && this.connected) {
            console.log('Enviado:', elementData.delta);

            try {
                this.stompClient.send(
                    `/envio/alteracoes/${salaUUID}`,
                    {},
                    JSON.stringify(elementData)
                );
            } catch (error) {
                console.error('Falha ao enviar:', error);
            }
        } else {
            console.error('Socket não está conectado, mensagem não enviada.');
            // Tentativa de reconexão
            this.connect(() => {
                this.stompClient.send(
                    `/envio/alteracoes/${salaUUID}`,
                    {},
                    JSON.stringify(elementData)
                );
            });
        }
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.disconnect(() => {
                console.log('Disconnected');
                this.connected = false;
            });
        }
    }

    isConnected() {
        return this.stompClient && this.connected;
    }
}

const socketService = new SocketService();

export default socketService;
