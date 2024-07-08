import api from '../api';

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
}

export default ApiServices;
