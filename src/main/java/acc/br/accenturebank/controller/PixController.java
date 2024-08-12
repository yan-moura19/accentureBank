package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.pix.CreatePixDTO;
import acc.br.accenturebank.dto.pix.PixResponseDTO;
import acc.br.accenturebank.dto.pix.UpdatePixDTO;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.service.PixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pix")
public class PixController {

    @Autowired
    private PixService pixService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PixResponseDTO createPix(@RequestBody CreatePixDTO createPixDTO) {
        Pix pix = pixService.createPix(createPixDTO);
        return new PixResponseDTO(pix);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PixResponseDTO> getAllPix() {
        return pixService.getAllPix();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PixResponseDTO getPixById(@PathVariable int id) {
        Pix pix = pixService.getPixById(id);
        return new PixResponseDTO(pix);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PixResponseDTO updatePix(@PathVariable int id, @RequestBody UpdatePixDTO updatePixDTO) {
        Pix pix = pixService.updatePix(id, updatePixDTO);
        return new PixResponseDTO(pix);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePix(@PathVariable int id) {
        pixService.deletePix(id);
    }
}