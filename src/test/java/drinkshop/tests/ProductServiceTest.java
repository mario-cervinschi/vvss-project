package drinkshop.tests;

import drinkshop.domain.*;
import drinkshop.repository.AbstractRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

    ProductService service = new ProductService(
            new AbstractRepository<>() {
                @Override
                protected Integer getId(Product entity) { return entity.getId(); }
            }
    );

    {
        service.setValidator(new ProductValidator());
    }

    @RepeatedTest(1)
    @Tag("ECP")
    @DisplayName("ECP valid - produs corect")
    void testECPValid() {
        Product p = new Product(1, "cola", 10, null, null);
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("ECP")
    @DisplayName("ECP invalid - nume gol")
    void testECPInvalidNume() {
        Product p = new Product(1, "", 10, null, null);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("ECP")
    @DisplayName("ECP invalid - pret negativ")
    void testECPInvalidPret() {
        Product p = new Product(1, "cola", -5, null, null);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA invalid - pret = -1")
    void testBVAInvalidMinusOne() {
        Product p = new Product(1, "cola", -1, null, null);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA invalid - pret = 0")
    void testBVAInvalidZero() {
        Product p = new Product(1, "cola", 0, null, null);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA valid - pret = 1")
    void testBVAValidOne() {
        Product p = new Product(1, "cola", 1, null, null);
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA valid - pret = 2")
    void testBVAValidTwo() {
        Product p = new Product(1, "cola", 2, null, null);
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @ParameterizedTest
    @Tag("BVA")
    @ValueSource(doubles = {-10, 0})
    @DisplayName("BVA invalid - preturi multiple")
    void testBVAInvalidMultiple(double pret) {
        Product p = new Product(1, "cola", pret, null, null);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }
}