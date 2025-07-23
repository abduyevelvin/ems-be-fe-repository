import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../../services/users/UserService';

const LoginComponent = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = (e) => {
        e.preventDefault();
        loginUser({ username, password })
            .then(response => {
                // Save token if needed
                localStorage.setItem('token', response.data.token);
                localStorage.setItem('group', response.data.group);
                setErrorMessage('');
                navigate('/');
            })
            .catch(error => {
                if (error.response && error.response.status === 401) {
                    setErrorMessage("Invalid credentials.")
                } else if (error.response && error.response.status === 404) {
                    setErrorMessage("User not found.")
                } else {
                    setErrorMessage('Server error.');
                }
                console.error(error);
            });
    };

    return (
        <div className='container'>
            <h2 className='text-center'>Login</h2>
            {errorMessage && <div className='alert alert-danger'>{errorMessage}</div>}
            <form onSubmit={handleLogin}>
                <div className='mb-3'>
                    <label>Username:</label>
                    <input type='text' className='form-control' value={username} onChange={e => setUsername(e.target.value)} required />
                </div>
                <div className='mb-3'>
                    <label>Password:</label>
                    <input type='password' className='form-control' value={password} onChange={e => setPassword(e.target.value)} required />
                </div>
                <button type='submit' className='btn btn-primary'>Login</button>
            </form>
        </div>
    );
};

export default LoginComponent;
