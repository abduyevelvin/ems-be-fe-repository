import './App.css'
import EmployeeComponent from './components/employees/EmployeeComponent'
import FooterComponent from './components/FooterComponent'
import HeaderComponent from './components/HeaderComponent'
import ListEmployeeComponent from './components/employees/ListEmployeeComponent'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import LoginComponent from './components/users/LoginComponent'
import RegisterComponent from './components/users/RegisterComponent'
import WelcomeComponent from './components/WelcomeComponent'
import ListUserComponent from './components/users/ListUserComponent'
import EditUserComponent from './components/users/EditUserComponent'

function App() {

  return (
    <>
      <BrowserRouter>
        <HeaderComponent />
        <Routes>
          {/* //http://localhost:3000 */}
          <Route path='/' element={<WelcomeComponent />}></Route>
          {/* //http://localhost:3000/employees */}
          <Route path='/employees' element={<ListEmployeeComponent />}></Route>
          {/* //http://localhost:3000/add-employee */}
          <Route path='/add-employee' element={<EmployeeComponent />}></Route>
          {/* //http://localhost:3000/edit-employee/1 */}
          <Route path='/edit-employee/:id' element={<EmployeeComponent />} />
          <Route path='/employees/edit-employee/:id' element={<EmployeeComponent />} />
          {/* //http://localhost:3000/users */}
          <Route path='/users' element={<ListUserComponent />} />
          {/* //http://localhost:3000/edit-user/1 */}
          <Route path='/edit-user/:id' element={<EditUserComponent />} />
          {/* //http://localhost:3000/login */}
          <Route path='/login' element={<LoginComponent />} />
          {/* //http://localhost:3000/register */}
          <Route path='/register' element={<RegisterComponent />} />
        </Routes>
        <FooterComponent />
      </BrowserRouter>
    </>
  )
}

export default App
