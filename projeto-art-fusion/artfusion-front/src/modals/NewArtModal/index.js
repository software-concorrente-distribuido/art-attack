import React, { useState, useRef } from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import Label from '../../components/Label';
import Button from '../../components/Button';
import exit from '../../assets/icons/delete-circled-outline.svg';
import ModalOverlayContainer from '../../components/Containers/ModalOverlayContainer';
import ShareModalContainer from '../../components/Containers/ShareModalContainer';
import ContainerTitleExitEditPerfil from '../../components/Containers/ContainerTitleExitEditPerfil';
import Input from '../../components/Input';
import InputPassword from '../../components/InputPassword';
import Cookies from 'js-cookie';
import ApiServices from '../../services/apiServices';

const IconExit = styled.img`
  width: 46px;
  height: 46px;
  display: flex;
  margin: 0px;
  cursor: pointer; /* Adiciona o cursor de ponteiro para indicar que é clicável */
`;

const PasswordContainer = styled.div`
  display: flex;
  width: 100%;
  margin-bottom: 20px;
  margin-top: 8px;
`;

const ConfirmarSenha = styled.div`
  margin-left: 50px;
`;

const Alinhamento = styled.div`
  display: flex;
`;

const ModalHeader = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
`;

const CreateButton = styled.button`
    margin-top: 10px;
    padding: 8px 20px;
    font-size: 16px;
    height: 50px;
    color: white;
    background-color: #0f62fe;
    border: none;
    border-radius: 5px;
    cursor: pointer;

    margin-left: 20px;
`;

function NewArtModal({ onClose }) {
    const [isClosing, setIsClosing] = useState(false);
    const [arteName, setArteName] = useState('');
    const modalRef = useRef(null);

    const handleClose = () => {
        setIsClosing(true);
        setTimeout(onClose, 300); // Tempo da animação em ms
    };

    const handleCreateArte = async () => {
        const token = Cookies.get('user_token');
        if (!arteName) {
            alert('Por favor, insira um nome para a arte.');
            return;
        }

        if (token) {
            try {
                const novaArte = await ApiServices.criarArte(arteName, token);
                if (novaArte && novaArte.id) {
                    try {
                        const salaUUID = await ApiServices.abrirSala(novaArte.id, token);
                        handleClose();
                        window.open(`/paint/${novaArte.id}/${salaUUID}`, '_blank'); // Redirecionar para a página de pintura
                    } catch (error) {
                        console.error('Erro ao abrir sala:', error);
                    }
                }
            } catch (error) {
                console.error(
                    'Erro ao criar arte:',
                    error
                );
            }
        } else {
            console.error('JWT token não encontrado.');
        }
    };


    return (
        <ModalOverlayContainer>
            <ShareModalContainer>

                <ModalHeader>
                    <Title align={'left'} size="18px">
                        <strong>Insira o  Nome da Arte</strong>
                    </Title>
                    <IconExit src={exit} alt="exit" onClick={handleClose} />
                </ModalHeader>
                <Alinhamento>
                    <Input
                        type="text"
                        placeholder="Nome da Arte"
                        value={arteName}
                        onChange={(e) => setArteName(e.target.value)}
                    />

                    <CreateButton onClick={handleCreateArte}>Criar</CreateButton>
                </Alinhamento>

            </ShareModalContainer>
        </ModalOverlayContainer>
    );
}

export default NewArtModal;
