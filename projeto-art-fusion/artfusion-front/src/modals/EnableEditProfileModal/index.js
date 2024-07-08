import React from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import Label from '../../components/Label';
import user from '../../assets/images/man16_117721.svg';
import Button from '../../components/Button';
import exit from '../../assets/icons/delete-circled-outline.svg';
import ModalOverlayContainer from '../../components/Containers/ModalOverlayContainer'
import EditProfileModalContainer from '../../components/Containers/EditProfileModalContainer'
import ContainerTitleExitEditPerfil from '../../components/Containers/ContainerTitleExitEditPerfil'

const ImageUser = styled.img`
  width: 120px;
  height: 120px;
  display: flex;
  margin: 0px;
  margin-top: 16px;
  margin-bottom: 16px;
`;

const IconExit = styled.img`
  width: 46px;
  height: 46px;
  display: flex;
  margin: 0px;
`;

const LabelDados = styled.p`
  display: flex;
  width: 100%;
  margin-left: 20px;
  margin-bottom: 20px;
`;


function EnableEditProfileModal() {
  return (
    <ModalOverlayContainer>
      <EditProfileModalContainer>
        <ContainerTitleExitEditPerfil>

            <Title
                align={"left"}
            ><strong>Meu Perfil</strong>
            </Title>

            <IconExit src={exit} alt="exit" />
        </ContainerTitleExitEditPerfil>
        
        <ImageUser src={user} alt="user" />

        <Label>Nome:</Label>
          <LabelDados>João Silva</LabelDados>
        <Label>E-mail:</Label>
          <LabelDados>joaodasilva@email.com</LabelDados>

        <Button Text="Editar"/>
      </EditProfileModalContainer>
    </ModalOverlayContainer>
  );
}

export default EnableEditProfileModal;
