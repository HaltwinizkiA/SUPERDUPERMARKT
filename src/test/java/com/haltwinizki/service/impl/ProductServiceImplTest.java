package com.haltwinizki.service.impl;

import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.products.Whiskey;
import com.haltwinizki.repository.impl.LocalProductRepository;
import com.haltwinizki.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
   @InjectMocks
    private static final ProductService productService = new ProductServiceImpl();
    private static Date date1;
    private static Date date2;
    @Mock
    private LocalProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        try {
            date1 = dateFormat.parse("20.07.2023");
            date2 = dateFormat.parse("01.05.2023");
        } catch (ParseException e) {
            System.out.println(e);
        }
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
        Product productWithNullField = new Wein(0, null, 0, 0, null, 0);
        when(productRepository.read(1)).thenReturn(product);
        when(productRepository.read(0)).thenReturn(productWithNullField);
        when(productRepository.read(1234)).thenReturn(null);

        assertNull(productService.read(1234));
        Product retrievedProduct0 = productService.read(0);
        Product retrievedProduct1 = productService.read(1);
        assertNotNull(retrievedProduct1);
        assertEquals(productWithNullField, retrievedProduct0);
        assertEquals(product, retrievedProduct1);
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
        List<Product> productList = new ArrayList<>();
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
        Product product4 = new Whiskey(3, "Jack Daniels", 9.66, 20, null, 29);

        product1.changeQuality();
        product2.changeQuality();
        product3.changeQuality();
        product4.changeQuality();

        assertEquals(11, product1.getQuality().get()); // Assuming qualityChange() method reduces quality by 1
        assertEquals(39, product2.getQuality().get());
        assertEquals(10, product3.getQuality().get());
        assertEquals(21, product4.getQuality().get());
    }
}