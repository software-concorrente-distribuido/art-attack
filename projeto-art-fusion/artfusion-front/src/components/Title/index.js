import styled from "styled-components"

export const Title = styled.h2`
    width: 100%;
    color: #21272A;
    font-size: ${props => props.size || '42px'};
    text-align: ${props => props.align || 'center'};
    margin: 0;
`
export default Title;