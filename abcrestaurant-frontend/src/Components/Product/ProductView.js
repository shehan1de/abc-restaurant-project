import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../../CSS/Card.css';
import Navigation2 from '../navigation2';

const ProductView = () => {
    const { categoryName } = useParams();
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [cart, setCart] = useState({});
    const [favorites, setFavorites] = useState(new Set());
    const [productQuantities, setProductQuantities] = useState({});
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user ? user.userId : null;

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch products based on category
                const productResponse = await axios.get(`/product?categoryName=${encodeURIComponent(categoryName)}`);
                setProducts(productResponse.data);

                // Fetch favorite products for the logged-in user
                const favoritesResponse = await axios.get(`/api/favorites/list?userId=${userId}`);
                setFavorites(new Set(favoritesResponse.data || []));

                // Fetch cart details for the logged-in user
                const cartResponse = await axios.get(`/api/cart/details?userId=${userId}`);
                const cartData = cartResponse.data;
                const cartItems = cartData.productId || {};
                setCart(cartItems);
                setProductQuantities(cartItems); // Initialize product quantities from cart data
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        if (userId) {
            fetchData();
        }
    }, [categoryName, userId]);

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value.toLowerCase());
    };

    const filteredProducts = products.filter(product =>
        product.productName.toLowerCase().includes(searchTerm)
    );

    const handleQuantityChange = (productId, delta) => {
        setProductQuantities(prevQuantities => {
            const currentQuantity = prevQuantities[productId] || 0;
            const newQuantity = Math.max(currentQuantity + delta, 0);
            return {
                ...prevQuantities,
                [productId]: newQuantity
            };
        });
    };

    const handleAddToCart = async (product) => {
        try {
            const quantity = productQuantities[product.productId] || 0;

            if (quantity > 0) {
                const updatedCart = {
                    ...cart,
                    [product.productId]: quantity
                };

                await axios.post('/api/cart/add', null, {
                    params: {
                        userId,
                        productId: product.productId,
                        quantity
                    }
                });

                toast.success('Product added to cart!', {
                    position: 'top-center',
                    autoClose: 1000
                });

                setCart(updatedCart);
                localStorage.setItem('cart', JSON.stringify(updatedCart));
            } else {
                toast.warn('Quantity must be greater than 0', {
                    position: 'top-center',
                    autoClose: 1000
                });
            }
        } catch (error) {
            console.error('Error adding product to cart:', error);
            toast.error('Error adding product to cart.', {
                position: 'top-center',
                autoClose: 1000
            });
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
            } else {
                await axios.post('/api/favorites/add', null, { params: { userId, productId } });
                newFavorites.add(productId);
            }

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
                                        <span className="custom-mx-2">{productQuantities[product.productId] || 0}</span>
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
            <ToastContainer />
        </>
    );
};

export default ProductView;
