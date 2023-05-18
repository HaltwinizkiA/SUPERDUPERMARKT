package com.haltwinizki.service.impl;

import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.products.Whiskey;
import com.haltwinizki.repository.impl.LocalProductRepository;
import com.haltwinizki.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    @InjectMocks
    private static final ProductService productService = new ProductServiceImpl();
    private static Date date1;
    private static Date date2;
    @Mock
    private LocalProductRepository productRepository;

    @BeforeEach
    public  void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            date1 = dateFormat.parse("20.07.2023");

            date2 = dateFormat.parse("01.05.2023");
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    @BeforeEach
    public void clearBase() {
    }

    @Test
    public void testCreate() {

        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        when(productRepository.create(product)).thenReturn(product);
        Product createdProduct = productService.create(product);
        assertNotNull(createdProduct);
        assertEquals(product, createdProduct);
    }

    @Test
    public void testGet() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        when(productRepository.read(1)).thenReturn(product);
        productService.create(product);
        Product retrievedProduct = productService.read(1);
        assertNotNull(retrievedProduct);
        assertEquals(product, retrievedProduct);
    }

    @Test
    public void testDelete() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        when(productRepository.create(product)).thenReturn(product);
        productService.create(product);
        when(productRepository.delete(1)).thenReturn(product);
        when(productRepository.delete(1)).thenReturn(product);
        Product removedProduct = productService.delete(1);
        assertNotNull(removedProduct);
        assertNull(productService.read(1));
        assertEquals(product, removedProduct);
//        assertTrue(productService.getDiscardedProducts().add().contains(removedProduct));
    }
    @Test
    public void testUpdate() {

        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        when(productRepository.create(product)).thenReturn(product);
        productService.create(product);
        Product updatedProduct = new Wein(1, "Deutsche rot", 6, 23, null, 1);
        when(productRepository.update(updatedProduct)).thenReturn(updatedProduct);
        Product result = productService.update(updatedProduct);
        assertNotNull(result);
        assertEquals(updatedProduct, result);
        assertEquals(updatedProduct, result);
        assertEquals(product.getId(), result.getId());

    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(1, "Emmental", 9.66, 40, date1, 0);
        when(productRepository.create(product1)).thenReturn(product1);
        when(productRepository.create(product2)).thenReturn(product2);
        List<Product> productList=new ArrayList<>();
        productList.add(productService.create(product1));
        productList.add(productService.create(product2));
        when(productRepository.getAllProducts()).thenReturn(productList);
        List<Product> allProducts = productService.getAllProducts();
        assertNotNull(allProducts);
        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(product2));
    }

    @Test
    public void testQualityChange() {
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 9);
        Product product2 = new Käse(2, "Emmental", 9.66, 40, date1, 0);
        Product product3 = new Wein(3, "Deutsche rot", 5.66, 10, null, 8);
        Product product4=new Whiskey(3, "Jack Daniels", 9.66, 20, null, 24);

        product1.changeQuality();
        product2.changeQuality();
        product3.changeQuality();
        assertEquals(11, product1.getQuality().get()); // Assuming qualityChange() method reduces quality by 1
        assertEquals(39, product2.getQuality().get());
        assertEquals(10, product3.getQuality().get());
        assertEquals(0, product4.getQuality().get());
    }
}