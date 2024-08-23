import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../CSS/Card.css';
import Navigation2 from '../navigation2';

const FavoritesPage = () => {
  const [favoriteProductIds, setFavoriteProductIds] = useState([]);
  const [productDetails, setProductDetails] = useState([]);
  const [cart, setCart] = useState({});
  const [favorites, setFavorites] = useState(new Set());
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user'));
  const userId = user ? user.userId : null;

  useEffect(() => {
    const fetchFavoriteProductIds = async () => {
      try {
        const response = await axios.get(`/api/favorites/list?userId=${userId}`);
        setFavoriteProductIds(response.data);
        setFavorites(new Set(response.data));
      } catch (error) {
        console.error('Error fetching favorite product IDs:', error);
      }
    };

    fetchFavoriteProductIds();
  }, [userId]);

  useEffect(() => {
    const fetchProductDetails = async () => {
      try {
        const productRequests = favoriteProductIds.map((productId) =>
          axios.get(`/product/byProductId/${productId}`)
        );
        const responses = await Promise.all(productRequests);
        const products = responses.map((response) => response.data);
        setProductDetails(products);
      } catch (error) {
        console.error('Error fetching product details:', error);
      }
    };

    if (favoriteProductIds.length > 0) {
      fetchProductDetails();
    }
  }, [favoriteProductIds]);

  const handleQuantityChange = (productId, delta) => {
    setCart((prevCart) => {
      const newQuantity = Math.max((prevCart[productId]?.quantity || 0) + delta, 0);
      const updatedCart = {
        ...prevCart,
        [productId]: {
          ...(prevCart[productId] || {}),
          quantity: newQuantity,
        },
      };
      localStorage.setItem('cart', JSON.stringify(updatedCart));
      return updatedCart;
    });
  };

  const handleAddToCart = async (product) => {
    try {
      const currentQuantity = cart[product.productId]?.quantity || 0;
      const newQuantity = currentQuantity + 1;

      // Update the cart in the backend
      await axios.post('/api/cart/add', {
        userId,
        productId: product.productId,
        quantity: newQuantity,
      });

      // Update local cart state and localStorage
      setCart((prevCart) => {
        const updatedCart = {
          ...prevCart,
          [product.productId]: {
            ...product,
            quantity: newQuantity,
          },
        };
        localStorage.setItem('cart', JSON.stringify(updatedCart));
        return updatedCart;
      });
    } catch (error) {
      console.error('Error adding product to cart:', error);
    }
  };

  const toggleFavorite = async (productId) => {
    if (!userId) {
      console.log('User not logged in');
      return;
    }

    const isFavorite = favorites.has(productId);
    const newFavorites = new Set(favorites);

    try {
      if (isFavorite) {
        await axios.post('/api/favorites/remove', null, { params: { userId, productId } });
        newFavorites.delete(productId);
        setFavoriteProductIds((prevIds) => prevIds.filter(id => id !== productId));
        setProductDetails((prevProducts) => prevProducts.filter(product => product.productId !== productId));
      } else {
        await axios.post('/api/favorites/add', null, { params: { userId, productId } });
        newFavorites.add(productId);
        const response = await axios.get(`/product/byProductId/${productId}`);
        setProductDetails(prev => [...prev, response.data]);
      }

      setFavorites(newFavorites);
    } catch (error) {
      console.error('Error toggling favorite:', error);
    }
  };

  return (
    <>
      <Navigation2 />
      <h1 className="form-head-one">
        <span>Favorites</span>
      </h1>

      {productDetails.length === 0 ? (
        <div className="no-products-message">
          <p>Currently, there are no favorite products available.</p>
        </div>
      ) : (
        <div className="custom-row">
          {productDetails.map((product) => (
            <div className="custom-col" key={product.productId}>
              <div className="custom-card">
                <img
                  src={`/images/${product.productImage}`}
                  className="custom-card-img-top"
                  alt={product.productName}
                />
                <div className="custom-card-body">
                  <h5 className="custom-card-title">{product.productName}</h5>
                  <p className="custom-card-text">Rs. {product.productPrice.toFixed(2)}</p>

                  <div className="custom-favorite-icon" onClick={() => toggleFavorite(product.productId)}>
                    <i className={`bi ${favorites.has(product.productId) ? 'bi-heart-fill' : 'bi-heart'}`}></i>
                  </div>

                  <div className="custom-quantity-controls">
                    <button
                      className="custom-btn custom-btn-outline-secondary"
                      onClick={() => handleQuantityChange(product.productId, -1)}
                    >
                      <i className="bi bi-dash"></i>
                    </button>
                    <span className="custom-mx-2">{cart[product.productId]?.quantity || 0}</span>
                    <button
                      className="custom-btn custom-btn-outline-secondary"
                      onClick={() => handleQuantityChange(product.productId, 1)}
                    >
                      <i className="bi bi-plus"></i>
                    </button>
                  </div>
                  <button
                    className="custom-btn custom-btn-primary custom-mt-2"
                    onClick={() => handleAddToCart(product)}
                  >
                    Add to Cart
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );
};

export default FavoritesPage;
