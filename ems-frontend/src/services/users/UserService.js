import axios from 'axios';

const USER_API_BASE_URL = 'http://localhost:8181/api/v1/users';

function getAuthHeader() {
    const token = localStorage.getItem('token');
    return token ? { Authorization: `Bearer ${token}` } : {};
}

export const loginUser = (loginData) => axios.post(`${USER_API_BASE_URL}/login`, loginData)

export const registerUser = (registerData) => axios.post(`${USER_API_BASE_URL}/register`, registerData)

export function listUsers() {
    return axios.get(USER_API_BASE_URL, {
        headers: getAuthHeader()
    });
}

export function deleteUser(id) {
    return axios.delete(`${USER_API_BASE_URL}/${id}`, {
        headers: getAuthHeader()
    });
}

export function getUser(id) {
    return axios.get(`${USER_API_BASE_URL}/${id}`, {
        headers: getAuthHeader()
    });
}

export function updateUser(id, updateData) {
    return axios.patch(`${USER_API_BASE_URL}/${id}`, updateData, {
        headers: getAuthHeader()
    });
}
