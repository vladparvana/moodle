package pos.proiect.AcademiaAPI.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.enums.GradDidactic;
import pos.proiect.AcademiaAPI.enums.TipAsociere;
import pos.proiect.AcademiaAPI.exceptions.CadruDidacticNotFound;
import pos.proiect.AcademiaAPI.mapper.CadruDidacticMapper;
import pos.proiect.AcademiaAPI.repository.CadruDidacticRepository;
import pos.proiect.AcademiaAPI.specification.CadruDidacticSpecifications;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class CadruDidacticService {

    @Autowired
    private CadruDidacticRepository cadruDidacticRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    public List<CadruDidacticDTO> getAllProfessors(String nume,String gradDidactic,String tipAsociere,String disciplina)
    {
        List<CadruDidactic> professors;

        if(nume == null && gradDidactic == null && tipAsociere == null && disciplina == null)
        {
            professors = cadruDidacticRepository.findAll();
        }
        else {
            Specification<CadruDidactic> specification = Specification.where(null);

            if(disciplina!=null)
            {
                specification=specification.and(CadruDidacticSpecifications.hasDisciplina(disciplina));
            }

            if(nume != null)
            {
                specification = specification.and(CadruDidacticSpecifications.hasNume(nume));
            }

            if(gradDidactic != null)
            {
                specification = specification.and(CadruDidacticSpecifications.hasGradDidactic(GradDidactic.valueOf(gradDidactic)));
            }
            if(tipAsociere != null)
            {
                specification = specification.and(CadruDidacticSpecifications.hasTipAsociere(TipAsociere.valueOf(tipAsociere)));
            }

            professors = cadruDidacticRepository.findAll(specification);
        }

        return professors.stream()
                .map(CadruDidacticMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CadruDidacticDTO> getProfessorById(Long id) {
        return cadruDidacticRepository.findById(id)
                .map(CadruDidacticMapper.INSTANCE::toDTO);

    }

    public CadruDidacticDTO createProfessor(CadruDidacticDTO cadruDidacticDTO) {
        CadruDidactic cadruDidactic = CadruDidacticMapper.INSTANCE.toEntity(cadruDidacticDTO);
        return CadruDidacticMapper.INSTANCE.toDTO(cadruDidacticRepository.save(cadruDidactic));
    }

    public Optional<CadruDidacticDTO> updateProfessor(Long id, CadruDidacticDTO cadruDidacticDTO) {
        Optional<CadruDidactic> cadruDidacticOptional = cadruDidacticRepository.findById(id);
        if (cadruDidacticOptional.isPresent()) {
            cadruDidacticDTO.setId(id);
            return Optional.of(CadruDidacticMapper.INSTANCE.toDTO(cadruDidacticRepository.save(CadruDidacticMapper.INSTANCE.toEntity(cadruDidacticDTO))));
        }
        throw new CadruDidacticNotFound(id);
    }

    public Optional<CadruDidacticDTO> patchProfessor(Long id, Map<String, Object> fields) throws JsonMappingException {
        Optional<CadruDidactic> professorOptional = cadruDidacticRepository.findById(id);
        if (professorOptional.isPresent()) {
            CadruDidactic professor = professorOptional.get();
            CadruDidacticDTO professorDTO= CadruDidacticMapper.INSTANCE.toDTO(professor);
            objectMapper.updateValue(professorDTO,fields);
            validateProfessor(professorDTO);
            cadruDidacticRepository.save(CadruDidacticMapper.INSTANCE.toEntity(professorDTO));
            return Optional.of(CadruDidacticMapper.INSTANCE.toDTO(professor));
        }
        throw new CadruDidacticNotFound(id);
    }

    public Optional<CadruDidacticDTO> deleteProfessor(Long id) {
        Optional<CadruDidacticDTO> professorOptional = cadruDidacticRepository.findById(id).map(CadruDidacticMapper.INSTANCE::toDTO);
        cadruDidacticRepository.deleteById(id);
        return professorOptional;
    }

    public Boolean existsProfessor(Long id) {
        return cadruDidacticRepository.existsById(id);
    }

    private void validateProfessor(@Valid CadruDidacticDTO professorDTO) {
        Set<ConstraintViolation<CadruDidacticDTO>> violations = validator.validate(professorDTO);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Transactional
    public Optional<CadruDidacticDTO> findByEmail(String email) {
        return cadruDidacticRepository.findByEmail(email).map(CadruDidacticMapper.INSTANCE::toDTO);
    }
}
