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

    private final CategorieBautura dummyCategory = null;
    private final TipBautura dummyType = null;

    @RepeatedTest(1)
    @Tag("ECP")
    @DisplayName("ECP valid - produs corect")
    void testECPValid() {
        // Arrange
        Product p = new Product(1, "cola", 10, dummyCategory, dummyType);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("ECP")
    @DisplayName("ECP invalid - nume gol")
    void testECPInvalidNume() {
        // Arrange
        Product p = new Product(1, "", 10, dummyCategory, dummyType);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("ECP")
    @DisplayName("ECP invalid - pret negativ")
    void testECPInvalidPret() {
        // Arrange
        Product p = new Product(1, "cola", -5, dummyCategory, dummyType);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA invalid - pret = -1")
    void testBVAInvalidMinusOne() {
        // Arrange
        Product p = new Product(1, "cola", -1, dummyCategory, dummyType);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA invalid - pret = 0")
    void testBVAInvalidZero() {
        // Arrange
        Product p = new Product(1, "cola", 0, dummyCategory, dummyType);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA valid - pret = 1")
    void testBVAValidOne() {
        // Arrange
        Product p = new Product(1, "cola", 1, dummyCategory, dummyType);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA valid - pret = 2")
    void testBVAValidTwo() {
        // Arrange
        Product p = new Product(1, "cola", 2, dummyCategory, dummyType);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA invalid - nume prea scurt (lungime 1)")
    void testBVAInvalidNumeLengthOne() {
        // Arrange
        Product p = new Product(1, "A", 10, dummyCategory, dummyType);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @RepeatedTest(1)
    @Tag("BVA")
    @DisplayName("BVA valid - nume minim acceptat (lungime 2)")
    void testBVAValidNumeLengthTwo() {
        // Arrange
        Product p = new Product(1, "Ab", 10, dummyCategory, dummyType);

        // Act & Assert
        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @ParameterizedTest
    @Tag("BVA")
    @ValueSource(doubles = {-10, 0})
    @DisplayName("BVA invalid - preturi multiple")
    void testBVAInvalidMultiple(double pret) {
        // Arrange
        Product p = new Product(1, "cola", pret, dummyCategory, dummyType);

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }
}