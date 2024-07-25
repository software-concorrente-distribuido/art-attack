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
            <LinkContainer>
                <Link to="/artes-recentes-contribution">
                    <ItenSideBar>
                        <ImagemIten src={colorpaletteoutline}></ImagemIten>Artes Recentes
                    </ItenSideBar>
                </Link>
            </LinkContainer>

            <LinkContainer>
                <Link to="/minhas-artes">
                    <ItenSideBar>
                        <ImagemIten src={brushoutline}></ImagemIten>Minhas Artes
                    </ItenSideBar>
                </Link>
            </LinkContainer>


            <LinkContainer>
                <Link to="/artes-compartilhadas">
                    <ItenSideBar>
                        <ImagemIten src={sharesocialoutline}></ImagemIten>Compartilhadas Comigo
                    </ItenSideBar>
                </Link>
            </LinkContainer>
        </ItensSideBar>
    );
}

export default ItensSideBarHome;
