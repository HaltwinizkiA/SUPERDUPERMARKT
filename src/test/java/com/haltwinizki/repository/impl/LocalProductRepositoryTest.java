package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocaleProductsBase.class})
public class LocalProductRepositoryTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static Date date;
    @InjectMocks
    private ProductRepository productRepository = new LocalProductRepository();
    private LocaleProductsBase localeProductsBaseMock;

    @Before
    public void setUp() throws ParseException {
        Mockito.framework().clearInlineMocks();
        mockStatic(LocaleProductsBase.class);
        localeProductsBaseMock = mock(LocaleProductsBase.class);
        when(LocaleProductsBase.getInstance()).thenReturn(localeProductsBaseMock);
        date = dateFormat.parse("20.07.2023");

    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(2, "Emmental", 9.66, 40, date, 0);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        when(localeProductsBaseMock.getProductsList()).thenReturn(products);
        assertEquals(products, productRepository.getAllProducts());
    }

    @Test
    public void testDelete() {
        long id = 2;
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(2, "Emmental", 9.66, 40, date, 0);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(localeProductsBaseMock.getProductsList()).thenReturn(new ArrayList<>(products));

        Product removedProduct = productRepository.delete(id);
        verify(localeProductsBaseMock,times(1)).save();
        assertNull(productRepository.delete(123213));
        assertEquals(removedProduct, product2);
    }
    @Test
    public void testUpdate() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product updatedProduct = new Wein(1, "Holandishe", 9.66, 40, null, 10);
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(localeProductsBaseMock.getProductsList()).thenReturn(new ArrayList<>(products));
        Product actualProduct = productRepository.update(updatedProduct);

        assertEquals( actualProduct,updatedProduct);
    }
    @Test
    public void testCreate() {
        Product newProduct = new Käse(0, "Emmental", 9.66, 40, date, 0);
        List<Product> products = new ArrayList<>();

        when(localeProductsBaseMock.getProductsList()).thenReturn(new ArrayList<>(products));
        when(localeProductsBaseMock.getMaxId()).thenReturn(new AtomicLong(1));

        Product actualProduct = productRepository.create(newProduct);
        verify(localeProductsBaseMock,times(1)).save();
        assertEquals(newProduct,actualProduct);
    }
    @Test
    public void testRead() {
        Product product = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(2, "Emmental", 9.66, 40, date, 0);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);

        when(localeProductsBaseMock.getProductsList()).thenReturn(new ArrayList<>(products));

        Product readProduct = productRepository.read(2);
        assertEquals(readProduct,product2);

    }
}