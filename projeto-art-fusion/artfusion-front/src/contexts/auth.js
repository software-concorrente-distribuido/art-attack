import { createContext, useEffect, useState } from 'react';
import api from '../api';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';

export const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState();

    useEffect(() => {
        const token = Cookies.get('user_token');

        if (token) {
            const decodedToken = jwtDecode(token);

            const { sub: email } = jwtDecode(token);
            setUser({ email });
        }
    }, []);

    const login = async (email, password) => {
        try {
            const loginData = { email, senha: password };

            const response = await api.post('/auth/login', loginData);
            const token = response.data;

            console.log('response: ', response);

            Cookies.set('user_token', token, {
                expires: 1,
                secure: false,
                sameSite: 'Strict',
            });
            setUser({ email });
        } catch (error) {
            console.error(
                'Errooooooooooor:',
                error.response ? error.response.data : error.message
            );
            return 'E-mail ou senha incorretos';
        }
    };

    const register = async (email, name, password) => {
        try {
            console.log('API URL:', process.env.REACT_APP_ARTFUSION_API_URL);
            const response = await api.post('/usuario/criar', {
                email,
                nome: name,
                senha: password,
                isAtive: true,
            });
            console.log('response: ', response);
        } catch (error) {
            console.error(
                'Errooooooooooor:',
                error.response ? error.response.data : error.message
            );
            return 'Erro ao registrar usuário. ' + error.response
                ? error.response.data
                : error.message;
        }
    };

    const signout = () => {
        setUser(null);
        Cookies.remove('user_token');
    };

    return (
        <AuthContext.Provider
            value={{ user, signed: !!user, login, register, signout }}
        >
            {children}
        </AuthContext.Provider>
    );
};
