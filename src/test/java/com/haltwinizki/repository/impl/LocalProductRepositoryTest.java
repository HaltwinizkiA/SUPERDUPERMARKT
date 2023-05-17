package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.products.Whiskey;
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
import static org.mockito.Mockito.*;

class LocalProductRepositoryTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static Date date1;
    private static Date date2;
    @InjectMocks
    private LocalProductRepository productRepository = new LocalProductRepository();
    @Mock
    private LocaleProductsBase localeProductsBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            date1 = dateFormat.parse("20.07.2023");

            date2 = dateFormat.parse("01.05.2023");
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    @Test
    void getAllProducts_ReturnsAllProducts() {
        // Arrange
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Wein(1, "Deutsche rot", 5.66, 10, null, 10));
        expectedProducts.add(new Käse(2, "Emmental", 9.66, 40, date1, 0));
        expectedProducts.add(new Whiskey(3, "Jack Daniels", 9.66, 20, null, 23));

        when(localeProductsBase.getProductsList()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productRepository.getAllProducts();

        // Assert
        assertEquals(expectedProducts, actualProducts);
        verify(localeProductsBase, times(1)).getProductsList();
    }

    @Test
    void delete_RemovesProductWithGivenId() {
        // Arrange
        long id = 2;
        Product expectedProduct = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);

//        when(localeProductsBase.getProductsList()).thenReturn(new ArrayList<>(List.of(new Product(1, "Product 1", 10.0), expectedProduct, new Product(3, "Product 3", 30.0))));

        // Act
        Product removedProduct = productRepository.remove(id);

        // Assert
        assertEquals(expectedProduct, removedProduct);
        verify(localeProductsBase, times(1)).getProductsList();
        verify(localeProductsBase, times(1)).save();
    }

    @Test
    void update_UpdatesExistingProduct() {
        // Arrange
        long id = 1;
        Product updatedProduct = new Wein("Deutsche rot", 5.66, 10, null, 10);

//        when(localeProductsBase.getProductsList()).thenReturn(new ArrayList<>(List.of(new Product(1, "Product 1", 10.0), new Product(2, "Product 2", 20.0), new Product(3, "Product 3", 30.0))));

        // Act
        Product actualProduct = productRepository.update(updatedProduct);

        // Assert
        assertEquals(updatedProduct, actualProduct);
        assertEquals("Updated Product", actualProduct.getName());
        assertEquals(15.0, actualProduct.getPrice());
        verify(localeProductsBase, times(1)).getProductsList();
        verify(localeProductsBase, times(1)).save();
    }

    @Test
    void create_AddsNewProduct() {
        // Arrange
        Product newProduct = new Wein("Deutsche rot", 5.66, 10, null, 10);

        when(localeProductsBase.getMaxId().get()).thenReturn(1L);
        when(localeProductsBase.getProductsList()).thenReturn(new ArrayList<>(List.of(new Wein(1L, "Deutsche rot", 5.66, 10, null, 10),
                new Wein("Weis", 5.66, 10, null, 10))));

        // Act
        Product actualProduct = productRepository.create(newProduct);

        // Assert
        assertEquals(newProduct, actualProduct);

    }
}