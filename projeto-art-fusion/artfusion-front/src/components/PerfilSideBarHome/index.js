import React, { useState } from 'react';
import styled from 'styled-components';
import user from '../../assets/images/man16_117721.svg';
import EditProfileModal from '../../modals/EditProfileModal'; 
import EnableEditProfileModal from '../../modals/EnableEditProfileModal'; 

const PageContainer = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
`;

const PerfilSideBarHomeContainer = styled.div`
    display: flex;
    font-size: 16px;
    color: #21272A; 
    justify-content: center;
    align-items: center;
    text-align: center;
    margin-top: auto;
    margin-bottom: 36px;
    cursor: pointer;
`;

const ImageUser = styled.img`
    width: 32px;
    height: 32px;
    display: flex;
    margin-right: 8px;
`;

function PerfilSideBarHome() {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <PageContainer>
            <PerfilSideBarHomeContainer onClick={openModal}>
                <ImageUser src={user} alt='user' />
                <p>Nome usuário</p>
            </PerfilSideBarHomeContainer>

            {isModalOpen && (
                <EnableEditProfileModal onClose={closeModal} /> 
            )}
        </PageContainer>
    );
}

export default PerfilSideBarHome;
