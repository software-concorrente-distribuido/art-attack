import React from 'react';
import styled from 'styled-components'
import Label from '../Label'

const RadioChecklist = styled.li`
    list-style-type: none;
    margin-top: 4px;
`

const RadioChecklistContainer = styled.ul`
    margin-top: 16px;
`

const InputRadio = styled.input`
    display: none;

    & + label {
        position: relative;
        padding-left: 28px; 
        cursor: pointer;
    }

    & + label::before {
        content: '';
        display: inline-block;
        width: 16px;
        height: 16px;
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        border-radius: 50%;
        border: 2px solid #21272A; 
        background-color: white;
    }

    &:checked + label::after {
        content: '';
        display: inline-block;
        width: 10px;
        height: 10px;
        position: absolute;
        left: 5px;
        top: 50%;
        transform: translateY(-50%);
        border-radius: 50%;
        background-color: #21272A; 
    }
`


const GeneralAccess = () => {
    return (

        <div>

            <RadioChecklistContainer>
                <RadioChecklist>
                    <InputRadio type="radio" name="checklist" id="item1"></InputRadio>
                    <Label for="item1">Apenas pessoas adicionadas</Label>
                </RadioChecklist>

                <RadioChecklist>
                    <InputRadio type="radio" name="checklist" id="item2"></InputRadio>
                    <Label for="item2">Qualquer um com o link pode editar</Label>
                </RadioChecklist>

                <RadioChecklist>
                    <InputRadio type="radio" name="checklist" id="item3"></InputRadio>
                    <Label for="item3">Qualquer um com o link pode visualizar</Label>
                </RadioChecklist>

                <RadioChecklist>
                    <InputRadio type="radio" name="checklist" id="item4"></InputRadio>
                    <Label for="item4">Todos da plataforma podem editar</Label>
                </RadioChecklist>

                <RadioChecklist>
                    <InputRadio type="radio" name="checklist" id="item5"></InputRadio>
                    <Label for="item5">Todos da plataforma podem visualizar</Label>
                </RadioChecklist>
            </RadioChecklistContainer>
        </div>

    );
};

export default GeneralAccess;
