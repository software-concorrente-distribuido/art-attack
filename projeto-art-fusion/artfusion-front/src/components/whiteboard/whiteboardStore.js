import { makeAutoObservable, action } from 'mobx';
import { set } from 'mobx';

class WhiteboardStore {
    tool = null;
    elements = [];
    lineWidth = 2; // Valor inicial para a espessura da linha
    color = '#000000';

    constructor() {
        makeAutoObservable(this, {
            setToolType: action,
            setLineWidth: action,
            setColor: action,
            updateElement: action,
            setElements: action,
            clearElements: action,
        });
    }

    setToolType(tool) {
        this.tool = tool;
    }

    setLineWidth(lineWidth) {
        this.lineWidth = lineWidth;
    }

    setColor(color) {
        this.color = color;
    }

    updateElement(element) {
        const index = this.elements.findIndex((el) => el.id === element.id);

        if (index === -1) {
            this.elements.push(element);
        } else {
            //this.elements[index] = element;
            // this.elements[index] = Object.assign(
            //     {},
            //     this.elements[index],
            //     element
            // );
            //this.elements.splice(index, 1, element);
            set(this.elements, index, { ...this.elements[index], ...element });
        }
    }

    setElements(elements) {
        this.elements = elements;
    }

    clearElements() {
        this.elements = [];
    }
}

const store = new WhiteboardStore();
export default store;
