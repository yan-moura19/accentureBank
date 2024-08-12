package acc.br.accenturebank.dto.pix;

import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoChavePix;
import lombok.Data;

@Data
public class PixSimpleDTO {
    private int id;
    private TipoChavePix tipo;
    private String chave;

    public PixSimpleDTO(Pix pix) {
        this.id = pix.getId();
        this.tipo = pix.getTipo();
        this.chave = pix.getChave();
    }
}
