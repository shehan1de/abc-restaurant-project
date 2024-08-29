import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // Fixed import for jwt-decode
import React, { useEffect, useState } from 'react';
import '../../CSS/Profile.css';
import SecFooter from '../footer2';
import TrdNavigation from '../navigation3';

const OrderStaff = () => {
  const [orders, setOrders] = useState([]);
  const [userBranch, setUserBranch] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [showProductsModal, setShowProductsModal] = useState(false);
  const [currentOrderId, setCurrentOrderId] = useState(null);
  const [statusToUpdate, setStatusToUpdate] = useState('');
  const [currentOrder, setCurrentOrder] = useState(null);

  useEffect(() => {
    const fetchUserBranchAndOrders = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) throw new Error('No token found');

        const decoded = jwtDecode(token);
        console.log('Decoded token:', decoded);

        const userResponse = await axios.get(`/user/${decoded.userId}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        console.log('User response:', userResponse.data);
        const branch = userResponse.data.branch.trim();
        setUserBranch(branch);

        const ordersResponse = await axios.get('/orders', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        console.log('Orders response:', ordersResponse.data);

        const filteredOrders = ordersResponse.data.filter(
          (order) => order.branch.trim() === branch
        );
        console.log('Filtered orders:', filteredOrders);

        const sortedOrders = filteredOrders.sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate));

        setOrders(sortedOrders);
      } catch (error) {
        console.error('Error fetching user branch or orders:', error);
      }
    };

    fetchUserBranchAndOrders();
  }, []);

  const handleOrderClick = (order) => {
    setCurrentOrder(order);
    setShowProductsModal(true);
  };

  const handleStatusChange = (orderId, status) => {
    setCurrentOrderId(orderId);
    setStatusToUpdate(status);
    setShowModal(true);
  };

  const updateOrderStatus = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) throw new Error('No token found');

      const response = await axios.put(`/orders/${currentOrderId}/status`, null, {
        params: { orderStatus: statusToUpdate },
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      console.log('Update response:', response.data);

      setOrders((prevOrders) =>
        prevOrders
          .map((order) =>
            order.orderId === currentOrderId
              ? { ...order, orderStatus: statusToUpdate }
              : order
          )
          .sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
      );
      setShowModal(false);
    } catch (error) {
      console.error(`Error updating order status to ${statusToUpdate}:`, error);
    }
  };

  return (
    <div>
      <TrdNavigation />
      <h1 className="form-head-one">
        <span>{userBranch} Orders</span>
      </h1>
      <div className="gallery-container">
        <div className="gallery-content">
          <p className='sub-par'>Note - Click on the Order ID to view the products included in the order.</p>
          <table className="custom-table">
            <thead>
              <tr>
                <th>Order ID</th>
                <th>User Email</th>
                <th>Order Date</th>
                <th>Payment Method</th>
                <th>Delivery Address</th>
                <th>Offer ID</th>
                <th>Tax Amount</th>
                <th>Delivery Charges</th>
                <th>Discount Amount</th>
                <th>Final Amount</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {orders.length > 0 ? (
                orders.map((order) => (
                  <tr key={order.orderId}>
                    <td
                      onClick={() => handleOrderClick(order)}
                      style={{ cursor: 'pointer', color: 'blue' }}
                    >
                      {order.orderId}
                    </td>
                    <td>{order.userEmail}</td>
                    <td>{new Date(order.orderDate).toLocaleString()}</td>
                    <td>{order.paymentMethod}</td>
                    <td>{order.deliveryAddress}</td>
                    <td>{order.offerId}</td>
                    <td>Rs. {order.taxAmount.toFixed(2)}</td>
                    <td>Rs. {order.deliveryCharges.toFixed(2)}</td>
                    <td>Rs. {order.discountAmount.toFixed(2)}</td>
                    <td>Rs. {order.finalAmount.toFixed(2)}</td>
                    <td>
                      {order.orderStatus === 'Pending' ? (
                        <>
                          <button
                            className="btn-confirm"
                            onClick={() => handleStatusChange(order.orderId, 'Accepted')}
                          >
                            Confirm
                          </button>
                          <button
                            className="btn-deny"
                            onClick={() => handleStatusChange(order.orderId, 'Denied')}
                          >
                            Deny
                          </button>
                        </>
                      ) : order.orderStatus === 'Accepted' ? (
                        <i className="bi bi-check-circle-fill text-success" title="Accepted"></i>
                      ) : order.orderStatus === 'Denied' ? (
                        <i className="bi bi-x-circle-fill text-danger" title="Denied"></i>
                      ) : null}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="12" className="text-center">
                    No orders found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {showModal && (
        <>
          <div className="modal-backdrop-blur"></div>

          <div className="modal show" style={{ display: 'block' }}>
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Order Status Confirmation</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setShowModal(false)}
                  >
                    &times;
                  </button>
                </div>
                <div className="modal-body">
                  <p>Are you sure you want to {statusToUpdate} this order?</p>
                </div>
                <div className="modal-footer">
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setShowModal(false)}
                  >
                    No
                  </button>
                  <button
                    type="button"
                    className="btn btn-danger"
                    onClick={updateOrderStatus}
                  >
                    Yes
                  </button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}

      {showProductsModal && currentOrder && (
        <>
          <div className="modal-backdrop-blur"></div>
          
          <div className="modal show" style={{ display: 'block' }}>
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Order Products - {currentOrder.orderId} </h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setShowProductsModal(false)}
                  >
                    &times;
                  </button>
                </div>
                <div className="modal-body">
                  <table className="custom-table">
                    <thead>
                      <tr>
                        <th>Product Name</th>
                        <th>Quantity</th>
                      </tr>
                    </thead>
                    <tbody>
                      {currentOrder.items.length > 0 ? (
                        currentOrder.items.map((item) => (
                          <tr key={item.productId}>
                            <td>{item.productName}</td>
                            <td>{item.quantity}</td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan="2" className="text-center">
                            No products found for this order.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
                <div className="modal-footer">
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setShowProductsModal(false)}
                  >
                    Close
                  </button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}

      <SecFooter />
    </div>
  );
};

export default OrderStaff;
