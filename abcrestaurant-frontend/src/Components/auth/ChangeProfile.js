import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // Import jwt-decode to decode the JWT
import React, { useEffect, useState } from 'react';

const ChangeProfile = () => {
  const [username, setUsername] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [profilePicture, setProfilePicture] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) throw new Error('Token is missing');
        
        const decodedToken = jwtDecode(token);
        const { userId } = decodedToken;
        
        // Fetch user data
        const response = await axios.get(`/user/${userId}`);
        const userData = response.data;
        
        setUsername(userData.username);
        setPhoneNumber(userData.phoneNumber);
      } catch (error) {
        console.error('Error fetching user data:', error);
        setMessage('Failed to fetch user data.');
      }
    };

    fetchUserData();
  }, []);

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePhoneNumberChange = (event) => {
    setPhoneNumber(event.target.value);
  };

  const handleProfilePictureChange = (event) => {
    setProfilePicture(event.target.files[0]);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const token = localStorage.getItem('token');
    if (!token) {
      setMessage('Token is missing');
      return;
    }
    
    const decodedToken = jwtDecode(token);
    const { userId } = decodedToken;

    const formData = new FormData();
    formData.append('username', username);
    formData.append('phoneNumber', phoneNumber);
    if (profilePicture) {
      formData.append('profilePicture', profilePicture);
    }

    try {
      const response = await axios.put(`/user/profile/${userId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${token}`, // Add Authorization header
        },
      });
      setMessage('Profile updated successfully!');
    } catch (error) {
      console.error('Error updating profile:', error);
      setMessage('Failed to update profile.');
    }
  };

  return (
    <div className="container mt-4">
      <h2>Change Profile</h2>
      {message && <div className="alert alert-info">{message}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="username" className="form-label">Username</label>
          <input
            type="text"
            className="form-control"
            id="username"
            value={username}
            onChange={handleUsernameChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="phoneNumber" className="form-label">Phone Number</label>
          <input
            type="tel"
            className="form-control"
            id="phoneNumber"
            value={phoneNumber}
            onChange={handlePhoneNumberChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="profilePicture" className="form-label">Profile Picture</label>
          <input
            type="file"
            className="form-control"
            id="profilePicture"
            accept="image/*"
            onChange={handleProfilePictureChange}
          />
        </div>
        <button type="submit" className="btn btn-primary">Update Profile</button>
      </form>
    </div>
  );
};

export default ChangeProfile;
