package acc.br.accenturebank.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BirthDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthDate {

    String message() default "A data de nascimento não pode ser mais antiga do que 150 anos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}