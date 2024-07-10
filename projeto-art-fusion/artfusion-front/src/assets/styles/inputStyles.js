import styled from 'styled-components';

export const Container = styled.div`
    position: relative;
    width: 100%;
    margin-top: 10px;
`;

export const Input = styled.input`
    outline: none;
    padding: 16px;
    width: 100%;
    font-size: 16px;
    background-color: #f2f4f8;
    border: none;
    &:focus + label,
    &:not(:placeholder-shown) + label {
        top: -10px;
        left: 10px;
        font-size: 12px;
        color: #21272a;
    }
`;

export const Label = styled.label`
    position: absolute;
    top: 50%;
    left: 20px;
    transform: translateY(-50%);
    font-size: 16px;
    color: ${(props) =>
        props.$isFocused || props.$hasValue ? '#21272A' : '#697077'};
    pointer-events: none;
    transition: all 0.2s ease;
`;
