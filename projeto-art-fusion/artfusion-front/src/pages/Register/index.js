import React, { useState } from 'react';
import Input from '../../components/Input';
import Button from '../../components/Button';
import InputPassword from '../../components/InputPassword';
import * as C from './styles';
import { Link, useNavigate } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import logo from '../../assets/images/logo.png';

const Register = () => {
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirmation, setPasswordConfirmation] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const { register } = useAuth();

    const handleRegister = async () => {
        if (!email | !name | !password | !passwordConfirmation) {
            setError('Preencha todos os campos');
            return;
        } else if (password !== passwordConfirmation) {
            setError('As senhas informadas não são iguais');
            return;
        }

        const res = await register(email, name, password);

        if (res) {
            setError(res);
            return;
        }

        alert('Usuário cadatrado com sucesso!');
        navigate('/');
    };

    const isFormValid = email && password && passwordConfirmation;

    return (
        <C.Container>
            <C.Content>
            <C.ImageContainer>
                <C.Image src={logo} alt="ArtFusion" />
                <C.TitleText>ArtFusion</C.TitleText>
            </C.ImageContainer>
            <C.Label style={{ paddingBottom : '10px' }}>
                Informe os dados para cadastrar.
            </C.Label>
            <Input
                    placeholder="Nome"
                    value={name}
                    onChange={(e) => [setName(e.target.value), setError('')]}
                />
                <Input
                    type="email"
                    placeholder="E-mail"
                    value={email}
                    onChange={(e) => [setEmail(e.target.value), setError('')]}
                />
                <InputPassword
                    type="password"
                    placeholder="Senha"
                    value={password}
                    onChange={(e) => [
                        setPassword(e.target.value),
                        setError(''),
                    ]}
                />
                <InputPassword
                    type="password"
                    placeholder="Confirme sua Senha"
                    value={passwordConfirmation}
                    onChange={(e) => [
                        setPasswordConfirmation(e.target.value),
                        setError(''),
                    ]}
                />
                <C.LabelError>{error}</C.LabelError>
                <Button Text="Inscrever-se" onClick={handleRegister} disabled={!isFormValid} />
                <C.Label style={{paddingTop : '20px'}}>
                    Já tem uma conta?
                    <C.Strong>
                        <Link to="/">&nbsp;Entre</Link>
                    </C.Strong>
                </C.Label>
            </C.Content>
        </C.Container>
    );
};

export default Register;
