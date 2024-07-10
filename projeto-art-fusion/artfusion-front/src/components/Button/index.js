import React from 'react';
import * as C from './styles';

const Button = ({ Text, onClick, type = 'button', disabled, width }) => {
    return (
        <C.Button
            type={type}
            onClick={onClick}
            disabled={disabled}
            width={width}
        >
            {Text}
        </C.Button>
    );
};

export default Button;
