import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../../CSS/Card.css';
import SecFooter from '../footer2';
import Navigation2 from '../navigation2';

const ProductView = () => {
    const { categoryName, productId } = useParams();
    const [products, setProducts] = useState([]);
    const [product, setProduct] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [cart, setCart] = useState({});
    const [favorites, setFavorites] = useState(new Set());
    const [productQuantities, setProductQuantities] = useState({});
    const [quantity, setQuantity] = useState(0);
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user ? user.userId : null;

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (categoryName) {
                    // Fetch products by category
                    const productResponse = await axios.get(`/product?categoryName=${encodeURIComponent(categoryName)}`);
                    setProducts(productResponse.data);
                }

                if (productId) {
                    // Fetch single product details
                    const productResponse = await axios.get(`/product/${productId}`);
                    setProduct(productResponse.data);
                    setQuantity(cart[productId] || 0);
                }

                if (userId) {
                    // Fetch cart details
                    const cartResponse = await axios.get(`/api/cart/details?userId=${userId}`);
                    const cartData = cartResponse.data;
                    setCart(cartData.productId || {});

                    // Fetch favorite products
                    const favoritesResponse = await axios.get(`/api/favorites/list?userId=${userId}`);
                    setFavorites(new Set(favoritesResponse.data || []));
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [categoryName, productId, userId]);

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value.toLowerCase());
    };

    const filteredProducts = products.filter(product =>
        product.productName.toLowerCase().includes(searchTerm)
    );

    const handleQuantityChange = (delta) => {
        setQuantity(prevQuantity => Math.max(prevQuantity + delta, 0));
    };

    const handleAddToCart = async () => {
        try {
            if (quantity > 0) {
                const updatedCart = {
                    ...cart,
                    [productId]: quantity
                };

                await axios.post('/api/cart/add', null, {
                    params: {
                        userId,
                        productId,
                        quantity
                    }
                });

                toast.success('Cart Updated Successfully!', {
                    position: 'top-center',
                    autoClose: 1000
                });

                setCart(updatedCart);
                localStorage.setItem('cart', JSON.stringify(updatedCart));
            } else {
                toast.warn('Quantity must be at least 1', {
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

    const toggleFavorite = async () => {
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

    if (categoryName && !productId) {
        // Display list of products in the category
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
                <SecFooter/>
            </>
        );
    }

    if (productId) {
        // Display details for a single product
        if (!product) {
            return <div>Loading...</div>; // Loading state
        }

        return (
            <>
                <Navigation2 />
                <div className="product-view-container">
                    <div className="product-view-header">
                        <span className="back-arrow" onClick={() => navigate(`/products/${categoryName}`)}>
                            <i className="bi bi-caret-left-fill"></i>
                        </span>
                        <h1>{product.productName}</h1>
                    </div>

                    <div className="product-view-content">
                        <img
                            src={`/images/${product.productImage}`}
                            className="product-view-img"
                            alt={product.productName}
                        />
                        <div className="product-view-details">
                            <p className="product-price">Rs. {product.productPrice.toFixed(2)}</p>
                            <p className="product-description">{product.productDescription}</p>

                            <div className="product-quantity-controls">
                                <button
                                    className="btn btn-outline-secondary"
                                    onClick={() => handleQuantityChange(-1)}
                                >
                                    <i className="bi bi-dash"></i>
                                </button>
                                <span className="mx-2">{quantity}</span>
                                <button
                                    className="btn btn-outline-secondary"
                                    onClick={() => handleQuantityChange(1)}
                                >
                                    <i className="bi bi-plus"></i>
                                </button>
                            </div>

                            <button
                                className="btn btn-primary mt-2"
                                onClick={handleAddToCart}
                            >
                                Add to Cart
                            </button>

                            <div className="favorite-icon" onClick={toggleFavorite}>
                                <i className={`bi ${favorites.has(productId) ? 'bi-heart-fill' : 'bi-heart'}`}></i>
                            </div>
                        </div>
                    </div>
                </div>
                <ToastContainer />

            </>
        );
    }

    return <div>Invalid route</div>;
};

export default ProductView;
