import React, { useState } from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import * as C from './styles';

const InputPassword = ({ placeholder, value, onChange }) => {
    const [isFocused, setIsFocused] = useState(false);

    const [showPassword, setShowPassword] = useState(false);

    const toggleShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleFocus = () => {
        setIsFocused(true);
    };

    const handleBlur = () => {
        setIsFocused(false);
    };

    return (
        <C.Container>
            <C.Input
                type={showPassword ? 'text' : 'password'}
                placeholder=" "
                value={value}
                onChange={onChange}
                onFocus={handleFocus}
                onBlur={handleBlur}
            />
            <C.Label $isFocused={isFocused} $hasValue={!!value}>
                {placeholder}
            </C.Label>
            <C.Icon onClick={toggleShowPassword}>
                {showPassword ? <FaEyeSlash /> : <FaEye />}
            </C.Icon>
        </C.Container>
    );
};

export default InputPassword;
