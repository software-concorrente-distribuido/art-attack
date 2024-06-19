import React, { useState, useRef, useEffect } from 'react';
import Header from '../../components/HeaderPaint';
import Sidebar from '../../components/SidebarPaint';
import * as C from './styles';
import socketService from '../../services/socket';
import Cookies from 'js-cookie';
import Whiteboard from '../../components/whiteboard/whiteboard';
import { updateElement as updateElementInStore } from '../../components/whiteboard/whiteboardSlice';
import { useDispatch } from 'react-redux';

const Paint = () => {
    const dispatch = useDispatch();

    useEffect(() => {
        const token = Cookies.get('user_token');
        
        if (token) {
            socketService.connect(() => {
                console.log('Subscribing to /topic/alteracoes...');
                socketService.subscribe('/topic/alteracoes', (message) => {
                    console.log("Raw message received:", message);

                    if (!message || !message.delta) {
                        console.error("Received a message without a delta.");
                        return;
                    }
                
                    try {
                        console.log("Delta received:", message.delta);
                        dispatch(updateElementInStore(message.delta));
                    } catch (error) {
                        console.error("Erro ao obter o delta:", error);
                    }
                });
            });
        } else {
            console.error('JWT token não encontrado.');
        }

        return () => {
            socketService.disconnect();
        };
    }, [dispatch]);


    return (
        <div>
            {/* <Header onUndo={handleUndo} onRedo={handleRedo} /> */}
            <Header />
            <Sidebar
                // onToolChange={handleToolChange}
                // onColorChange={handleColorChange}
                // onLineWidthChange={handleLineWidthChange}
            />
            <Whiteboard />
            {/* <canvas
                ref={previewCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{ marginLeft: '60px', marginTop: '60px', backgroundColor: 'transparent', position: 'absolute', pointerEvents: 'none', zIndex: 1 }}
            />
            <canvas
                ref={mainCanvasRef}
                width={window.innerWidth - 60}
                height={window.innerHeight - 60}
                style={{ marginLeft: '60px', marginTop: '60px', backgroundColor: '#fff', position: 'absolute', zIndex: 0 }}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
            /> */}
        </div>
    );
};

export default Paint;
