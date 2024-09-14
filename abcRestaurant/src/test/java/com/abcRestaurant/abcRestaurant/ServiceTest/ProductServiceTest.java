package com.abcRestaurant.abcRestaurant.ServiceTest;

import com.abcRestaurant.abcRestaurant.Exception.ResourceNotFoundException;
import com.abcRestaurant.abcRestaurant.Model.Product;
import com.abcRestaurant.abcRestaurant.Repository.ProductRepository;
import com.abcRestaurant.abcRestaurant.Service.ProductService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

  /*  @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllProducts() {

        Product product1 = new Product();
        product1.setId(new ObjectId("64e1d2b2a34c3e3c1c40b5f1"));
        product1.setProductId("prod-001");
        product1.setProductName("Product 1");
        product1.setCategoryName("Category A");
        product1.setProductPrice(19.99f);
        product1.setProductImage("image1.jpg");
        product1.setProductDescription("Description for Product 1");

        Product product2 = new Product();
        product2.setId(new ObjectId("64e1d2b2a34c3e3c1c40b5f2"));
        product2.setProductId("prod-002");
        product2.setProductName("Product 2");
        product2.setCategoryName("Category B");
        product2.setProductPrice(29.99f);
        product2.setProductImage("image2.jpg");
        product2.setProductDescription("Description for Product 2");

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.allProduct();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getProductName());
        assertEquals("Product 2", products.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testSingleProduct_Success() {

        ObjectId id = new ObjectId("64e1d2b2a34c3e3c1c40b5f1");
        Product product = new Product();
        product.setId(id);
        product.setProductId("prod-001");
        product.setProductName("Product 1");
        product.setCategoryName("Category A");
        product.setProductPrice(19.99f);
        product.setProductImage("image1.jpg");
        product.setProductDescription("Description for Product 1");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.singleProduct(id);

        assertNotNull(foundProduct);
        assertEquals(id, foundProduct.get().getProductId());
        assertEquals("Product 1", foundProduct.get().getProductName());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testSingleProduct_NotFound() {

        ObjectId id = new ObjectId("64e1d2b2a34c3e3c1c40b5f1");
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.singleProduct(id);
        });

        assertEquals("Product not found with id " + id, exception.getMessage());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testAddProduct() {

        Product product = new Product();
        product.setId(new ObjectId("64e1d2b2a34c3e3c1c40b5f3"));
        product.setProductId("prod-003");
        product.setProductName("Product 3");
        product.setCategoryName("Category C");
        product.setProductPrice(39.99f);
        product.setProductImage("image3.jpg");
        product.setProductDescription("Description for Product 3");

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("prod-003", savedProduct.getProductId());
        assertEquals("Product 3", savedProduct.getProductName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {

        ObjectId id = new ObjectId("64e1d2b2a34c3e3c1c40b5f1");
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setProductName("Old Name");

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setProductName("New Name");

        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);


        Product result = productService.updateProduct(id, );

        // Verify
        assertNotNull(result);
        assertEquals("New Name", result.getProductName());
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testUpdateProduct_NotFound() {
        // Setup
        ObjectId id = new ObjectId("64e1d2b2a34c3e3c1c40b5f1");
        Product updatedProduct = new Product();
        updatedProduct.setId(id);

        when(productRepository.existsById(id)).thenReturn(false);

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(id, updatedProduct);
        });

        assertEquals("Product not found with id " + id, exception.getMessage());
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(0)).save(updatedProduct);
    }

    @Test
    void testDeleteProduct_Success() {
        // Setup
        ObjectId id = new ObjectId("64e1d2b2a34c3e3c1c40b5f1");
        when(productRepository.existsById(id)).thenReturn(true);

        // Execute
        productService.deleteProduct(id);

        // Verify
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Setup
        ObjectId id = new ObjectId("64e1d2b2a34c3e3c1c40b5f1");
        when(productRepository.existsById(id)).thenReturn(false);

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(String.valueOf(id));
        });

        assertEquals("Product not found with id " + id, exception.getMessage());
        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(0)).deleteById(id);
    }*/
}
