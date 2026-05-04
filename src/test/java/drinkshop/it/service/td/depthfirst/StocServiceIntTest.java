package drinkshop.it.service.td.depthfirst;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.StocService;
import drinkshop.service.validator.StocValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;

// S ---> R ---> E, depth-first

// Unit tests
// Step4: S, R, E, V real
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StocServiceIntTest {
    private StocValidator stocValidator;
    private Repository<Integer, Stoc> stocRepo;
    private StocService stocService;

    @BeforeEach
    void setUp() {
        // everything real
        stocValidator = new StocValidator();
        stocRepo = new FileStocRepository("data/stocuri.txt");
        stocService = new StocService(stocRepo, stocValidator);
    }

    @Test
    @Order(1)
    void testAddValid_allReal() {
        Stoc stoc = new Stoc(20, "Lapte", 4.0, 1.5);

        try {
            stocService.add(stoc);
        } catch (Exception e) {
            fail("Should not throw: " + e);
        }

        assert stocService.getAll().size() > 0;
    }

    @Test
    @Order(2)
    void testAddInvalid_allReal() {
        Stoc stoc = new Stoc(-7, "Lapte", 1.0, 8.0);

        Assertions.assertThrows(ValidationException.class, () -> {
            stocService.add(stoc);
        });
    }
}