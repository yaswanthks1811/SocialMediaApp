import React from 'react';

const ErrorMessage = ({ message }) => {
    return (
        <div style={{ color: 'red', margin: '10px 0', border: '1px solid red', padding: '10px', backgroundColor: '#ffe0e0' }}>
            {message}
        </div>
    );
};

export default ErrorMessage;