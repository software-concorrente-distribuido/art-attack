import React from 'react';
import ItensSideBarHome from '../../components/ItensSideBarHome'
import LogoSideBarHome from '../../components/logoSideBarHome'
import PerfilSideBarHome from '../../components/PerfilSideBarHome'
import styled from 'styled-components'

const SideBarContainer = styled.div`
    position: fixed; 
    top: 0;
    left: 0;

    width: 256px; 
    height: 100vh;
    display: flex;
    flex-direction: column;

    justify-content: flex-start; 
    align-items: flex-start; 
    background-color: #FFFFFF; 
    padding-left: 16px; 
`

const SideBar = () => {
    return (
        <SideBarContainer>
            <LogoSideBarHome></LogoSideBarHome>
            <ItensSideBarHome></ItensSideBarHome>
            <PerfilSideBarHome></PerfilSideBarHome>
        </SideBarContainer>
    );
};

export default SideBar;
