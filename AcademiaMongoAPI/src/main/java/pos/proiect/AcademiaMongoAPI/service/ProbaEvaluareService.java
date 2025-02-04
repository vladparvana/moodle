package pos.proiect.AcademiaMongoAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pos.proiect.AcademiaMongoAPI.dto.ProbaEvaluareDTO;
import pos.proiect.AcademiaMongoAPI.entity.ProbaEvaluare;
import pos.proiect.AcademiaMongoAPI.exception.ProbaEvaluareValidationException;
import pos.proiect.AcademiaMongoAPI.mapper.ProbaEvaluareMapper;
import pos.proiect.AcademiaMongoAPI.repository.ProbaEvaluareRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProbaEvaluareService {

    @Autowired
    private ProbaEvaluareRepository probaEvaluareRepository;

    public List<ProbaEvaluareDTO> getAll() {
        return probaEvaluareRepository.findAll().stream()
                .map(ProbaEvaluareMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public List<ProbaEvaluareDTO> getByCodDisciplina(String codDisciplina) {
        return probaEvaluareRepository.findAllByCodDisciplina(codDisciplina).stream()
                .map(ProbaEvaluareMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public ProbaEvaluareDTO addProba(ProbaEvaluareDTO probaEvaluareDTO,String codDisciplina) {
        List<ProbaEvaluare> probeExistente = probaEvaluareRepository
                .findAllByCodDisciplina(codDisciplina)
                .stream()
                .collect(Collectors.toList());

        double sumaPonderi = probeExistente.stream()
                .mapToDouble(ProbaEvaluare::getPondere)
                .sum();

        if (sumaPonderi + probaEvaluareDTO.getPondere() > 100) {
            throw new ProbaEvaluareValidationException("Suma ponderilor nu poate depăși 100%");
        }

        ProbaEvaluare probaEvaluare = ProbaEvaluareMapper.INSTANCE.toEntity(probaEvaluareDTO);
        return ProbaEvaluareMapper.INSTANCE.toDto(probaEvaluareRepository.save(probaEvaluare));
    }

    public ProbaEvaluareDTO updateProba(String id,ProbaEvaluareDTO probaEvaluareDTO) {
        ProbaEvaluare probaExistenta = probaEvaluareRepository.findById(id)
                .orElseThrow(() -> new ProbaEvaluareValidationException("Proba de evaluare nu există."));

        List<ProbaEvaluare> probeExistente = probaEvaluareRepository
                .findAllByCodDisciplina(probaEvaluareDTO.getCodDisciplina())
                .stream()
                .collect(Collectors.toList());

        double sumaPonderi = probeExistente.stream()
                .mapToDouble(ProbaEvaluare::getPondere)
                .sum();

        if (sumaPonderi - probaExistenta.getPondere() + probaEvaluareDTO.getPondere() > 100) {
            throw new ProbaEvaluareValidationException("Suma ponderilor nu poate depăși 100%");
        }

        probaExistenta.setNume(probaEvaluareDTO.getNume());
        probaExistenta.setPondere(probaEvaluareDTO.getPondere());
        probaEvaluareRepository.save(probaExistenta);
        return ProbaEvaluareMapper.INSTANCE.toDto(probaExistenta);
    }

    public void deleteProba(String id) {
        ProbaEvaluare probaExistenta = probaEvaluareRepository.findById(id)
                .orElseThrow(() -> new ProbaEvaluareValidationException("Proba de evaluare nu există."));
        probaEvaluareRepository.delete(probaExistenta);
    }
}