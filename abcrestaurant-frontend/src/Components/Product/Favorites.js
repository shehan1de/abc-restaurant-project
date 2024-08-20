import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../CSS/Card.css';
import Navigation2 from '../navigation2';

const FavoritesPage = () => {
    const [favorites, setFavorites] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchFavorites = async () => {
            try {
                const userId = localStorage.getItem('userId');
                const response = await axios.get(`/favorites?userId=${userId}`);
                setFavorites(response.data);
            } catch (error) {
                console.error('Error fetching favorite products:', error);
            }
        };

        fetchFavorites();
    }, []);

    return (
        <>
            <Navigation2 />
            <h1 className="form-head-one">Favorite Products</h1>
            {favorites.length === 0 ? (
                <div className="no-products-message">
                    <p>You have no favorite products.</p>
                </div>
            ) : (
                <div className="custom-row">
                    {favorites.map(favorite => (
                        <div className="custom-col" key={favorite.productId}>
                            <div className="custom-card">
                                <img
                                    src={`/images/${favorite.productImage}`}
                                    className="custom-card-img-top"
                                    alt={favorite.productName}
                                />
                                <div className="custom-card-body">
                                    <h5 className="custom-card-title">{favorite.productName}</h5>
                                    <p className="custom-card-text">Rs. {favorite.productPrice.toFixed(2)}</p>
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
