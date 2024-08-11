package acc.br.accenturebank.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Override
    public void initialize(ValidBirthDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate dataNascimento, ConstraintValidatorContext context) {

        if (dataNascimento == null) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate oldestValidDate = currentDate.minusYears(150);

        return dataNascimento.isAfter(oldestValidDate) || dataNascimento.isEqual(oldestValidDate);
    }
}
