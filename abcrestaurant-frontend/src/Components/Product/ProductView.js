import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../../CSS/Card.css';
import { addFavoriteProduct, removeFavoriteProduct } from '../../Services/apiService';
import Navigation2 from '../navigation2';

const ProductView = () => {
    const { categoryName } = useParams();
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [cart, setCart] = useState({});
    const [favorites, setFavorites] = useState(new Set());
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user')); // Retrieve user object from localStorage
    const userId = user ? user.userId : null; // Extract userId from user object

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get(`/product?categoryName=${encodeURIComponent(categoryName)}`);
                setProducts(response.data);

                const storedFavorites = JSON.parse(localStorage.getItem('favoriteProducts')) || [];
                setFavorites(new Set(storedFavorites));
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };

        fetchProducts();
    }, [categoryName]);

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value.toLowerCase());
    };

    const filteredProducts = products.filter(product =>
        product.productName.toLowerCase().includes(searchTerm)
    );

    const handleQuantityChange = (productId, delta) => {
        setCart(prevCart => {
            const newQuantity = Math.max((prevCart[productId]?.quantity || 0) + delta, 0);
            const updatedCart = {
                ...prevCart,
                [productId]: {
                    ...(prevCart[productId] || {}),
                    quantity: newQuantity
                }
            };
            localStorage.setItem('cart', JSON.stringify(updatedCart));
            return updatedCart;
        });
    };

    const handleAddToCart = (product) => {
        if (cart[product.productId]?.quantity > 0) {
            const newCart = {
                ...cart,
                [product.productId]: {
                    ...product,
                    quantity: (cart[product.productId]?.quantity || 0) + 1
                }
            };
            setCart(newCart);
            localStorage.setItem('cart', JSON.stringify(newCart));
        } else {
            console.log('Quantity must be greater than 0');
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
                newFavorites.delete(productId);
                await removeFavoriteProduct(userId, productId);
            } else {
                newFavorites.add(productId);
                await addFavoriteProduct(userId, productId);
            }

            localStorage.setItem('favoriteProducts', JSON.stringify([...newFavorites]));
            setFavorites(newFavorites);
        } catch (error) {
            console.error('Error toggling favorite:', error);
        }
    };

    return (
        <>
            <Navigation2 />
            <div className="search-container-one">
                <input
                    type="text"
                    className="form-control search-input"
                    placeholder="Search products..."
                    value={searchTerm}
                    onChange={handleSearchChange}
                />
            </div>

            <h1 className="form-head-one">
                <div className="back-arrow-two">
                    <span className="back-arrow-one" onClick={() => navigate('/customer-dashboard')}>
                        <i className="bi bi-caret-left-fill"></i>
                    </span>
                </div>
                <span>{categoryName}</span>
            </h1>

            {products.length === 0 ? (
                <div className="no-products-message">
                    <p>Currently, there are no products available in the {categoryName} category.</p>
                </div>
            ) : (
                <div className="custom-row">
                    {filteredProducts.map((product) => (
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

export default ProductView;
