import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import AboutABC from './Components/AboutABC';
import ForgetPw1 from './Components/auth/ForgetPw1';
import ForgetPw2 from './Components/auth/ForgetPw2';
import Login from './Components/auth/Login';
import Register from './Components/auth/Register';
import ResetPw from './Components/auth/ResetPw';
import Cart from './Components/Cart/Cart';
import Contact from './Components/Contact';
import AdminDashboard from './Components/Dashboard/AdminDashboard';
import CustomerDashboard from './Components/Dashboard/CustomerDashboard';
import StaffDashboard from './Components/Dashboard/StaffDashboard';
import Gallery from './Components/Gallery';
import Index from './Components/Index';
import Favorites from './Components/Product/Favorites';
import Menu from './Components/Product/Menu';
import ProductView from './Components/Product/ProductView';
import ProtectedRoute from './Components/ProtectedRoute';
import Reservation from './Components/Reservation';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Index />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/reservation" element={<Reservation />} />
        <Route path="/gallery" element={<Gallery />} />
        <Route path="/gallery/:type" element={<Gallery />} />
        <Route path="/about" element={<AboutABC />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="*" element={<Index />} />
        <Route path="/forget-password-1" element={<ForgetPw1 />} />
        <Route path="/forget-password-2" element={<ForgetPw2 />} />
        <Route path="/reset-pw" element={<ResetPw />} />
        <Route path="/admin-dashboard" element={<ProtectedRoute element={AdminDashboard} allowedRoles={['Admin']} />} />
        <Route path="/staff-dashboard" element={<ProtectedRoute element={StaffDashboard} allowedRoles={['Staff']} />} />
        <Route path="/customer-dashboard" element={<ProtectedRoute element={CustomerDashboard} allowedRoles={['Customer']} />} />
        <Route path="/products/:categoryName" element={<ProtectedRoute element={ProductView} allowedRoles={['Customer', 'Admin', 'Staff']} />} />
        <Route path="/favorites" element={<ProtectedRoute element={Favorites} allowedRoles={['Customer']} />} />
        <Route path="/cart" element={<ProtectedRoute element={Cart} allowedRoles={['Customer']} />} />
      </Routes>
    </Router>
  );
}

export default App;

