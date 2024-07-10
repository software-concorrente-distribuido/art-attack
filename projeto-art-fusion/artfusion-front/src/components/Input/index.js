import React, { useState } from 'react';
import * as C from './styles';

const Input = ({ type, placeholder, value, onChange }) => {
    const [isFocused, setIsFocused] = useState(false);

    const handleFocus = () => {
        setIsFocused(true);
    };

    const handleBlur = () => {
        setIsFocused(false);
    };

    return (
        <C.Container>
            <C.Input
                value={value}
                onChange={onChange}
                type={type}
                placeholder=" " // Placeholder vazio para utilizar o seletor :placeholder-shown
                onFocus={handleFocus}
                onBlur={handleBlur}
            />
            <C.Label $isFocused={isFocused} $hasValue={!!value}>
                {placeholder}
            </C.Label>
        </C.Container>
    );
};

export default Input;
