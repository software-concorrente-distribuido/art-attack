import React, { useState } from 'react';
import { FaPencilAlt, FaUndo, FaRedo, FaSave } from 'react-icons/fa';
import * as S from './styles';
import logo from '../../assets/images/logo.png';
import ShareModal from '../../modals/ShareModal/';
import apiServices from "../../services/apiServices";

const Header = ({ onUndo, onRedo, title,setTitle ,editarTitulo, arteId}) => {
    const [isEditing, setIsEditing] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const maxLength = 45;

    const handleTitleEdit = () => {
        setIsEditing(true);
    };

    const handleTitleChange = async (e) => {
        setTitle(e.target.value);
    };

    const handleShareClick = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    const handleTitleBlur = async () => {
        if (title.trim() === '') {
            setTitle('Obra sem título');
        }
        await editarTitulo(title,arteId)

        setIsEditing(false);
    };


    return (
        <>
            <S.HeaderContainer>
                <S.LeftSection>
                    <S.Logo src={logo} alt="Logo" />
                    {isEditing ? (
                        <S.TitleInput
                            type="text"
                            value={title}
                            onChange={handleTitleChange}
                            onBlur={handleTitleBlur}
                            autoFocus
                            maxLength={maxLength}
                        />
                    ) : (
                        <S.Title onClick={handleTitleEdit}>{title}</S.Title>
                    )}
                    <S.IconButton onClick={handleTitleEdit}>
                        <FaPencilAlt />
                    </S.IconButton>
                </S.LeftSection>
                <S.RightSection>
                    {/* <S.UserAvatar>K</S.UserAvatar> */}

                    <S.ShareButton onClick={handleShareClick}>
                        Compartilhar
                    </S.ShareButton>
                </S.RightSection>
            </S.HeaderContainer>
            {isModalOpen && <ShareModal onClose={handleCloseModal} />}
        </>
    );
};

export default Header;
