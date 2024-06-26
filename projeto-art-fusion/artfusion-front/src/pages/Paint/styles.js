import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: 100vw;
`;

export const MainContent = styled.div`
    display: flex;
    flex-grow: 1;
    width: 100%;
    height: calc(100vh - 60px); /* Altura restante após a header */
    margin-top: 60px; /* Para ficar abaixo do header */
`;

export const CanvasContainer = styled.div`
    position: relative;
    flex-grow: 1;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f0f0f0;
    padding: 80px 0 0 70px; /* Adicionar o espaçamento desejado */
`;
