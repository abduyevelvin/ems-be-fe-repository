import React from 'react';
import { useNavigate } from 'react-router-dom';

function isTokenValid(token) {
    if (!token) return false;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        if (!payload.exp) return false;
        // exp is in seconds, Date.now() in ms
        return payload.exp * 1000 > Date.now();
    } catch (e) {
        return false;
    }
}

const WelcomeComponent = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const isLoggedIn = isTokenValid(token);

    return (
        <div className='container text-center mt-5'>
            <h1>Welcome to EMS</h1>
            {isLoggedIn ? (
                <div className='mt-4'>
                    <button className='btn btn-primary m-2' onClick={() => navigate('/employees')}>
                        List Employees
                    </button>
                    <button className='btn btn-secondary m-2' onClick={() => navigate('/users')}>
                        List Users
                    </button>
                </div>
            ) : (
                <div className='mt-4'>
                    <button className='btn btn-success m-2' onClick={() => navigate('/login')}>
                        Login
                    </button>
                    <button className='btn btn-outline-primary m-2' onClick={() => navigate('/register')}>
                        Register
                    </button>
                </div>
            )}
        </div>
    );
};

export default WelcomeComponent;
