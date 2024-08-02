package acc.br.accenturebank.controller;

import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.service.PixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pix")
public class PixController {

    @Autowired
    private PixService pixService;

    @GetMapping
    public List<Pix> getAllPix() {
        return pixService.getAllPix();
    }

    @GetMapping("/{id}")
    public Pix getPixById(@PathVariable int id) {
        return pixService.getPixById(id);
    }

    @PostMapping
    public Pix createPix(@RequestBody Pix pix) {
        return pixService.createPix(pix);
    }

    @PutMapping("/{id}")
    public Pix updatePix(@PathVariable int id, @RequestBody Pix pix) {
        return pixService.updatePix(id, pix);
    }

    @DeleteMapping("/{id}")
    public void deletePix(@PathVariable int id) {
        pixService.deletePix(id);
    }
}