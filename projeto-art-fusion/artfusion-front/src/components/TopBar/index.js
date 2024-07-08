import React from 'react';
import styled from 'styled-components'

const Opcao = styled.li`
    font-size: 16px;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    height: 100%;
    margin-top: 20px;
    margin-right: 20px;

    padding: 5px;
    cursor: pointer;
    min-width: 120px;

    transition: color 0.9s ease; 
    border-bottom: transparent 2px solid; 

    &:hover {
        border-bottom: #003D87 2px solid; 
        color: #003D87;
    }
`

const Opcoes = styled.ul`
    display: flex;
    border-bottom: #21272A 2px solid; 
    margin: 20px;
    background-color: yellow; 
`


const TopBar = () => {
    return (
        <Opcoes>
            <Opcao>Abertas à contribuição</Opcao>
            <Opcao>Abertas à visualização</Opcao>
            <Opcao>Apenas minhas</Opcao>
        </Opcoes>
    );
};

export default TopBar;
