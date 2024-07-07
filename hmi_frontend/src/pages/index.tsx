// src/pages/index.tsx
import React from 'react';

const Home: React.FC = () => {
  const handleButtonClick = (direction: string) => {
    console.log(direction); // Replace with your function to handle the command
  };

  return (
    <div>
      <h1>Resque</h1>
      <p>Click W, S, A, or D to move forward, backward, left, or right.</p>
    </div>
  );
};

export default Home;
