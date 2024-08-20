import 'bootstrap/dist/css/bootstrap.min.css';
import { jwtDecode } from 'jwt-decode';
import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import '../CSS/Navigation2.css';
import defaultProfilePic from '../Image/logo.png';

const SecNavigation = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [user, setUser] = useState({
        username: '',
        email: '',
        profilePicture: defaultProfilePic
    });

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decoded = jwtDecode(token);
            setUser({
                username: decoded.sub,
                email: decoded.userEmail,
                profilePicture: decoded.profilePicture ? decoded.profilePicture : defaultProfilePic
            });
        }
    }, []);

    const handleLogout = () => {
        Swal.fire({
            title: 'Logging out...',
            text: 'Please wait',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        setTimeout(() => {
            localStorage.clear();
            Swal.fire({
                icon: 'success',
                title: 'Logged Out',
                text: 'You have been logged out successfully!',
                timer: 2500,
                showConfirmButton: false
            }).then(() => {
                navigate('/login');
            });
        },);
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-transparent">
            <div className="container-fluid">
                <div className="navbar-brand" to="/">
                    <span className="navbar-brand-text">ABC RESTAURANT</span>
                </div>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse flex-grow-1 justify-content-center" id="navbarNavDropdown">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <Link className={`nav-link ${location.pathname === '/customer-dashboard' ? 'active' : ''}`} to="/customer-dashboard">Home</Link>
                        </li>
                        <li className="nav-item">
                            <Link className={`nav-link ${location.pathname === '/favorites' ? 'active' : ''}`} to="/favorites">Favorites</Link>
                        </li>
                        <li className="nav-item">
                            <Link className={`nav-link ${location.pathname === '/customer/cart' ? 'active' : ''}`} to="/customer/cart">Cart</Link>
                        </li>
                        <li className="nav-item">
                            <Link className={`nav-link ${location.pathname === '/customer/purchases' ? 'active' : ''}`} to="/customer/purchases">Purchases</Link>
                        </li>
                        <li className="nav-item">
                            <button className="btn btn-gold nav-link" onClick={handleLogout}>Logout</button>
                        </li>
                    </ul>
                </div>
                <div className="profile-container d-flex align-items-center">
                    <img src={`/images/${user.profilePicture}`} alt="Profile" className="profile-pic" />
                    <div className="profile-info ms-2">
                        <span className="username">{user.username}</span>
                        <br />
                        <span className="email">{user.email}</span>
                    </div>
                    <div className="dropdown ms-3">
                        <button
                            className="btn btn-secondary dropdown-toggle"
                            type="button"
                            id="dropdownMenuButton"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                        >
                            <i className="bi bi-gear"></i>
                        </button>
                        <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                            <li><Link className="dropdown-item" to="/profile">Change Profile</Link></li>
                            <li><Link className="dropdown-item" to="/change-password">Change Password</Link></li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default SecNavigation;
