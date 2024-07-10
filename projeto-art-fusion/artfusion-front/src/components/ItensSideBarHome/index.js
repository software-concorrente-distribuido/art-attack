import styled from 'styled-components';
import sharesocialoutline from '../../assets/icons/share-social-outline.svg';
import colorpaletteoutline from '../../assets/icons/color-palette-outline.svg';
import brushoutline from '../../assets/icons/brush-outline.svg';
import { Link, useNavigate } from 'react-router-dom';
import LinkContainer from '../Containers/LinkContainer';

const ItenSideBar = styled.li`
    font-size: 16px;
    display: flex;
    justify-content: left;
    align-items: center;
    text-align: center;
    padding-right: 10px;
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 8px;
    cursor: pointer;
    min-width: 224px;
    font-weight: bold;

    &:hover {
        background-color: #dee0e5;
    }
`;

const ItensSideBar = styled.ul`
    padding-top: 64px;
`;

const ImagemIten = styled.img`
    margin-right: 8px;
`;

function ItensSideBarHome() {
    return (
        <ItensSideBar>
            <ItenSideBar>
                <LinkContainer>
                    <Link to="/artes-recentes">
                        <ImagemIten src={colorpaletteoutline}></ImagemIten>Artes
                        Recentes
                    </Link>
                </LinkContainer>
            </ItenSideBar>

            <ItenSideBar>
                <LinkContainer>
                    <Link to="/minhas-artes">
                        <ImagemIten src={brushoutline}></ImagemIten>Minhas Artes
                    </Link>
                </LinkContainer>
            </ItenSideBar>

            <ItenSideBar>
                <LinkContainer>
                    <Link to="/artes-compartilhadas">
                        <ImagemIten src={sharesocialoutline}></ImagemIten>
                        Compartilhadas Comigo
                    </Link>
                </LinkContainer>
            </ItenSideBar>
        </ItensSideBar>
    );
}

export default ItensSideBarHome;
