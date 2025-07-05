import React, { useEffect, useState } from 'react'
import { createEmployee, getEmployee, updateEmployee } from '../services/EmployeeService'
import { useNavigate, useParams, Link } from 'react-router-dom'

const EmployeeComponent = () => {

    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')

    const { id } = useParams()

    const [errors, setErrors] = useState({
        firstName: '',
        lastName: '',
        email: ''
    })

    const handleLastName = (e) => setLastName(e.target.value)

    const handleEmail = (e) => setEmail(e.target.value)

    const navigator = useNavigate()

    const handleCancel = () => {
        setFirstName('');
        setLastName('');
        setEmail('');
        setErrors({ firstName: '', lastName: '', email: '' });
        navigator('/employees');
    };

    useEffect(() => {
        if (id) {
            getEmployee(id).then((response) => {
                setFirstName(response.data.firstName)
                setLastName(response.data.lastName)
                setEmail(response.data.email)
            }).catch(error => {
                console.error(error)
            })
        }
    }, [id])

    function saveOrUpdateEmployee(e) {
        e.preventDefault()

        if (validateForm()) {
            const employee = { firstName, lastName, email }
            console.log(employee)

            if (id) {
                updateEmployee(id, employee).then((response) => {
                    console.log(response.data)
                    navigator('/employees')
                }).catch(error => {
                    if (
                        error.response &&
                        error.response.data &&
                        error.response.data.message &&
                        error.response.data.message.includes('Employee already exists with email')
                    ) {
                        setErrors(prev => ({ ...prev, email: 'Employee with this email already exists' }))
                    } else {
                        console.error(error)
                    }
                })
            } else {
                createEmployee(employee).then((response) => {
                    console.log(response.data)
                    navigator('/employees')
                }).catch(error => {
                    if (
                        error.response &&
                        error.response.data &&
                        error.response.data.message &&
                        error.response.data.message.includes('Employee already exists with email')
                    ) {
                        setErrors(prev => ({ ...prev, email: 'Employee with this email already exists' }))
                    } else {
                        console.error(error)
                    }
                })
            }
        }
    }

    function validateForm() {
        let valid = true

        const errorsCopy = { ...errors }

        if (firstName.trim()) {
            errorsCopy.firstName = ''
        } else {
            errorsCopy.firstName = 'First Name is required'
            valid = false
        }

        if (lastName.trim()) {
            errorsCopy.lastName = ''
        } else {
            errorsCopy.lastName = 'Last Name is required'
            valid = false
        }

        if (email.trim()) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (emailRegex.test(email)) {
                errorsCopy.email = ''
            } else {
                errorsCopy.email = 'Email format is invalid'
                valid = false
            }
        } else {
            errorsCopy.email = 'Email Address is required'
            valid = false
        }

        setErrors(errorsCopy)

        return valid
    }

    function pageTitle() {
        if (id) {
            return <h2 className='text-center'>Update Employee</h2>
        } else {
            return <h2 className='text-center'>Add Employee</h2>
        }
    }

    return (
        <div className='container'>
            <div className='row'>
                <div className='card'>
                    {
                        pageTitle()
                    }
                    <div className='card-body'>
                        <form>
                            <div className='form-group mb-2'>
                                <label className='form-label'>First Name:</label>
                                <input
                                    type='text'
                                    placeholder='Enter Employee First Name'
                                    name='firstName'
                                    value={firstName}
                                    className={`form-control ${errors.firstName ? 'is-invalid' : ''}`}
                                    onChange={(e) => setFirstName(e.target.value)}>
                                </input>
                                {errors.firstName && <div className='invalid-feedback'> {errors.firstName} </div>}
                            </div>

                            <div className='form-group mb-2'>
                                <label className='form-label'>Last Name:</label>
                                <input
                                    type='text'
                                    placeholder='Enter Employee Last Name'
                                    name='lastName'
                                    value={lastName}
                                    className={`form-control ${errors.lastName ? 'is-invalid' : ''}`}
                                    onChange={handleLastName}>
                                </input>
                                {errors.lastName && <div className='invalid-feedback'> {errors.lastName} </div>}
                            </div>

                            <div className='form-group mb-2'>
                                <label className='form-label'>Email:</label>
                                <input
                                    type='text'
                                    placeholder='Enter Employee Email Address'
                                    name='email'
                                    value={email}
                                    className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                                    onChange={handleEmail}>
                                </input>
                                {errors.email && <div className='invalid-feedback'> {errors.email} </div>}
                            </div>

                            <div className="d-flex justify-content-between">
                                <button type="button" className='btn btn-danger' onClick={handleCancel}>
                                    <i className="bi bi-x-square"></i> Cancel
                                </button>
                                <button className='btn btn-success' onClick={saveOrUpdateEmployee}>
                                    <i className="bi bi-check2-square"></i> Save
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default EmployeeComponent