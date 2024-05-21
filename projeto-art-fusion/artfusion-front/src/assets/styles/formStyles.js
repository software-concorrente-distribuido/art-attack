import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    gap: 20px;
    height: 100vh;
`;

export const ImageContainer = styled.div`
    display: flex;
    align-items: center; /* Centraliza verticalmente */
    justify-content: center; /* Centraliza horizontalmente */
`;

export const Image = styled.img`
    margin-right: 10px; /* Espaço entre a imagem e o texto */
    width: 90px; /* Defina a largura desejada */
  height: auto; /* Mantém a proporção da imagem */
`;

export const TitleText = styled.label`
    font-size: 30px;
    color: #001D6C;
    font-weight: 600;
`;

export const Content = styled.div`
    gap: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    width: 100%;
    box-shadow: 0 1px 2px #0003;
    background-color: white;
    max-width: 500px;
    padding: 50px;
    border-radius: 5px;
`;

export const Label = styled.label`
    font-size: 16px;

    color: #676767;
`;

export const LabelError = styled.label`
    font-size: 14px;
    color: red;
`;

export const Strong = styled.strong`
    cursor: pointer;

    a {
        text-decoration: none;
        color: #001D6C;
    }
`;
