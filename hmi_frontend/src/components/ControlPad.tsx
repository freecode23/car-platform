import React from 'react';
import axios from 'axios';
import './ControlPad.css';

const ControlPad: React.FC = () => {
  const sendCommand = (command: string) => {
    console.log("sending command");
    axios.post('http://localhost:8082/api/sendCommand', null, { params: { command } })
      .then(response => {
        console.log(response.data);
      })
      .catch(error => {
        console.error('There was an error sending the command!', error);
      });
  };

  return (
    <div className="control-container">
      <div className="control-pad">
        <div className="control-button up" onClick={() => sendCommand('w')}>▲</div>
        <div className="control-button left" onClick={() => sendCommand('a')}>◀</div>
        <div className="control-button stop" onClick={() => sendCommand('x')}>OK</div>
        <div className="control-button right" onClick={() => sendCommand('d')}>▶</div>
        <div className="control-button down" onClick={() => sendCommand('s')}>▼</div>
      </div>
    </div>
  );
};

export default ControlPad;
