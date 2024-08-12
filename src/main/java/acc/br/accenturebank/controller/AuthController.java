package acc.br.accenturebank.controller;


import acc.br.accenturebank.exception.ClienteNaoEncontradoException;
import acc.br.accenturebank.exception.SenhaIncorretaException;
import acc.br.accenturebank.exception.response.ErroResponse;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String login, @RequestParam String senha) {
        try {
            Cliente cliente = clienteService.login(login, senha);
            return ResponseEntity.ok(cliente);
        } catch (ClienteNaoEncontradoException e) {
            ErroResponse erroResponse = new ErroResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse); // 404 Not Found
        } catch (SenhaIncorretaException e) {
            ErroResponse erroResponse = new ErroResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erroResponse); // 401 Unauthorized
        }
    }
}
