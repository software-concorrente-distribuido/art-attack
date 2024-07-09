import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    tool: null,
    elements: [],
    lineWidth: 2, // Valor inicial para a espessura da linha
    color: '#000000',
};

const whiteboardSlice = createSlice({
    name: 'whiteboard',
    initialState,
    reducers: {
        clearElements: (state) => {
            state.elements = [];
        },
        setToolType: (state, action) => {
            state.tool = action.payload;
        },
        setLineWidth: (state, action) => {
            state.lineWidth = action.payload;
        },
        setColor: (state, action) => {
            state.color = action.payload;
        },
        updateElement: (state, action) => {
            const { id } = action.payload;

            const index = state.elements.findIndex(
                (element) => element.id === id
            );

            if (index === -1) {
                state.elements.push(action.payload);
            } else {
                state.elements[index] = action.payload;
            }
        },
        setElements: (state, action) => {
            state.elements = action.payload;
        },
    },
});

export const {
    setToolType,
    setLineWidth,
    setColor,
    updateElement,
    setElements,
    clearElements,
} = whiteboardSlice.actions;

export default whiteboardSlice.reducer;
