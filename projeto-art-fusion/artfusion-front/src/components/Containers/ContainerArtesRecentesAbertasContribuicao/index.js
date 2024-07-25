import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ApiServices from '../../../services/apiServices'
import Cookies from 'js-cookie';
import styled from 'styled-components';
import Picture from '../../../assets/images/picture.png'

const Container = styled.div`
    padding: 20px;
`;

const Title = styled.h1`
    text-align: center;
    color: #333;
`;

const ArtList = styled.ul`
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    list-style: none;
    width: 1060px;
`;

const CardContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 250px; 
    height: 342px;
    overflow: hidden; 
    background-color: #FFFFFF; 
    padding-bottom: 6px;
    cursor: pointer;
`

const TitleCard = styled.h2`
    font-size: 20px;
    color: #21272A;
    padding: 20px;
`

const TextCard = styled.h2`
    font-size: 16px;
    color: #21272A;
    font-weight: normal; 
    padding: 0 20px 5px; 
    overflow: hidden; 
    text-overflow: ellipsis; 
`

const ContainerArtesRecentesAbertasContribuicao = () => {
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

    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        return new Date(dateString).toLocaleDateString('pt-BR', options);
    };

    return (
        <Container>
            {loading ? (
                <p>Carregando...</p>
            ) : (
                <ArtList>
                    {artes.map((arte) => (
                        <CardContainer
                            key={arte.id}
                            onClick={() => handleSelectArte(arte.id)}
                        >
                            <img src={Picture} alt="Arte" />
                            <TitleCard>{arte.titulo}</TitleCard>
                            <TextCard>Data de criação: {formatDate(arte.dataCriacao)}</TextCard>
                            <TextCard>ID adm: {arte.administrador.id}</TextCard>
                        </CardContainer>
                    ))}
                </ArtList>
            )}
        </Container>
    );
};

export default ContainerArtesRecentesAbertasContribuicao;
