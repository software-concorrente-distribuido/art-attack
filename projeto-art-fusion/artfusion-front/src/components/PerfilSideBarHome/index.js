import user from '../../assets/images/man16_117721.svg';
import styled from 'styled-components'

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
`
const ImageUser = styled.img`
    width: 32px;
    height: 32px;
    display: flex;
    margin-right: 8px;
`

function PerfilSideBarHome () {
    return(
        <PerfilSideBarHomeContainer>
            <ImageUser
                src={user}
                alt='user'
            ></ImageUser>
            <p>Nome usuário</p>
        </PerfilSideBarHomeContainer>
    )
}

export default PerfilSideBarHome