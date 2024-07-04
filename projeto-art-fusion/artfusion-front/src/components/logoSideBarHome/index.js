import logo from '../../assets/images/logo.png';
import styled from 'styled-components'

const LogoContainer = styled.div`
    display: flex;
    font-size: 24px;
    color: #001D6C; 
    padding-top: 24px;
    
`
const LogoImage = styled.img`
    margin-right: 10px;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: flex-start;
`

function LogoSideBarHome () {
    return(
        <LogoContainer>
            <LogoImage
                src={logo}
                alt='logo'
            ></LogoImage>
            <p><strong>Art Fusion</strong></p>
        </LogoContainer>
    )
}

export default LogoSideBarHome