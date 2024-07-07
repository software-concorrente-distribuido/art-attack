import React from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import Label from '../../components/Label';
import Button from '../../components/Button';
import exit from '../../assets/icons/delete-circled-outline.svg';
import ModalOverlayContainer from '../../components/Containers/ModalOverlayContainer'
import EditProfileModalContainer from '../../components/Containers/EditProfileModalContainer'
import ContainerTitleExitEditPerfil from '../../components/Containers/ContainerTitleExitEditPerfil'
import Input from '../../components/Input';
import InputPassword from '../../components/InputPassword';

const IconExit = styled.img`
  width: 46px;
  height: 46px;
  display: flex;
  margin: 0px;
`;

const PasswordContainer = styled.div`
  display: flex;
  width: 100%;
  margin-bottom: 20px;
`;

const ConfirmarSenha = styled.div`
  margin-left: 50px;
`;


function EditProfileModal() {
  return (
    <ModalOverlayContainer>
      <EditProfileModalContainer>
        <ContainerTitleExitEditPerfil>

            <Title
                align={"left"}
            ><strong>Meu Perfil</strong>
            </Title>

            <IconExit src={exit} alt="exit" />
        </ContainerTitleExitEditPerfil> <br></br>

        <Label>Nome do usuário</Label>
          <Input></Input> <br></br>
        <Label>E-mail</Label>
          <Input></Input> <br></br>
        <Label>Senha Atual</Label>
          <InputPassword></InputPassword> <br></br>

        <PasswordContainer>
          <div>
            <Label>Nova Senha</Label>
            <InputPassword></InputPassword> <br></br>
          </div>
          <ConfirmarSenha>
            <Label>Confirmar Senha</Label>
            <InputPassword></InputPassword> <br></br>
          </ConfirmarSenha>
        </PasswordContainer>
        

        <Button Text="Salvar"/>
      </EditProfileModalContainer>
    </ModalOverlayContainer>
  );
}

export default EditProfileModal;
