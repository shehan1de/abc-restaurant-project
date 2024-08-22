import axios from 'axios';
import React, { useEffect, useState } from 'react';
import Navigation2 from '../navigation2';

const FavoritesPage = () => {
  const [favoriteProductIds, setFavoriteProductIds] = useState([]);
  const [productDetails, setProductDetails] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);

  // Retrieve user ID from token
  const getUserIdFromToken = () => {
    const token = localStorage.getItem('token'); // Assuming token is stored in localStorage
    if (token) {
      try {
        // Decode the token to get userId (assuming token contains userId)
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Decode JWT token (assuming it’s a JWT)
        return decodedToken.userId; // Adjust according to your token structure
      } catch (error) {
        console.error('Error decoding token:', error);
      }
    }
    return null;
  };

  // Fetch favorite product IDs for the user
  useEffect(() => {
    const fetchFavoriteProductIds = async () => {
      try {
        const userId = getUserIdFromToken();
        if (userId) {
          const response = await axios.get(`/api/favorites/list?userId=${userId}`);
          if (response.data && Array.isArray(response.data.productIds)) {
            setFavoriteProductIds(response.data.productIds);
          } else {
            console.error('Unexpected response format:', response.data);
          }
        } else {
          console.error('User ID not found in token');
        }
      } catch (error) {
        console.error('Error fetching favorite product IDs:', error);
      }
    };

    fetchFavoriteProductIds();
  }, []);

  // Fetch details for the current product
  useEffect(() => {
    const fetchProductDetails = async () => {
      if (favoriteProductIds.length > 0 && currentIndex < favoriteProductIds.length) {
        try {
          const productId = favoriteProductIds[currentIndex];
          const response = await axios.get(`/product/byProductId/${productId}`);
          if (response.data) {
            setProductDetails(prevDetails => {
              // Check if product detail is already present
              const existingDetail = prevDetails.find(detail => detail.productId === productId);
              if (existingDetail) return prevDetails;
              return [...prevDetails, response.data];
            });
          } else {
            console.error('Unexpected response format for product details:', response.data);
          }
        } catch (error) {
          console.error('Error fetching product details:', error);
        }
      }
    };

    fetchProductDetails();
  }, [favoriteProductIds, currentIndex]);

  const handleNext = () => {
    if (currentIndex < favoriteProductIds.length - 1) {
      setCurrentIndex(currentIndex + 1);
    }
  };

  const handlePrevious = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 1);
    }
  };

  return (
    <>
      <Navigation2 />
      <div>
        <h1>Favorite Products</h1>
        {productDetails.length > 0 && productDetails[currentIndex] ? (
          <div>
            <h2>{productDetails[currentIndex].productName}</h2>
            <img src={productDetails[currentIndex].productImage} alt={productDetails[currentIndex].productName} />
            <p>Price: ${productDetails[currentIndex].productPrice}</p>
            <p>Description: {productDetails[currentIndex].productDescription}</p>
            {/* Add other product details here */}
            <button onClick={handlePrevious} disabled={currentIndex === 0}>
              Previous
            </button>
            <button onClick={handleNext} disabled={currentIndex === favoriteProductIds.length - 1}>
              Next
            </button>
          </div>
        ) : (
          <p>Loading product details...</p>
        )}
      </div>
    </>
  );
};

export default FavoritesPage;
