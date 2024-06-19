import { createSlice } from "@reduxjs/toolkit";

const initialState ={
    tool: null,
    elements: [],
    lineWidth: 2 // Valor inicial para a espessura da linha
}

const whiteboardSlice = createSlice({
    name: 'whiteboard',
    initialState,
    reducers: {
        setToolType: (state, action) => {
            state.tool = action.payload;
        },
        setLineWidth: (state, action) => {
            state.lineWidth = action.payload;
        },
        updateElement: (state, action) => {
            const { id } = action.payload;

            const index = state.elements.findIndex(element => element.id === id);

            if(index === -1) {
                state.elements.push(action.payload);
            } else {
                state.elements[index] = action.payload; //mudei aqui
            }
        },
        setElements: (state, action) => {
            state.elements = action.payload;
        },
    },
});

export const { setToolType, setLineWidth, updateElement, setElements } = whiteboardSlice.actions;

export default whiteboardSlice.reducer;