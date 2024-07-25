import React from 'react';
import styled from 'styled-components';
import { Link, useNavigate } from 'react-router-dom';

const UnstyledLink = styled(Link)`
    text-decoration: none;
    color: inherit;
`;

const Opcao = styled.li`
    font-size: 16px;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    height: 100%;

    margin-right: 20px;
    font-weight: bold;
    padding: 5px;
    cursor: pointer;
    min-width: 120px;
    transition: color 0.9s ease; 
    border-bottom: transparent 2px solid; 

    &:hover {
        border-bottom: #003D87 2px solid; 
        color: #003D87;
    }
`;

const Opcoes = styled.ul`
    display: flex;
    border-bottom: 1px solid rgba(221, 225, 230);
    margin: 20px;
`;

const TopBar = () => {
    return (
        <Opcoes>
            <UnstyledLink to="/artes-recentes-contribution">
                <Opcao>Abertas à contribuição</Opcao>
            </UnstyledLink>

            <UnstyledLink to="/artes-recentes-preview">
                <Opcao>Abertas à visualização</Opcao>
            </UnstyledLink>
        </Opcoes>
    );
};

export default TopBar;
