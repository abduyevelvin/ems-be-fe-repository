import axios from 'axios'

const REST_API_BASE_URL = 'http://localhost:8181/api/v1/employees'

export const listEmployees = () => {
    return axios.get(REST_API_BASE_URL)
}

export const createEmployee = (employee) => axios.post(REST_API_BASE_URL, employee)

export const getEmployee = (employeeId) => axios.get(REST_API_BASE_URL + '/' + employeeId)

export const updateEmployee = (employeeId, employee) => axios.put(REST_API_BASE_URL + '/' + employeeId, employee)

export const deleteEmployee = (employeeId) => axios.delete(REST_API_BASE_URL + '/' + employeeId)