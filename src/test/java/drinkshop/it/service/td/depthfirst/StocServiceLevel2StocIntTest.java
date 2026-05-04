package drinkshop.it.service.td.depthfirst;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.StocService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

// S ---> R ---> E, depth-first

// Unit tests
// Step3: S, R, E real, V mock
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StocServiceLevel2StocIntTest {
    private Validator<Stoc> stocValidator;
    private Repository<Integer, Stoc> stocRepo;
    private StocService stocService;

    @BeforeEach
    void setUp() {
        stocValidator = mock(Validator.class); // V - mocked
        stocRepo = new FileStocRepository("data/stocuri.txt"); // R - real
        // E - real
        stocService = new StocService(stocRepo, stocValidator);
    }

    @Test
    @Order(1)
    void testAddValid_realRepoAndStoc() {
        Stoc stoc = new Stoc(12, "Cafea", 3.0, 1.0);
        doNothing().when(stocValidator).validate(stoc);

        try {
            stocService.add(stoc);
        } catch (Exception e) {
            fail("Should not throw: " + e);
        }

        verify(stocValidator, times(1)).validate(stoc);
    }

    @Test
    @Order(2)
    void testAddInvalid_realRepoAndStoc() {
        Stoc stoc = new Stoc(-2, "", 0.0, 5.0);
        doThrow(new ValidationException("ID invalid!\n")).when(stocValidator).validate(stoc);

        Assertions.assertThrows(ValidationException.class, () -> {
            stocService.add(stoc);
        });

        verify(stocValidator, times(1)).validate(stoc);
    }
}