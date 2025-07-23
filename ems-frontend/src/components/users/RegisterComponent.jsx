import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../../services/users/UserService';

const RegisterComponent = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleRegister = (e) => {
        e.preventDefault();
        registerUser({ username, password })
            .then(response => {
                setSuccessMessage('Registration successful! You can now log in.');
                setErrorMessage('');
                setTimeout(() => navigate('/login'), 1500);
            })
            .catch(error => {
                setErrorMessage('Registration failed. Please try again.');
                setSuccessMessage('');
            });
    };

    return (
        <div className='container'>
            <h2 className='text-center'>Register</h2>
            {errorMessage && <div className='alert alert-danger'>{errorMessage}</div>}
            {successMessage && <div className='alert alert-success'>{successMessage}</div>}
            <form onSubmit={handleRegister}>
                <div className='mb-3'>
                    <label>Username:</label>
                    <input type='text' className='form-control' value={username} onChange={e => setUsername(e.target.value)} required />
                </div>
                <div className='mb-3'>
                    <label>Password:</label>
                    <input type='password' className='form-control' value={password} onChange={e => setPassword(e.target.value)} required />
                </div>
                <button type='submit' className='btn btn-primary'>Register</button>
            </form>
        </div>
    );
};

export default RegisterComponent;
