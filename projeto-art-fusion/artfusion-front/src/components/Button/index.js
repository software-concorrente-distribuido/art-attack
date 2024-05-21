import React from 'react';
import * as C from './styles';

const Button = ({ Text, onClick, type = 'button', disabled }) => {
    return (
        <C.Button type={type} onClick={onClick} disabled={disabled}>
            {Text}
        </C.Button>
    );
};

export default Button;
