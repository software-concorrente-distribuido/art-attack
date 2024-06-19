import { toolTypes } from "../../../constants";

export const adjustmentRequired = (type) =>
    [toolTypes.SQUARE, toolTypes.LINE].includes(type);