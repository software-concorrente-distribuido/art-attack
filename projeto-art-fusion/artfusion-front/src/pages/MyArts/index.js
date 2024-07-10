import React from 'react';
import SideBar from '../../components/SideBar'
import styled from "styled-components"

import Title from '../../components/Title'
import Button from '../../components/Button';
import TopBar from '../../components/TopBar'
import Card from '../../components/Card'

import ContainerTitleButton from '../../components/Containers/ContainerTitleButton'

const ConteinerArtesRecentesSideBar = styled.div`
    display: flex;
`

const ContainerArtesRecentes = styled.div`
    width: 81vw; 
    height: 100vh;
    margin-left: 256px;
`

const MyArts = () => {
    return (
        <div>
            <ConteinerArtesRecentesSideBar>
                <ContainerArtesRecentes>
                    <ContainerTitleButton>
                        <Title align={"left"}>Minhas Artes</Title>
                        <Button width={"15%"} Text={"+     Nova Arte"}></Button>
                    </ContainerTitleButton>

                    <TopBar></TopBar>
                    <Card></Card>

                </ContainerArtesRecentes>
                <SideBar></SideBar>
            </ConteinerArtesRecentesSideBar>
        </div>
    );
};

export default MyArts;
