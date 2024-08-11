package acc.br.accenturebank.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<ValidCPF, String> {

    @Override
    public void initialize(ValidCPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || !cpf.matches("\\d+")) {
            return false;
        }

        //converter String -> lista<int>
        int[] digitosCpf = cpf.chars().map(Character::getNumericValue).toArray();

        //Verificar se os nove primeiros digitos são iguais
        boolean todosIguais = true;
        for (int i = 1; i < 9; i++) {
            if (digitosCpf[i] != digitosCpf[0]) {
                todosIguais = false;
                break;
            }
        }

        if (todosIguais) {
            return false;
        }

        //Verificar os digitos verificadores
        int soma1 = 0;
        int soma2 = 0;

        for (int j = 0; j < 9; j++) {
            soma1 += digitosCpf[j] * (10 - j);
            soma2 += digitosCpf[j] * (11 - j);
        }

        int checarDigito1 = (soma1 * 10 % 11) % 10;
        int checarDigito2 = ((soma2 + checarDigito1 * 2) * 10 % 11) % 10;

        return checarDigito1 == digitosCpf[9] && checarDigito2 == digitosCpf[10];

    }
}
