package drinkshop.tests;

import drinkshop.domain.*;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StocServiceTest {

    StocService service;
    Repository<Integer, Stoc> dummyStocRepo;

    @BeforeEach
    void setUp() {
        dummyStocRepo = new AbstractRepository<>() {
            @Override protected Integer getId(Stoc entity) { return entity.getId(); }
        };

        service = new StocService(dummyStocRepo);
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC01: reteta == null")
    void testAreSuficient_RetetaNull() {
        // Arrange
        Reteta reteta = null;

        // Act & Assert
        assertFalse(service.areSuficient(reteta), "Trebuie sa returneze false cand reteta e null");
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC02: reteta cu lista goala")
    void testAreSuficient_ListaGoala() {
        // Arrange
        Reteta reteta = new Reteta(1, new ArrayList<>());

        // Act & Assert
        assertTrue(service.areSuficient(reteta), "Trebuie sa returneze true pentru lista goala");
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC03: ingrediente cantitate <= 0")
    void testAreSuficient_CantitateZero() {
        // Arrange
        Reteta reteta = new Reteta(1, new ArrayList<>());
        List<IngredientReteta> ingrediente = new ArrayList<>();

        IngredientReteta ingredient = new IngredientReteta("Ingredient test", 0);
        ingredient.setCantitate(0);
        ingrediente.add(ingredient);

        reteta.setIngrediente(ingrediente);

        // Act & Assert
        assertTrue(service.areSuficient(reteta), "Trebuie sa sara peste cu continue si sa returneze true la final");
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC04: disponibil == 0")
    void testAreSuficient_DisponibilZero() {
        // Arrange
        Reteta reteta = new Reteta(1, new ArrayList<>());
        List<IngredientReteta> ingrediente = new ArrayList<>();

        IngredientReteta ingredient = new IngredientReteta("Ingredient test", 0);
        ingredient.setCantitate(5);
        ingrediente.add(ingredient);
        reteta.setIngrediente(ingrediente);

        // Act & Assert
        assertFalse(service.areSuficient(reteta), "Trebuie sa returneze false cand stocul e 0");
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC05: stoc < necesar")
    void testAreSuficient_StocInsuficient() {
        // Arrange
        Reteta reteta = new Reteta(1, new ArrayList<>());
        List<IngredientReteta> ingrediente = new ArrayList<>();

        IngredientReteta ingredient = new IngredientReteta("Ingredient test", 0);
        ingredient.setCantitate(10);
        ingrediente.add(ingredient);
        reteta.setIngrediente(ingrediente);

        dummyStocRepo.save(new Stoc(1, "Ingredient test", 5, 2));

        // Act & Assert
        assertFalse(service.areSuficient(reteta), "Trebuie sa returneze false cand 5 < 10");
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC06: tot valid")
    void testAreSuficient_ValidUnIngredient() {
        // Arrange
        Reteta reteta = new Reteta(1, new ArrayList<>());
        List<IngredientReteta> ingrediente = new ArrayList<>();

        IngredientReteta ingredient = new IngredientReteta("Ingredient test", 0);
        ingredient.setCantitate(5);
        ingrediente.add(ingredient);
        reteta.setIngrediente(ingrediente);

        dummyStocRepo.save(new Stoc(1, "Ingredient test", 20, 2));

        // Act & Assert
        assertTrue(service.areSuficient(reteta), "Trebuie sa returneze true, stocul este suficient");
    }

    @Test
    @Tag("WBT")
    @DisplayName("F02_TC07: mai multe ingrediente")
    void testAreSuficient_ValidMultipleIngrediente() {
        // Arrange
        Reteta reteta = new Reteta(1, new ArrayList<>());
        List<IngredientReteta> ingrediente = new ArrayList<>();

        IngredientReteta ing1 = new IngredientReteta("Ingredient test1", 0);
        ing1.setCantitate(5);

        IngredientReteta ing2 = new IngredientReteta("Ingredient test2", 0);
        ing2.setCantitate(3);

        ingrediente.add(ing1);
        ingrediente.add(ing2);
        reteta.setIngrediente(ingrediente);

        dummyStocRepo.save(new Stoc(1, "Ingredient test1", 10, 2));
        dummyStocRepo.save(new Stoc(2, "Ingredient test2", 10, 2));

        // Act & Assert
        assertTrue(service.areSuficient(reteta), "Trebuie sa returneze true la finalul buclei for");
    }
}