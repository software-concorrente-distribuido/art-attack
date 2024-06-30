import styled from 'styled-components';

export const SidebarContainer = styled.div`
    width: 80px;
    height: calc(100vh - 60px); /* Altura para ficar abaixo do header */
    background-color: #2e3238;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 0;
    position: fixed;
    left: 0;
    top: 60px; /* Para ficar abaixo do header */
    z-index: 999;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
    overflow-y: auto; /* Barra de rolagem */
    border-right: 1px solid #444;
`;

export const ToolButton = styled.button`
    background: white;
    border: none;
    border-radius: 10px;
    color: white;
    transition: 0.3s;
    font-size: 24px;
    cursor: pointer;
    margin: 10px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
    height: 30px;

    &:hover {
        background: lightgray;
    }
`;

export const ColorBoxContainer = styled.div`
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
`;

export const ColorBox = styled.div`
    width: 20px;
    height: 20px;
    border-radius: 100%;
    background-color: ${(props) => props.color};
    border: 1px solid #fff;
    margin: 5px;
    cursor: pointer;

    &:hover {
        border-color: #ddd;
    }
`;

export const LineWidthPopup = styled.div`
    position: fixed;
    left: 90px;
    top: 50%;
    transform: translateY(-50%);
    background-color: #4d5358;
    border: 1px solid #fff;
    border-radius: 5px;
    padding: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
    z-index: 1000;
`;

export const LineWidthContainer = styled.div`
    display: flex;
    align-items: center;
    cursor: pointer;
    margin: 5px 0;
    padding: 5px;
    border-radius: 5px;

    &:hover {
        background-color: rgba(255, 255, 255, 0.1);
    }
`;

export const LineWidthLabel = styled.span`
    color: white;
    margin-right: 10px;
    font-size: 12px;
`;

export const LineWidthButton = styled.div`
    width: ${(props) =>
        props.width * 2}px; /* Multiplicador para melhor visualização */
    height: ${(props) => props.width * 2}px;
    background-color: white;
    border: 1px solid #fff;

    &:hover {
        border-color: #ddd;
    }
`;

export const SelectedColorIndicator = styled.div`
    display: flex;
    align-items: center;
    margin: 10px 0;
    position: relative;
`;

export const SelectedColor = styled.div`
    width: 25px;
    height: 25px;
    border-radius: 100%;
    background-color: ${(props) => props.color};
    border: 1px solid #fff;
    margin-right: 10px;
`;

export const ColorPickerPopup = styled.div`
    position: fixed;
    left: 90px;
    top: 50%;
    transform: translateY(-50%);
    background-color: #4d5358;
    border: 1px solid #fff;
    border-radius: 5px;
    padding: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
    z-index: 1000;
`;

export const Divider = styled.div`
    width: 80%;
    height: 1px;
    background-color: #bbb;
    margin: 8px 0;
    border-radius: 100px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
`;
