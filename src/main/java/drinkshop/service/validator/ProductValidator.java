package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() <= 0)
            errors += "ID invalid!\n";

        if (product.getNume() == null || product.getNume().isBlank() || product.getNume().length() < 2)
            errors += "Numele nu poate avea mai putin de 2 caractere!\n";

        if (product.getPret() <= 0)
            errors += "Pret invalid!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
