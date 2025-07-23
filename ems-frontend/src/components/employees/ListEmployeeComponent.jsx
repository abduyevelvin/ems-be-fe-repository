import React, { useEffect, useState } from 'react'
import { deleteEmployee, listEmployees } from '../../services/employees/EmployeeService'
import { useNavigate } from 'react-router-dom'

const ListEmployeeComponent = () => {

    const [employees, setEmployees] = useState([])
    const [errorMessage, setErrorMessage] = useState("")

    const navigator = useNavigate()

    useEffect(() => {
        getAllEmployees()
    }, [])

    function getAllEmployees() {
        listEmployees().then((response) => {
            setEmployees(response.data)
            setErrorMessage("")
        }).catch(error => {
            if (error.response && error.response.status === 403) {
                setErrorMessage("Access denied: You are not authorized to view employees.")
            } else {
                setErrorMessage("An error occurred while fetching employees.")
            }
            console.error(error)
        })
    }

    function addNewEmployee() {
        navigator('/add-employee')
    }

    function updateEmployee(id) {
        navigator(`edit-employee/${id}`)
    }

    function removeEmployee(id) {
        if (window.confirm('Are you sure to delete this employee?')) {
            deleteEmployee(id).then((response) => {
                getAllEmployees()
            }).catch(error => {
                console.error(error)
            })
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
            <h2 className='text-center'>List of Employees</h2>
            {isAdmin() && (
                <button className='btn btn-primary mb-2' onClick={addNewEmployee}>
                    Add Employee
                </button>
            )}
            <table className='table table-striped table-bordered'>
                <thead>
                    <tr>
                        <th>Employee First Name</th>
                        <th>Employee Last Name</th>
                        <th>Employee Email</th>
                        {isAdmin() && <th>Actions</th>}
                    </tr>
                </thead>
                <tbody>
                    {
                        employees.map(employee =>
                            <tr key={employee.employeeId}>
                                <td>{employee.firstName}</td>
                                <td>{employee.lastName}</td>
                                <td>{employee.email}</td>
                                {isAdmin() && (
                                    <td>
                                        <button className='btn btn-info ms-2' onClick={() => updateEmployee(employee.employeeId)}><i className='bi bi-pencil-square'></i> Update</button>
                                        <button className='btn btn-danger ms-2' onClick={() => removeEmployee(employee.employeeId)}><i className='bi bi-trash3'></i> Delete</button>
                                    </td>
                                )}
                            </tr>
                        )
                    }
                </tbody>
            </table>
        </div>
    )
}

export default ListEmployeeComponent