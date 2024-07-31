import React, { useState } from 'react';
import SideBar from '../../components/SideBar';
import styled from 'styled-components';
import Title from '../../components/Title';
import Button from '../../components/Button';
import ContainerMinhasArtes from '../../components/Containers/ContainerMinhasArtes';
import ContainerTitleButton from '../../components/Containers/ContainerTitleButton';
import NewArtModal from '../../modals/NewArtModal';

const ConteinerArtesRecentesSideBar = styled.div`
  display: flex;
`;

const ContainerArtesRecentes = styled.div`
  width: 81vw; 
  height: 100vh;
  margin-left: 256px;
`;

const MyArts = () => {
  const [isNewArtModalOpen, setNewArtModalOpen] = useState(false);

  return (
    <div>
      <ConteinerArtesRecentesSideBar>
        <ContainerArtesRecentes>
          <ContainerTitleButton>
            <Title align={"left"}>Minhas Artes</Title>
            <Button
              width={"15%"}
              Text={"+ Nova Arte"}
              onClick={() => setNewArtModalOpen(true)}
            />
          </ContainerTitleButton>
          <ContainerMinhasArtes />
        </ContainerArtesRecentes>
        <SideBar />
      </ConteinerArtesRecentesSideBar>

      {isNewArtModalOpen && (
        <NewArtModal
          onClose={() => setNewArtModalOpen(false)}
        />
      )}
    </div>
  );
};

export default MyArts;
