package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.pix.CreatePixDTO;
import acc.br.accenturebank.dto.pix.PixResponseDTO;
import acc.br.accenturebank.dto.pix.UpdatePixDTO;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoChavePix;
import acc.br.accenturebank.repository.PixRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static acc.br.accenturebank.model.enums.TipoChavePix.ALEATORIO;


@Service
public class PixService {

    @Autowired
    private PixRepository pixRepository;



    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^(\\+\\d{2}\\s?)?\\(?\\d{2}\\)?\\s?\\d{4,5}-\\d{4}$");
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$|^\\d{11}$");


    public Pix createPix(Pix createPixDTO) {

        TipoChavePix tipo = createPixDTO.getTipo();
        String chave = createPixDTO.getChave();
        if (!isChaveValida(tipo, chave)) {
            throw new IllegalArgumentException("Chave Pix inválida para o tipo especificado");
        }

        if (tipo == ALEATORIO) {
            chave = UUID.randomUUID().toString();
        }

        Pix pix = Pix.builder()
                .tipo(tipo)
                .chave(chave)
                .conta(createPixDTO.getConta())
                .build();

        return pixRepository.save(pix);
    }

    public Pix updatePix(int id, UpdatePixDTO updatePixDTO) {

        Pix pix = this.getPixById(id);

        TipoChavePix novoTipo = updatePixDTO.getTipo();
        String novaChave = updatePixDTO.getChave();
        Conta novaConta = updatePixDTO.getConta();

        if(novoTipo != null){
            pix.setTipo(novoTipo);
        }

        if(novaChave != null){
            pix.setChave(novaChave);
        }

        if(novaConta != null){
            pix.setConta(novaConta);
        }

        return pixRepository.save(pix);
    }

    public Pix getPixById(int id) {
        return pixRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Chave Pix com id %d não foi encontrado.".formatted(id)));
    }

    public Pix getPixByChave(String chave){
        return pixRepository.findByChave(chave).orElseThrow(() -> new ResourceNotFoundException("Chave Pix com chave %s não foi encontrado.".formatted(chave)));
    }

    public List<PixResponseDTO> getAllPix() {
        return pixRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }


    public void deletePix(int id) {

        this.getPixById(id);

        pixRepository.deleteById(id);
    }

    private PixResponseDTO converterParaDTO(Pix pix){
        return new PixResponseDTO(pix);
    }

    private boolean isChaveValida(TipoChavePix tipo, String chave) {
        return switch (tipo) {
            case EMAIL -> EMAIL_PATTERN.matcher(chave).matches();
            case TELEFONE -> TELEFONE_PATTERN.matcher(chave).matches();
            case CPF -> CPF_PATTERN.matcher(chave).matches();
            case ALEATORIO -> chave == null || chave.isEmpty();
        };
    }

}
