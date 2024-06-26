import { configureStore } from "@reduxjs/toolkit";
import whiteboardSliceReducer from "../components/whiteboard/whiteboardSlice";

export const store = configureStore({
    reducer: {
        whiteboard: whiteboardSliceReducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({
        serializableCheck: {
            ignoreActions: ["whiteboard/setElements"],
            ignoredPaths: ["whiteboard.elements"]
        }
    })
});