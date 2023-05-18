package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.repository.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocaleProductsBase.class})
public class LocalProductRepositoryTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private static Date date1;
    @InjectMocks
    private ProductRepository productRepository=new LocalProductRepository();

    private LocaleProductsBase localeProductsBaseMock;


    @Before
    public void setUp() {
        mockStatic(LocaleProductsBase.class);
        localeProductsBaseMock=mock(LocaleProductsBase.class);
        when(LocaleProductsBase.getInstance()).thenReturn(localeProductsBaseMock);
        try {
            date1 = dateFormat.parse("20.07.2023");
        } catch (ParseException e) {
            System.out.println(e);
        }
    }


    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(2, "Emmental", 9.66, 40, date1, 0);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        when(localeProductsBaseMock.getProductsList()).thenReturn(products);

        assertEquals(products,productRepository.getAllProducts());
    }
    @Test
    void delete_RemovesProductWithGivenId() {
        Product expectedProduct = new Wein(2, "Deutsche rot", 5.66, 10, null, 10);
        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
        Product product2 = new Käse(2, "Emmental", 9.66, 40, date1, 0);
        List<Product> products = new ArrayList<>();
//        when(localeProductsBase.getProductsList()).thenReturn(new ArrayList<>(List.of(new Product(1, "Product 1", 10.0), expectedProduct, new Product(3, "Product 3", 30.0))));
        when(localeProductsBaseMock.getProductsList().remove(product1)).thenReturn(product1);

        Product removedProduct = productRepository.delete(id);

        // Assert
        assertEquals(expectedProduct, removedProduct);
        verify(localeProductsBaseMock, times(1)).getProductsList();
        verify(localeProductsBaseMock, times(1)).save();
    }






//    @BeforeEach
//    public void setUp() {
//        PowerMockito.mockStatic(LocaleProductsBase.class);
//
//        try {
//            date1 = dateFormat.parse("20.07.2023");
//
//            date2 = dateFormat.parse("01.05.2023");
//        } catch (ParseException e) {
//            System.out.println(e);
//        }
//    }



//    @Test
//    void getAllProducts_ReturnsAllProducts() {
//        List<Product> products = productRepository.getAllProducts();
//        assertNotNull(products);
//        assertFalse(products.isEmpty());
//        // Arrange
//
//        when(LocaleProductsBase.getInstance()).thenReturn(localeProductsBaseMock);
//
//        Product product1 = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
//        Product product2 = new Käse(2, "Emmental", 9.66, 40, date1, 0);
//        Product product3 = new Whiskey(3, "Jack Daniels", 9.66, 20, null, 23);
//        List<Product> expectedProducts = new ArrayList<>();
//        expectedProducts.add(productRepository.create(product1));
//        expectedProducts.add(productRepository.create(product2));
//        when(localeProductsBaseMock.getProductsList()).thenReturn(expectedProducts);
//        doReturn(expectedProducts).when(localeProductsBaseMock).getProductsList();
//
//
//        // Act
//        List<Product> actualProducts = productRepository.getAllProducts();
//
//        // Assert
//        assertEquals(expectedProducts, actualProducts);
//        verify(localeProductsBaseMock, times(1)).getProductsList();
//    }
//
//    @Test
//    void delete_RemovesProductWithGivenId() {
//        // Arrange
//        long id = 2;
//        Product expectedProduct = new Wein(1, "Deutsche rot", 5.66, 10, null, 10);
//
////        when(localeProductsBase.getProductsList()).thenReturn(new ArrayList<>(List.of(new Product(1, "Product 1", 10.0), expectedProduct, new Product(3, "Product 3", 30.0))));
//
//        // Act
//        Product removedProduct = productRepository.delete(id);
//
//        // Assert
//        assertEquals(expectedProduct, removedProduct);
//        verify(localeProductsBaseMock, times(1)).getProductsList();
//        verify(localeProductsBaseMock, times(1)).save();
//    }
//
//    @Test
//    void update_UpdatesExistingProduct() {
//        // Arrange
//        long id = 1;
//        Product updatedProduct = new Wein("Deutsche rot", 5.66, 10, null, 10);
//
////        when(localeProductsBase.getProductsList()).thenReturn(new ArrayList<>(List.of(new Product(1, "Product 1", 10.0), new Product(2, "Product 2", 20.0), new Product(3, "Product 3", 30.0))));
//
//        // Act
//        Product actualProduct = productRepository.update(updatedProduct);
//
//        // Assert
//        assertEquals(updatedProduct, actualProduct);
//        assertEquals("Updated Product", actualProduct.getName());
//        assertEquals(15.0, actualProduct.getPrice());
//        verify(localeProductsBaseMock, times(1)).getProductsList();
//        verify(localeProductsBaseMock, times(1)).save();
//    }
//
//    @Test
//    void create_AddsNewProduct() {
//        // Arrange
//        Product newProduct = new Wein("Deutsche rot", 5.66, 10, null, 10);
//
//        when(localeProductsBaseMock.getMaxId().get()).thenReturn(1L);
//        when(localeProductsBaseMock.getProductsList()).thenReturn(new ArrayList<>(List.of(new Wein(1L, "Deutsche rot", 5.66, 10, null, 10),
//                new Wein("Weis", 5.66, 10, null, 10))));
//
//        // Act
//        Product actualProduct = productRepository.create(newProduct);
//
//        // Assert
//        assertEquals(newProduct, actualProduct);
//
//    }
}