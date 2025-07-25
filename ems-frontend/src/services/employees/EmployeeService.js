import axios from 'axios'

const REST_API_BASE_URL = 'http://localhost:8080/api/v1/employees'

function getAuthHeader() {
    const token = localStorage.getItem('token')
    return token ? { Authorization: `Bearer ${token}` } : {}
}

export const listEmployees = () => {
    return axios.get(REST_API_BASE_URL, {
        headers: getAuthHeader()
    })
}

export const createEmployee = (employee) => axios.post(REST_API_BASE_URL, employee, {
    headers: getAuthHeader()
})

export const getEmployee = (employeeId) => axios.get(REST_API_BASE_URL + '/' + employeeId, {
    headers: getAuthHeader()
})

export const updateEmployee = (employeeId, employee) => axios.put(REST_API_BASE_URL + '/' + employeeId, employee, {
    headers: getAuthHeader()
})

export const deleteEmployee = (employeeId) => axios.delete(REST_API_BASE_URL + '/' + employeeId, {
    headers: getAuthHeader()
})