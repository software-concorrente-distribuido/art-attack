import React from 'react';
import styled from 'styled-components';
import Title from '../../components/Title';
import user from '../../assets/images/man16_117721.svg';
import exit from '../../assets/icons/delete-circled-outline.svg';
import ModalOverlayContainer from '../../components/Containers/ModalOverlayContainer'
import ContainerTitleExitEditPerfil from '../../components/Containers/ContainerTitleExitEditPerfil'
import EditProfileModalContainer from '../../components/Containers/EditProfileModalContainer'
import GeneralAccess from '../../components/GeneralAccess';
import NomeEmailImgContainer from '../../components/Containers/NomeEmailImgContainer'

const IconExit = styled.img`
  width: 46px;
  height: 46px;
  display: flex;
  margin: 0px;
`;

const Nome = styled.label`
  max-width: 500px;
  font-size: 16px; 
  font-weight: bold;
  color: #21272A;
  display: flex;
`;

const Permissão = styled.label`
  font-size: 16px; 
  font-weight: bold;
  color: #21272A;
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

  border: 1px #DDE1E6 solid;
  padding: 8px;
`;

const TableUser = styled.ul`
  margin-top: 16px;
  margin-bottom: 16px;
`;

const AddParticipant = styled.input`
  margin-top: 16px;
  margin-bottom: 16px;
  background-color: #F2F4F8;
  border-radius: 5px;
 

  width: 670px;
  height: 48px;

  padding-left: 20px; 

  &::placeholder {
    color: #697077; 
  }

`;

function ShareModal() {
  return (
    <ModalOverlayContainer>
      <EditProfileModalContainer>
        <ContainerTitleExitEditPerfil>

          <Title
            align={"left"}
          ><strong>Compartilhar Nome da Arte Nome da Arte Nome da Arte</strong>
          </Title>

          <IconExit src={exit} alt="exit" />
        </ContainerTitleExitEditPerfil>

        <AddParticipant type="email" id="email" name="email" placeholder="Adicione participantes"></AddParticipant>

        <Title size="20px" align="left"> Pessoas com acesso </Title>

        <TableUser>
          <RowTableUser>
            <NomeEmailImgContainer>
              <img src={user} alt='user' />
              <NomeEmailContainer>
                <Nome>Jane Doe</Nome>
                <Email>fulano@email.com</Email>
              </NomeEmailContainer>
            </NomeEmailImgContainer>

            <PermissaoContainer>
              <Permissão>Editor</Permissão>
              <Reticencias>...</Reticencias>
            </PermissaoContainer>
          </RowTableUser>
        </TableUser>



        <Title size="20px" align="left"> Acesso geral </Title>

        <GeneralAccess></GeneralAccess>

      </EditProfileModalContainer>
    </ModalOverlayContainer>
  );
}

export default ShareModal;
