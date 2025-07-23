import React, { useEffect, useState } from 'react';
import { listUsers, deleteUser } from '../../services/users/UserService';
import { useNavigate } from 'react-router-dom';

const ListUserComponent = () => {
    const [users, setUsers] = useState([]);
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        getAllUsers();
    }, []);

    function getAllUsers() {
        listUsers().then((response) => {
            setUsers(response.data);
            setErrorMessage("");
        }).catch(error => {
            setErrorMessage("An error occurred while fetching users.");
            console.error(error);
        });
    }

    function updateUser(id) {
        navigate(`/edit-user/${id}`);
    }

    function removeUser(id) {
        if (window.confirm('Are you sure to delete this user?')) {
            deleteUser(id).then(() => {
                getAllUsers();
            }).catch(error => {
                setErrorMessage("Failed to delete user.");
                console.error(error);
            });
        }
    }

    function isAdmin() {
        return localStorage.getItem('group') === 'ADMIN';
    }

    return (
        <div className='container'>
            {errorMessage && (
                <div className='alert alert-danger' role='alert'>
                    {errorMessage}
                </div>
            )}
            <h2 className='text-center'>List of Users</h2>
            <table className='table table-striped table-bordered'>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Group</th>
                        <th>Permissions</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            <td>{user.username}</td>
                            <td>{user.group}</td>
                            <td>{user.permissions.join(', ')}</td>
                            <td>
                                {isAdmin() && (
                                    <button className='btn btn-info ms-2' onClick={() => updateUser(user.id)}><i className='bi bi-pencil-square'></i> Edit</button>
                                )}
                                <button className='btn btn-danger ms-2' onClick={() => removeUser(user.id)}><i className='bi bi-trash3'></i> Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ListUserComponent;
