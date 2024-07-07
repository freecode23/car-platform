import React, { useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import './ControlPad.css';

const ControlPad: React.FC = () => {
    const FORWARD_KEY = 'w';
    const LEFT_KEY = 'a';
    const STOP_KEY = 'x';
    const RIGHT_KEY = 'd';
    const BACKWARD_KEY = 's';

    const sendCommand = async (command: string) => {
        try {
            await axios.post(
                `${process.env.NEXT_PUBLIC_COMMAND_SENDER_API_URL}/sendCommand`,
                 null, { params: { command } }
                );
        } catch (error) {
            toast.error('There was an error sending the command!' + error);
        }
    };

    // If button is clicked, send the command directly.
    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            let validCommands = [FORWARD_KEY, LEFT_KEY, STOP_KEY, RIGHT_KEY, BACKWARD_KEY];
            if (!validCommands.includes(event.key)) {
                return;
            }
            sendCommand(event.key);
        };
        window.addEventListener('keydown', handleKeyDown);

        return () => {
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, []);

    return (
        <div className="control-container">
        <div className="control-pad">
            <div className="control-button up" onClick={() => sendCommand(FORWARD_KEY)}>▲</div>
            <div className="control-button left" onClick={() => sendCommand(LEFT_KEY)}>◀</div>
            <div className="control-button stop" onClick={() => sendCommand(STOP_KEY)}>stop</div>
            <div className="control-button right" onClick={() => sendCommand(RIGHT_KEY)}>▶</div>
            <div className="control-button down" onClick={() => sendCommand(BACKWARD_KEY)}>▼</div>
        </div>
        </div>
    );
};

export default ControlPad;
