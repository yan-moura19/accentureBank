package acc.br.accenturebank.exception;

public class SenhaIncorretaException extends RuntimeException {
    public SenhaIncorretaException(String mensagem) {
        super(mensagem);
    }
}