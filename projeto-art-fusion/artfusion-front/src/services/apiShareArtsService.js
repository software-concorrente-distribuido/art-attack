import api from '../api';
import Cookies from "js-cookie";

class ApiShareArtsService {
    // Função para compartilhar uma arte
    static async compartilharArte(arteId, usuarioBeneficiadoEmail) {
        console.debug('compartilharArte');
        const response = await api.post(
            '/arte/compartilharPorEmail',
            {
                arteId: arteId,
                usuarioBeneficiadoEmail: usuarioBeneficiadoEmail,
                permissoes: ["VISUALIZAR", "EDITAR", "EXCLUIR"]
            },
            {
                headers: {
                    Authorization: `Bearer ${Cookies.get('user_token')}`,
                    'Content-Type': 'application/json'
                }
            }
        );
        try {
            return response.data; 
        } catch (error) {
            console.log(response.data);
            console.error('Erro ao compartilhar arte, tente novamente!', error);
            throw error;
        }
    }
}

export default ApiShareArtsService;
