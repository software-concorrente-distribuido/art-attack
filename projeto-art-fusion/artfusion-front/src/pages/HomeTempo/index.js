import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ApiServices from '../../services/apiServices';
import Cookies from 'js-cookie';
import styled from 'styled-components';

const Container = styled.div`
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
`;

const Title = styled.h1`
    text-align: center;
    color: #333;
`;

const ArtList = styled.ul`
    list-style: none;
    padding: 0;
`;

const ArtItem = styled.li`
    padding: 10px;
    border-bottom: 1px solid #ccc;
    cursor: pointer;
    &:hover {
        background-color: #f0f0f0;
    }
`;

const Input = styled.input`
    width: 100%;
    padding: 10px;
    margin: 10px 0;
    box-sizing: border-box;
`;

const Button = styled.button`
    width: 100%;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    &:hover {
        background-color: #0056b3;
    }
`;

const Home = () => {
    const [arteName, setArteName] = useState('');
    const [artes, setArtes] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        ApiServices.listarTodasArtes()
            .then(setArtes)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    const handleCreateArte = async () => {
        const token = Cookies.get('user_token');
        if (!arteName) {
            alert('Por favor, insira um nome para a arte.');
            return;
        }

        if (token) {
            console.debug('Token OK, entrando em criar arte');
            try {
                const novaArte = await ApiServices.criarArte(arteName, token);
                console.log('Arte criada com sucesso:', novaArte);
                setArtes([...artes, novaArte]);
                setArteName('');
                console.log('Arte criada com sucesso:', novaArte.id);
                // Continua apenas se tiver o ID da nova arte
                if (novaArte && novaArte.id) {
                    handleSelectArte(novaArte.id);
                }
            } catch (error) {
                console.error(
                    'Erro ao criar arteeeeeeeeeeeeeeeeeeeeeeeeee:',
                    error
                );
            }
        } else {
            console.error('JWT token não encontrado.');
        }
    };

    const handleSelectArte = async (arteId) => {
        const token = Cookies.get('user_token');

        if (!arteId) {
            console.error('Arte não encontrada');
            return;
        }

        if (token) {
            try {
                const salaUUID = await ApiServices.abrirSala(arteId, token);
                window.open(`/paint/${arteId}/${salaUUID}`, '_blank'); // Redirecionar para a página de pintura
            } catch (error) {
                console.error('Erro ao abrir sala:', error);
            }
        } else {
            console.error('JWT token não encontrado.');
        }
    };

    return (
        <Container>
            <Title>Gerenciador de Artes</Title>
            <Input
                type="text"
                value={arteName}
                onChange={(e) => setArteName(e.target.value)}
                placeholder="Nome da Arte"
            />
            <Button onClick={handleCreateArte}>Criar Arte</Button>
            <h2>Lista de Artes</h2>
            {loading ? (
                <p>Carregando...</p>
            ) : (
                <ArtList>
                    {artes.map((arte) => (
                        <ArtItem
                            key={arte.id}
                            onClick={() => handleSelectArte(arte.id)}
                        >
                            {arte.titulo}
                        </ArtItem>
                    ))}
                </ArtList>
            )}
        </Container>
    );
};

export default Home;
