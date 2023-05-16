package com.haltwinizki.service.impl;

import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.service.ProductService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceImplTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final ProductService productService = new ProductServiceImpl();
    private static Date date1;
    private static Date date2;

    @BeforeAll
    public static void setUp() {
        try {
            date1 = dateFormat.parse("20.07.2023");

            date2 = dateFormat.parse("01.05.2023");
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    @BeforeEach
    public void clearBase() {
        productService.getAllProducts().clear();
    }

    @Test
    public void testCreate() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product createdProduct = productService.create(product);
        assertNotNull(createdProduct);
        assertEquals(product, createdProduct);
    }

    @Test
    public void testGet() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        productService.create(product);
        Product retrievedProduct = productService.get(1);
        assertNotNull(retrievedProduct);
        assertEquals(product, retrievedProduct);
    }

    @Test
    public void testRemove() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        productService.create(product);
        Product removedProduct = productService.remove(1);
        assertNotNull(removedProduct);
        assertNull(productService.get(1));
        assertEquals(product, removedProduct);
        assertTrue(productService.getDiscardedProducts().contains(removedProduct));
    }

    @Test
    public void testUpdate() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        productService.create(product);
        Product updatedProduct = new Wein(1, "Deutsche rot", 6, 23, null, 1);
        Product result = productService.update(updatedProduct);
        assertNotNull(result);
        assertEquals(updatedProduct, result);
    }

    @Test
    public void testCheckExpiration() {
        Product freshProduct = new Käse(1, "Emmental", 9.66, 40, date1, 0); // Expiration date 1 day in the future
        assertTrue(productService.checkExpiration(freshProduct));

        Product expiredProduct = new Käse(1, "Cabernet", 15, 29, date1, 0); // Expiration date 1 day in the past
        Product expiredProduct2 = new Käse(1, "Cabernet", 15, 70, date2, 0);
        assertFalse(productService.checkExpiration(expiredProduct));

    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(1, "Emmental", 9.66, 40, date1, 0);
        productService.create(product1);
        productService.create(product2);
        List<Product> allProducts = productService.getAllProducts();
        assertNotNull(allProducts);
        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(product2));
    }

    @Test
    public void testQualityChange() {
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(1, "Emmental", 9.66, 40, date1, 0);
        Product product3 = new Wein(1, "Deutsche rot", 5.66, 10, null, 8);

        productService.create(product1);
        productService.create(product2);

        productService.qualityChange();

        assertEquals(11, product1.getQuality()); // Assuming qualityChange() method reduces quality by 1
        assertEquals(39, product2.getQuality());
        assertEquals(10, product3.getQuality());
    }
}