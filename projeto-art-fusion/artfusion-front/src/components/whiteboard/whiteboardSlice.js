import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    tool: null,
    elements: [],
    lineWidth: 2,
    color: '#000000',
};

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
        setColor: (state, action) => {
            state.color = action.payload;
        },
    },
});

export const { setToolType, setLineWidth, setColor } = whiteboardSlice.actions;

export default whiteboardSlice.reducer;
