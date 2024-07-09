import { jwtDecode } from 'jwt-decode';
import Cookies from 'js-cookie';

export function useUserId() {
    const token = Cookies.get('user_token');
    if (token) {
        try {
            const decoded = jwtDecode(token);
            if (decoded.usuario) {
                const usuario = JSON.parse(decoded.usuario); // Parse da string JSON para objeto
                return usuario.id; // Retorna o ID do usuário
            }
        } catch (error) {
            console.error(
                'Falha ao decodificar o token ou ao analisar o campo usuário:',
                error
            );
        }
    }
    return null;
}
