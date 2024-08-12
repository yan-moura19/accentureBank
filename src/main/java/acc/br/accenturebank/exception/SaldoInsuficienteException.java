package acc.br.accenturebank.exception;

public class SaldoInsuficienteException extends  RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
