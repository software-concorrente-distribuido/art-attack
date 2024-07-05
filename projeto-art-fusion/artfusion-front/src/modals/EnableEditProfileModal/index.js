import React from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import Label from '../../components/Label';
import user from '../../assets/images/man16_117721.svg';
import Button from '../../components/Button';
import exit from '../../assets/icons/delete-circled-outline.svg';

const ImageUser = styled.img`
  width: 120px;
  height: 120px;
  display: flex;
  margin: 0px;
  margin-top: 16px;
  margin-bottom: 16px;
`;

const IconEdit = styled.img`
  width: 46px;
  height: 46px;
  display: flex;
  margin: 0px;
`;

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
`;

const EnableEditProfileModalContainer = styled.div`
  max-width: 714px;
  max-height: 648px;
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  padding: 15px;
  background-color: #FFFFFF;
  border-radius: 8px;
`;

const LayoutTitleExit = styled.section`
  display: flex;
  flex-direction: row;
  width: 100%;
`;

function EnableEditProfileModal() {
  return (
    <ModalOverlay>
      <EnableEditProfileModalContainer>
        <LayoutTitleExit>

            <Title
                align={"left"}
            ><strong>Meu Perfil</strong>
            </Title>

            <IconEdit src={exit} alt="exit" />
        </LayoutTitleExit>
        
        <ImageUser src={user} alt="user" />

        <Label>Nome:</Label>
        <p>João Silva</p>
        <Label>E-mail:</Label>
        <p>joaodasilva@email.com</p>

        <Button Text="Editar"/>
      </EnableEditProfileModalContainer>
    </ModalOverlay>
  );
}

export default EnableEditProfileModal;
