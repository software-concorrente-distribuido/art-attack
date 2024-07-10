import React, { useState } from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import Label from '../../components/Label';
import Button from '../../components/Button';
import exit from '../../assets/icons/delete-circled-outline.svg';
import ModalOverlayContainer from '../../components/Containers/ModalOverlayContainer';
import EditProfileModalContainer from '../../components/Containers/EditProfileModalContainer';
import ContainerTitleExitEditPerfil from '../../components/Containers/ContainerTitleExitEditPerfil';
import Input from '../../components/Input';
import InputPassword from '../../components/InputPassword';

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

function EditProfileModal({ onClose }) {
  const [isEditProfileOpen, setIsEditProfileOpen] = useState(false);

  const openEditProfileModal = () => {
    setIsEditProfileOpen(true);
  };

  const closeEditProfileModal = () => {
    setIsEditProfileOpen(false);
  };

  return (
    <ModalOverlayContainer>
      <EditProfileModalContainer>
        <ContainerTitleExitEditPerfil>
          <Title align="left"><strong>Meu Perfil</strong></Title>
          <IconExit src={exit} alt="exit" onClick={onClose} /> 
        </ContainerTitleExitEditPerfil>
        
        <Label>Nome do usuário</Label>
        <Input />
        <Label>E-mail</Label>
        <Input />
        <Label>Senha Atual</Label>
        <InputPassword />

        <PasswordContainer>
          <div>
            <Label>Nova Senha</Label>
            <InputPassword />
          </div>
          <ConfirmarSenha>
            <Label>Confirmar Senha</Label>
            <InputPassword />
          </ConfirmarSenha>
        </PasswordContainer>

        <Button width={"15%"} Text="Salvar" />

      </EditProfileModalContainer>
    </ModalOverlayContainer>
  );
}

export default EditProfileModal;
