import axios from 'axios';

const apiUrl = '/api/favorites'; // Adjust this according to your API

export const addFavoriteProduct = async (userId, productId) => {
    try {
        await axios.post(`${apiUrl}/add`, { userId, productId });
    } catch (error) {
        console.error('Error adding favorite product:', error);
        throw error;
    }
};

export const removeFavoriteProduct = async (userId, productId) => {
    try {
        await axios.post(`${apiUrl}/remove`, { userId, productId });
    } catch (error) {
        console.error('Error removing favorite product:', error);
        throw error;
    }
};
