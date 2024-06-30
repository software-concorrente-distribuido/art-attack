export const generateSprayPoints = (x, y, lineWidth) => {
    const points = [];
    const sprayDensity = lineWidth <= 5 ? 5 : 50;
    for (let i = 0; i < sprayDensity; i++) {
        const offsetX = (Math.random() - 0.5) * (lineWidth * 3);
        const offsetY = (Math.random() - 0.5) * (lineWidth * 3);
        points.push({ x: x + offsetX, y: y + offsetY });
    }
    return points;
};
