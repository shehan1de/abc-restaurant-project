import axios from 'axios';
import React, { useEffect, useState } from 'react';
import '../CSS/Profile.css';
import SecFooter from './footer2';
import TrdNavigation from './Navigations/navigation3';

const FeedbackResponse = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [selectedFeedback, setSelectedFeedback] = useState(null);
  const [response, setResponse] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [responseStatus, setResponseStatus] = useState('');

  useEffect(() => {
    fetchFeedbacks();
  }, []);

  const fetchFeedbacks = async () => {
    try {
      const response = await axios.get('/feedback');
      setFeedbacks(response.data);
    } catch (error) {
      console.error('Error fetching feedbacks:', error);
    }
  };

  const handleFeedbackClick = (feedback) => {
    setSelectedFeedback(feedback);
    setResponse(feedback.staffResponse || '');
    setResponseStatus(''); // Clear response status
    setShowModal(true);
  };

  const handleResponseChange = (e) => {
    setResponse(e.target.value);
  };

  const handleSubmitResponse = async () => {
    if (!selectedFeedback) return;
    try {
      await axios.patch(`/feedback/${selectedFeedback.feedbackId}/response`, { response });
      fetchFeedbacks();
      setResponseStatus('Response Submitted');
      setTimeout(() => {
        setShowModal(false);
        setSelectedFeedback(null);
      }, 1000);
    } catch (error) {
      console.error('Error updating staff response:', error);
    }
  };

  return (
    <div>
      <TrdNavigation />
      <h1 className="form-head-one">
        <span>Feedback Response</span>
      </h1>
      <div className="gallery-container">
        <table className="custom-table">
          <thead>
            <tr>
              <th>Feedback ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Subject</th>
              <th>Message</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {feedbacks.map((feedback) => (
              <tr key={feedback.feedbackId}>
                <td>{feedback.feedbackId}</td>
                <td>{feedback.name}</td>
                <td>{feedback.email}</td>
                <td>{feedback.subject}</td>
                <td>{feedback.message}</td>
                <td>
                  <button onClick={() => handleFeedbackClick(feedback)} className='btn-submit'>
                    Respond
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showModal && (
        <>
          <div className="modal-backdrop-blur"></div>
          <div className="modal show" style={{ display: 'block' }}>
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Respond to Feedback</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setShowModal(false)}
                  >
                    &times;
                  </button>
                </div>
                <div className="modal-body">
                  <textarea
                    value={response}
                    onChange={handleResponseChange}
                    placeholder="Enter your response"
                    className="form-control"
                    rows="5"
                  />
                  {responseStatus && <p className="response-status">{responseStatus}</p>}
                </div>
                <div className="modal-footer">
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setShowModal(false)}
                  >
                    Close
                  </button>
                  <button
                    type="button"
                    className="btn btn-danger"
                    onClick={handleSubmitResponse}
                  >
                    Submit
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

export default FeedbackResponse;
