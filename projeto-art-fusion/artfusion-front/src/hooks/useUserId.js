import { jwtDecode } from 'jwt-decode';
import Cookies from 'js-cookie';

export function useUserId() {
    const token = Cookies.get('user_token');
    return token ? jwtDecode(token).userId : null;
}
