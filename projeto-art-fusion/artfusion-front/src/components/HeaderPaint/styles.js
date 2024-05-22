import styled from 'styled-components';

export const HeaderContainer = styled.header`
    width: 100%;
    height: 60px;
    background-color: #343A3F;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1000;
`;

export const LeftSection = styled.div`
    display: flex;
    align-items: center;
`;

export const RightSection = styled.div`
    display: flex;
    align-items: center;
`;

export const Logo = styled.img`
    height: 40px;
    margin-right: 10px;
`;

export const Title = styled.h1`
    //color: #21272a;
    color: white;
    font-size: 24px;
    margin: 0 10px;
    font-weight: 600;
    cursor: pointer;
    display: inline-block;
    line-height: 1.5;
    white-space: nowrap; /* Impede a quebra de linha */
    min-width: 0;
`;

export const TitleInput = styled.input`
    //color: #21272a;
    color: white;
    font-size: 24px;
    background: none;
    border: none;
    outline: none;
    margin: 0 10px;
    display: inline-block;
    line-height: 1.5;
    box-sizing: border-box;
    padding: 0;
    white-space: nowrap; /* Impede a quebra de linha */
    min-width: 0;
    &:focus {
        border-bottom: 1px solid white;
    }
`;

export const IconButton = styled.button`
    background: none;
    border: none;
    //color: #21272a;
    color: white;
    font-size: 18px;
    cursor: pointer;
    margin: 0 5px;

    &:hover {
        color: #697077;
    }
`;

export const SaveStatus = styled.div`
    color: #21272a;
    margin-left: 10px;
    font-size: 20px;
`;

export const UserAvatar = styled.div`
    width: 30px;
    height: 30px;
    border-radius: 50%;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20px;
`;

export const ShareButton = styled.button`
    background-color: #0f62fe;
    color: white;
    border: none;
    border-radius: 5px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 16px;

    &:hover {
        background-color: #ddd;
    }
`;
