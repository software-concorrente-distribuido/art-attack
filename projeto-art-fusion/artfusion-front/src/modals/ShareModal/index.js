import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import user from '../../assets/images/man16_117721.svg';
import exit from '../../assets/icons/delete-circled-outline.svg';
import ModalOverlayContainer from '../../components/Containers/ModalOverlayContainer';
import ContainerTitleExitEditPerfil from '../../components/Containers/ContainerTitleExitEditPerfil';
import EditProfileModalContainer from '../../components/Containers/EditProfileModalContainer';
import NomeEmailImgContainer from '../../components/Containers/NomeEmailImgContainer';

const IconExit = styled.img`
    width: 30px;
    height: 30px;
    cursor: pointer;
`;

const Nome = styled.label`
    max-width: 500px;
    font-size: 16px;
    font-weight: bold;
    color: #21272a;
    display: flex;
`;

const Permissao = styled.label`
    font-size: 16px;
    font-weight: bold;
    color: #21272a;
    display: flex;
    padding: 10px;
    align-items: center;
`;

const Reticencias = styled.label`
    font-size: 30px;
    font-weight: bold;
    color: #697077;
    display: flex;
    padding-bottom: 15px;
    padding-left: 12px;
    padding-right: 12px;
    cursor: pointer;
    transition-duration: 0.4s;
`;

const Email = styled.label`
    max-width: 500px;
    font-size: 14px;
    color: #697077;
    display: flex;
`;

const NomeEmailContainer = styled.label`
    margin-left: 8px;
    display: flex;
    flex-direction: column;
    justify-content: center;
`;

const PermissaoContainer = styled.label`
    display: flex;
    flex-direction: row;
`;

const RowTableUser = styled.li`
    text-decoration: none;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    border: 1px #dde1e6 solid;
    padding: 8px;
    margin-bottom: 10px; /* Espaço entre os elementos */
`;

const TableUser = styled.ul`
    margin-top: 16px;
    margin-bottom: 24px; /* Espaço abaixo da lista */
`;

const Dropdown = styled.select`
    margin-top: 16px;
    width: 100%;
    padding: 8px;
    font-size: 16px;
    border: 1px solid #dde1e6;
    border-radius: 5px;
`;

const SaveButton = styled.button`
    margin-top: 16px;
    padding: 8px 16px;
    font-size: 16px;
    height: 40px;
    color: white;
    background-color: #0f62fe;
    border: none;
    border-radius: 5px;
    cursor: pointer;
`;

const ModalHeader = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px; /* Espaço abaixo do cabeçalho */
`;

const SectionTitle = styled(Title)`
    padding-top: 10px; /* Espaço abaixo dos títulos das seções */
`;

function ShareModal({ onClose }) {
    const [generalAccess, setGeneralAccess] = useState('only-added');
    const [isClosing, setIsClosing] = useState(false);
    const modalRef = useRef(null);

    const handleAccessChange = (e) => {
        setGeneralAccess(e.target.value);
    };

    const handleSave = () => {
        console.log('Salvar alterações:', generalAccess);
        handleClose();
    };

    const handleClose = () => {
        setIsClosing(true);
        setTimeout(onClose, 300); // Tempo da animação em ms
    };

    const handleOverlayClick = (event) => {
        if (modalRef.current && !modalRef.current.contains(event.target)) {
            handleClose();
        }
    };

    return (
        <ModalOverlayContainer
            onClick={handleOverlayClick}
            isClosing={isClosing}
        >
            <EditProfileModalContainer
                ref={modalRef}
                onClick={(e) => e.stopPropagation()}
            >
                <ModalHeader>
                    <Title align={'left'} size="26px">
                        <strong>Compartilhar Nome da Arte</strong>
                    </Title>
                    <IconExit src={exit} alt="exit" onClick={handleClose} />
                </ModalHeader>

                <SectionTitle size="20px" align="left">
                    Pessoas com acesso
                </SectionTitle>

                <TableUser>
                    <RowTableUser>
                        <NomeEmailImgContainer>
                            <img src={user} alt="user" />
                            <NomeEmailContainer>
                                <Nome>Jane Doe</Nome>
                                <Email>fulano@email.com</Email>
                            </NomeEmailContainer>
                        </NomeEmailImgContainer>

                        <PermissaoContainer>
                            <Permissao>Editor</Permissao>
                            <Reticencias>...</Reticencias>
                        </PermissaoContainer>
                    </RowTableUser>
                </TableUser>

                <SectionTitle size="20px" align="left">
                    Colaboração
                </SectionTitle>

                <Dropdown value={generalAccess} onChange={handleAccessChange}>
                    <option value="only-added">Apenas você pode acessar</option>
                    <option value="link-edit">
                        Apenas pessoas adicionadas
                    </option>
                    <option value="platform-edit">
                        Todos da plataforma podem editar
                    </option>
                    <option value="platform-view">
                        Todos da plataforma podem visualizar
                    </option>
                </Dropdown>

                <SaveButton onClick={handleSave}>Salvar</SaveButton>
            </EditProfileModalContainer>
        </ModalOverlayContainer>
    );
}

export default ShareModal;
