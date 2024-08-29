import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import AboutABC from './Components/AboutABC';
import ChangePassword from './Components/auth/ChangePassword';
import ChangeProfile from './Components/auth/ChangeProfile';
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
import FeedbackResponse from './Components/Feedback';
import Gallery from './Components/Gallery';
import Index from './Components/Index';
import Checkout from './Components/Order/Checkout';
import OrderStaff from './Components/Order/OrderStaff';
import Purchases from './Components/Order/Purchases';
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
        <Route path="/products/:categoryName" element={<ProtectedRoute element={ProductView} allowedRoles={['Customer']} />} />
        <Route path="/product/:productId" element={<ProtectedRoute element={ProductView} allowedRoles={['Customer']} />} />
        <Route path="/favorites" element={<ProtectedRoute element={Favorites} allowedRoles={['Customer']} />} />
        <Route path="/cart" element={<ProtectedRoute element={Cart} allowedRoles={['Customer']} />} />
        <Route path="/checkout" element={<ProtectedRoute element={Checkout} allowedRoles={['Customer']} />} />
        <Route path="/purchases" element={<ProtectedRoute element={Purchases} allowedRoles={['Customer']} />} />
        <Route path="/change-profile" element={<ProtectedRoute element={ChangeProfile} allowedRoles={['Customer','Staff']} />} />
        <Route path="/change-password" element={<ProtectedRoute element={ChangePassword} allowedRoles={['Customer','Staff']} />} />
        <Route path="/order-staff" element={<ProtectedRoute element={OrderStaff} allowedRoles={['Staff']} />} />
        <Route path="/feedback-response" element={<ProtectedRoute element={FeedbackResponse} allowedRoles={['Staff']} />} />
      </Routes>
    </Router>
  );
}

export default App;

