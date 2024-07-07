import React from 'react';
import * as C from './styles';
import SideBar from '../../components/SideBar'
import TopBar from '../../components/TopBar'
import ArtesRecentes from '../../components/ArtesRecentes'
import EnableEditProfileModal from '../../modals/EnableEditProfileModal'
import styled from "styled-components"

/* <EnableEditProfileModal></EnableEditProfileModal> */

const ConteinerArtesRecentesSideBar = styled.div`
    display: flex;
`

const Home = () => {
    return (
        <C.Container>
            <ConteinerArtesRecentesSideBar>
                <ArtesRecentes></ArtesRecentes>
                <SideBar></SideBar>
            </ConteinerArtesRecentesSideBar>

            
            
            
            
        </C.Container>
    );
};

export default Home;
