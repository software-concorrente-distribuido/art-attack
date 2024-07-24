import React, { useState } from 'react';
import Input from '../../components/Input';
import Button from '../../components/Button';
import InputPassword from '../../components/InputPassword';
import * as C from './styles';
import { Link, useNavigate } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import logo from '../../assets/images/logo.png';

const Login = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async () => {
        if (!email | !password) {
            setError('Preencha todos os campos');
            return;
        }

        const res = await login(email, password);

        if (res) {
            setError(res);
            return;
        }

        navigate('/home');

    };

    const isFormValid = email && password;

    return (
        <C.Container>
            <C.Content>
                <C.ImageContainer>
                    <C.Image src={logo} alt="ArtFusion" />
                    <C.TitleText>ArtFusion</C.TitleText>
                </C.ImageContainer>
                <C.Label style={{ paddingBottom: '20px' }}>
                    Por favor, faça login para continuar.
                </C.Label>

                <Input
                    type="email"
                    placeholder={'E-mail'}
                    value={email}
                    onChange={(e) => [setEmail(e.target.value), setError('')]}
                />
                <InputPassword
                    //type="password"
                    placeholder={'Senha'}
                    value={password}
                    onChange={(e) => [
                        setPassword(e.target.value),
                        setError(''),
                    ]}
                />
                <C.LabelError>{error}</C.LabelError>
                <Button
                    Text="Entrar"
                    onClick={handleLogin}
                    disabled={!isFormValid}
                />
                <C.Label style={{ paddingTop: '20px' }}>
                    Não tem uma conta?
                    <C.Strong>
                        <Link to="/register">&nbsp;Registre-se</Link>
                    </C.Strong>
                </C.Label>
            </C.Content>
        </C.Container>
    );
};

export default Login;
