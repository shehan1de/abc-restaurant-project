import axios from 'axios';
import React, { useEffect, useState } from 'react';
import '../../CSS/Cart.css'; // Import the CSS file
import SecNavigation from '../navigation2';

const Purchases = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = localStorage.getItem('token');
        const userId = token ? JSON.parse(atob(token.split('.')[1])).userId : null;

        if (userId) {
          const response = await axios.get(`/orders/user/${userId}`);
          setOrders(response.data);
        }
      } catch (error) {
        console.error('There was an error fetching the orders!', error);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const formatAmount = (amount) => {
    return amount.toFixed(2);
  };

  if (loading) return <div>Loading...</div>;

  return (
    <>
      <SecNavigation />
      <div className="purchases-container">
        {orders.map(order => (
          <div key={order._id} className="order-card">
            <div className="order-info">
              <div className='order-id'><strong>{order.orderId}</strong></div>
              <div className="order-items">
                  {order.items.map((item, index) => (
                    <span key={index}>
                      {item.quantity}  {item.productName}
                      {index < order.items.length - 1 && '  , '}
                    </span>
                  ))}
                </div>
            </div>
            <div className="order-summary">
              <div className="order-text">
                Rs. {formatAmount(order.finalAmount)}
              </div>
              <div className={`order-status ${order.orderStatus.toLowerCase()}`}>
                {order.orderStatus}
              </div>
            </div>
          </div>
        ))}
      </div>
    </>
  );
};

export default Purchases;