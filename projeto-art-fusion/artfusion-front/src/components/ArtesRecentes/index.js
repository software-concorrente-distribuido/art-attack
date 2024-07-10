import React from 'react';
import styled from 'styled-components'
import Title from '../../components/Title'
import Button from '../Button';
import TopBar from '../../components/TopBar'
import Card from '../Card'
import ContainerTitleButton from '../../components/Containers/ContainerTitleButton'

const ContainerArtesRecentes = styled.div`
    width: 81vw; 
    height: 100vh;
    margin-left: 256px;
    background-color: green; 
`

const ArtesRecentes = () => {
    return (
        <ContainerArtesRecentes>
            <ContainerTitleButton>
                <Title align={"left"}>Artes Recentes</Title>
                <Button width={"15%"}  Text={"+     Nova Arte"}></Button>
            </ContainerTitleButton>

            <TopBar></TopBar>
            <Card></Card>
            
        </ContainerArtesRecentes>
    );
};

export default ArtesRecentes;
