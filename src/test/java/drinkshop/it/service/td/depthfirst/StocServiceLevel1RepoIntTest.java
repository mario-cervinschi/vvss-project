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
// Step2: S, R real, V E mock
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StocServiceLevel1RepoIntTest {
    private Stoc stoc;
    private Validator<Stoc> stocValidator;
    private Repository<Integer, Stoc> stocRepo;
    private StocService stocService;

    @BeforeEach
    void setUp() {
        stoc = mock(Stoc.class); // E - mocked
        stocValidator = mock(Validator.class); // V - mocked
        stocRepo = new FileStocRepository("data/stocuri.txt"); // R - real
        stocService = new StocService(stocRepo, stocValidator);
    }

    @Test
    @Order(1)
    void testAddValid_realRepo() {
        when(stoc.getId()).thenReturn(7);
        when(stoc.getIngredient()).thenReturn("Zahar");
        when(stoc.getCantitate()).thenReturn(2.5);
        when(stoc.getStocMinim()).thenReturn(0.5);
        doNothing().when(stocValidator).validate(stoc);

        try {
            stocService.add(stoc);
        } catch (Exception e) {
            fail("Should not throw: " + e);
        }

        verify(stocValidator, times(1)).validate(stoc);
        verify(stoc, atLeastOnce()).getId();
    }

    @Test
    @Order(2)
    void testAddInvalid_realRepo() {
        when(stoc.getId()).thenReturn(-5);
        doThrow(new ValidationException("ID invalid!\n")).when(stocValidator).validate(stoc);

        Assertions.assertThrows(ValidationException.class, () -> {
            stocService.add(stoc);
        });

        verify(stocValidator, times(1)).validate(stoc);
    }
}
