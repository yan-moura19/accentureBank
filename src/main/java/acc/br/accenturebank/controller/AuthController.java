package acc.br.accenturebank.controller;


import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Cliente> login(@RequestParam String login, @RequestParam String senha) {
        return clienteService.login(login, senha)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build()); // 401 Unauthorized
    }
}
