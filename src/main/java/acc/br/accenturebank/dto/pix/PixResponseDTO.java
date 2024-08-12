package acc.br.accenturebank.dto.pix;

import acc.br.accenturebank.dto.conta.ContaSimpleDTO;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoChavePix;

public class PixResponseDTO {
    private int id;
    private TipoChavePix tipo;
    private String chave;
    private ContaSimpleDTO conta;


    public PixResponseDTO(Pix pix) {
        this.id = pix.getId();
        this.tipo = pix.getTipo();
        this.chave = pix.getChave();
        this.conta = new ContaSimpleDTO(pix.getConta());
    }
}

