import React from 'react'
import { Link, useNavigate } from 'react-router-dom';

const HeaderComponent = () => {
  const navigate = useNavigate();
  const isLoggedIn = (() => {
    const token = localStorage.getItem('token');
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  })();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div>
      <header>
        <nav className='navbar navbar-dark bg-dark d-flex justify-content-between'>
          <Link className='navbar-brand' to='/'>
            Employee Management System
          </Link>
          {isLoggedIn && (
            <button className='btn btn-outline-light' onClick={handleLogout}>
              Logout
            </button>
          )}
        </nav>
      </header>
    </div>
  );
};

export default HeaderComponent