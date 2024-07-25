import api from '../api';
import Cookies from "js-cookie";

class ApiServices {
    // Função para abrir uma sala
    static async abrirSala(arteId, accessToken) {
        try {
            const response = await api.post(
                '/sala/abrir',
                {
                    arteId: arteId,
                },
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            console.debug(response.data.uuid);

            // Assumindo que o backend retorna o UUID da sala em um campo chamado 'salaUUID'
            return response.data.uuid;
        } catch (error) {
            throw error; // Lançar o erro para ser tratado por quem chamar essa função
        }
    }

    // // Função para listar todas as artes
    // static async listarArtes() {
    //     console.debug('listando artees');
    //     try {
    //         const response = await api.get('/arte/listar');
    //         console.debug(response);
    //         return response.data; // Retorna diretamente o array de artes
    //     } catch (error) {
    //         console.error('Erro ao listar artes:', error);
    //         throw error;
    //     }
    // }

    // Função para listar minhas artes
    static async listarTodasArtes() {
        console.debug('listando artees');
        try {
            const response = await api.get('/arte/todasQueTenhoAcesso',{
                headers : {
                    Authorization : `Bearer ${Cookies.get('user_token')}`
                }
            });
            console.debug(response);
            return response.data; // Retorna diretamente o array de artes
        } catch (error) {
            console.error('Erro ao listar artes:', error);
            throw error;
        }
    }

    // Função para criar uma arte
    static async criarArte(nomeArte, accessToken) {
        console.debug('nome arte::: ', nomeArte);
        try {
            const response = await api.post(
                '/arte/criar',
                { titulo: nomeArte },
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        'Content-Type': 'application/json',
                    },
                }
            );
            return response.data; // Retorna a arte criada
        } catch (error) {
            console.error('Erro ao criar arte:', error);
            throw error;
        }
    }

    // Função para listar artes compartilhadas comigo
    static async listarArtesCompartilhadasMinhas() {
        console.debug('listando artees');
        try {
            const response = await api.get('/arte/compartilhadasMinhas',{
                headers : {
                    Authorization : `Bearer ${Cookies.get('user_token')}`
                }
            });
            console.debug(response);
            return response.data; // Retorna diretamente o array de artes
        } catch (error) {
            console.error('Erro ao listar artes:', error);
            throw error;
        }
    }

    // Função para listar minhas artes
    static async listarSomenteMinhasArtes() {
        console.debug('listando artees');
        try {
            const response = await api.get('/arte',{
                headers : {
                    Authorization : `Bearer ${Cookies.get('user_token')}`
                }
            });
            console.debug(response);
            return response.data; // Retorna diretamente o array de artes
        } catch (error) {
            console.error('Erro ao listar artes:', error);
            throw error;
        }
    }






}

export default ApiServices;
