import React from 'react';
import * as C from './styles';
import SideBar from '../../components/SideBar'
import TopBar from '../../components/TopBar'
import ArtesRecentes from '../../components/ArtesRecentes'
import EditProfileModal from '../../modals/EditProfileModal'
import styled from "styled-components"
import ShareModal from '../../modals/ShareModal'

const ConteinerArtesRecentesSideBar = styled.div`
    display: flex;
`

const Home = () => {
    return (
        <C.Container>
            <ShareModal></ShareModal>
        </C.Container>
    );
};

export default Home;
