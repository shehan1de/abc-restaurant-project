import 'bootstrap/dist/css/bootstrap.min.css';
import { jwtDecode } from 'jwt-decode';
import React, { useEffect, useState } from 'react';
import '../../CSS/Cart.css';
import Navigation2 from '../navigation2';

const Cart = () => {
  const [totalAmount, setTotalAmount] = useState(0);
  const [userId, setUserId] = useState('');
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        setUserId(decodedToken.userId);
      } catch (error) {
        console.error('Invalid token', error);
      }
    }
  }, []);

  useEffect(() => {
    if (userId) {
      const fetchCartDetails = async () => {
        try {
          const response = await fetch(`/api/cart/detailsInfo?userId=${userId}`);
          const data = await response.json();
          setTotalAmount(data.totalAmount);
          setCartItems(data.products);
        } catch (error) {
          console.error('Failed to fetch cart details', error);
        }
      };

      fetchCartDetails();
    }
  }, [userId]);

  const handleClearFromCart = async (productId, quantity) => {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('User not authenticated');
      return;
    }

    const userId = jwtDecode(token).userId;

    try {
      const response = await fetch(`/api/cart/remove?userId=${userId}&productId=${productId}&quantity=${quantity}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        console.log('Product removed from cart');
        fetchCartItems(); // Refresh the cart items
      } else {
        console.error('Failed to remove product from cart');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const fetchCartItems = async () => {
    if (userId) {
      try {
        const response = await fetch(`/api/cart/detailsInfo?userId=${userId}`);
        const data = await response.json();
        setTotalAmount(data.totalAmount);
        setCartItems(data.products);
      } catch (error) {
        console.error('Failed to fetch cart details', error);
      }
    }
  };

  return (
    <>
      <Navigation2 />
      <div className="cart-header">
        <span><i className="bi bi-cart3 fs-3"></i></span>
        <h5 className="total-amount" style={{ fontSize: '1.25rem' }}>
          Total Amount&nbsp;<span style={{ margin: '0 0.5rem' }}>-</span>&nbsp;Rs. {totalAmount.toFixed(2)}
        </h5>
        <div>
          <button type="button" className="btn-btn-warning">
            Continue Shopping
          </button>
          <button type="button" className="btn-btn-danger-me-2">
            Go to Checkout
          </button>
        </div>
      </div>
      <div className="cart-items-container mt-4">
        <div className="row">
          {cartItems.map(item => (
            <div className="col-md-6 mb-4" key={item.productId}>
              <div className="card-custom-card">
                <img src={`/images/${item.productImage}`} className="card-img" alt={item.productName} />
                <div className="card-body">
                  <div className="card-product-info">
                    <h5 className="card-product-name">{item.productName}</h5>
                    <p className="card-product-description">{item.productDescription}</p>
                    <p className="card-product-quantity">{item.quantity} {item.productName}</p>
                  </div>

                  <div className="card-product-actions">
                    <p className="card-product-subtotal">
                      Rs. {(item.productPrice * item.quantity).toFixed(2)}
                    </p>
                    <button className="card-clear-btn" onClick={() => handleClearFromCart(item.productId, item.quantity)}>
                      <span><i className="bi bi-trash-fill"></i></span>
                      Clear from Cart
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};

export default Cart;
