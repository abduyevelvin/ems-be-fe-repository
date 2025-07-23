import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { updateUser, getUser } from '../../services/users/UserService';

const EditUserComponent = () => {
    const { id } = useParams();
    const [group, setGroup] = useState('USER');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        getUser(id).then(response => {
            setGroup(response.data.group);
        }).catch(error => {
            setErrorMessage('Failed to load user.');
        });
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        updateUser(id, { group })
            .then(response => {
                setSuccessMessage('User updated successfully!');
                setErrorMessage('');
                setTimeout(() => navigate('/users'), 1000);
            })
            .catch(error => {
                setErrorMessage('Failed to update user.');
                setSuccessMessage('');
            });
    };

    return (
        <div className='container'>
            <h2 className='text-center'>Edit User Group</h2>
            {errorMessage && <div className='alert alert-danger'>{errorMessage}</div>}
            {successMessage && <div className='alert alert-success'>{successMessage}</div>}
            <form onSubmit={handleSubmit}>
                <div className='mb-3'>
                    <label>Group:</label>
                    <select className='form-control' value={group} onChange={e => setGroup(e.target.value)} required>
                        <option value='USER'>USER</option>
                        <option value='ADMIN'>ADMIN</option>
                    </select>
                </div>
                <button type='submit' className='btn btn-primary'>Save</button>
                <button type='button' className='btn btn-secondary ms-2' onClick={() => navigate('/users')}>Cancel</button>
            </form>
        </div>
    );
};

export default EditUserComponent;
