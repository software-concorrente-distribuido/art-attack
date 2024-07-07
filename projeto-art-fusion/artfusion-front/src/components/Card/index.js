import React from 'react';
import styled from 'styled-components'
import Picture from '../../assets/images/picture.png'

const CardContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 272px; 
    height: 342px;
    overflow: hidden; 

    background-color: #FFFFFF; 
    padding-bottom: 6px;
`

const TitleCard = styled.h2`
    font-size: 20px;
    color: #21272A;
    padding: 20px;
`

const TextCard = styled.h2`
    font-size: 16px;
    color: #21272A;
    font-weight: normal; 

    padding: 0 20px 5px; 
    overflow: hidden; 
    text-overflow: ellipsis; 
`

const Card = () => {
    return (

        <div>
            <CardContainer>
                <img src={Picture}></img>
                <TitleCard>Arte 1</TitleCard>
                <TextCard>Egestas elit dui scelerisque ut eu purus al scelerisque ut eu purus al scelerisque ut eu purus al scelerisque ut eu purus aliquam vitae habitasse.</TextCard>
            </CardContainer>

        </div>

    );
};

export default Card;
