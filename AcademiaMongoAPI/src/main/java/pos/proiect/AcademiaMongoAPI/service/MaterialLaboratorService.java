package pos.proiect.AcademiaMongoAPI.service;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import pos.proiect.AcademiaMongoAPI.config.JwtAuthenticationToken;
import pos.proiect.AcademiaMongoAPI.dto.MaterialCursDTO;
import pos.proiect.AcademiaMongoAPI.dto.MaterialLaboratorDTO;
import pos.proiect.AcademiaMongoAPI.entity.MaterialCurs;
import pos.proiect.AcademiaMongoAPI.entity.MaterialLaborator;
import pos.proiect.AcademiaMongoAPI.enums.Tip;
import pos.proiect.AcademiaMongoAPI.exception.DisciplinaNotFoundException;
import pos.proiect.AcademiaMongoAPI.exception.MaterialCursAlreadyExistsException;
import pos.proiect.AcademiaMongoAPI.exception.MaterialLaboratorAlreadyExistsException;
import pos.proiect.AcademiaMongoAPI.mapper.MaterialCursMapper;
import pos.proiect.AcademiaMongoAPI.mapper.MaterialLaboratorMapper;
import pos.proiect.AcademiaMongoAPI.repository.MaterialLaboratorRepository;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialLaboratorService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MaterialLaboratorRepository materialLaboratorRepository;

    @Value("${api.mariadb.url}")
    private String URL;


    public List<MaterialLaboratorDTO> getAll() {
        return materialLaboratorRepository.findAll().stream()
                .map(MaterialLaboratorMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<MaterialLaboratorDTO> getByCodDisciplina(String codDisciplina) {
        String uri = URL + "/lectures" + "/" + codDisciplina;
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
            System.out.println(response.toString());
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {
            throw new DisciplinaNotFoundException(codDisciplina);
        }

        return materialLaboratorRepository.findByCodDisciplina(codDisciplina).map(MaterialLaboratorMapper.INSTANCE::toDTO);
    }

    public MaterialLaboratorDTO addMaterial(String codDisciplina, Tip tip, String titlu, MultipartFile multipartFile) throws IOException {
        MaterialLaborator materialLaborator ;
        if(materialLaboratorRepository.existsByCodDisciplina(codDisciplina)) {
            materialLaborator= materialLaboratorRepository.findByCodDisciplina(codDisciplina).get();
            if (tip == Tip.simplu && materialLaborator.getContinut() != null) {
                throw new MaterialLaboratorAlreadyExistsException(codDisciplina);
            }
        }
        else
        {
            materialLaborator = new MaterialLaborator();
            materialLaborator.setCodDisciplina(codDisciplina);
            materialLaborator= materialLaboratorRepository.save(materialLaborator);
        }

        if(tip == Tip.simplu)
        {
            materialLaborator.setContinut(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
            materialLaborator.setTitlu(titlu);
            materialLaborator= materialLaboratorRepository.save(materialLaborator);
        }
        else
        {
            if(materialLaborator.getCapitole() ==null)
            {
                materialLaborator.setCapitole(new HashMap<String,Binary>());
            }
            materialLaborator.getCapitole().put(titlu,new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
            materialLaborator= materialLaboratorRepository.save(materialLaborator);
        }
        return MaterialLaboratorMapper.INSTANCE.toDTO(materialLaborator);
    }

    public MaterialLaboratorDTO updateMaterial(String codDisciplina, Tip tip, String titlu, MultipartFile multipartFile) throws IOException {
        MaterialLaborator materialLaborator = materialLaboratorRepository.findByCodDisciplina(codDisciplina)
                .orElseThrow(() -> new DisciplinaNotFoundException(codDisciplina));

        if (tip == Tip.simplu) {
            materialLaborator.setContinut(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
            materialLaborator.setTitlu(titlu);
        } else {
            if (materialLaborator.getCapitole() == null || !materialLaborator.getCapitole().containsKey(titlu)) {
                throw new IllegalArgumentException("Capitolul specificat nu există: " + titlu);
            }
            materialLaborator.getCapitole().put(titlu, new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
        }

        materialLaborator = materialLaboratorRepository.save(materialLaborator);
        return MaterialLaboratorMapper.INSTANCE.toDTO(materialLaborator);
    }

    public void deleteMaterial(String codDisciplina, Tip tip, String titlu) {
        MaterialLaborator materialLaborator = materialLaboratorRepository.findByCodDisciplina(codDisciplina)
                .orElseThrow(() -> new DisciplinaNotFoundException(codDisciplina));

        if (tip == Tip.simplu) {
            if (materialLaborator.getContinut() == null) {
                throw new IllegalArgumentException("Materialul complet nu există pentru această disciplină.");
            }
            materialLaborator.setContinut(null);
            materialLaborator.setTitlu(null);
        } else {
            if (materialLaborator.getCapitole() == null || !materialLaborator.getCapitole().containsKey(titlu)) {
                throw new IllegalArgumentException("Capitolul specificat nu există: " + titlu);
            }
            materialLaborator.getCapitole().remove(titlu);
        }

        materialLaboratorRepository.save(materialLaborator);
    }

    public byte[] getContinut(String codDisciplina) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new RuntimeException("User not authenticated");
        }
        String token = authentication.getCredentials().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String uri = URL + "/lectures/" + codDisciplina;

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new DisciplinaNotFoundException("Disciplina " + codDisciplina + " nu a fost găsită.");
        } catch (Exception e) {
            throw new RuntimeException("Eroare de comunicare cu serviciul MariaDB: " + e.getMessage());
        }

        Optional<MaterialLaborator> materialLaboratorOptional = materialLaboratorRepository.findByCodDisciplina(codDisciplina);
        if (materialLaboratorOptional.isEmpty() || materialLaboratorOptional.get().getContinut() == null) {
            throw new IllegalArgumentException("Nu exista continut: " + codDisciplina);
        }
        return materialLaboratorOptional.get().getContinut().getData();
    }

    public byte[] getCapitolContinut(String codDisciplina, String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new RuntimeException("User not authenticated");
        }
        String token = authentication.getCredentials().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String uri = URL + "/lectures/" + codDisciplina;

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new DisciplinaNotFoundException("Disciplina " + codDisciplina + " nu a fost găsită.");
        } catch (Exception e) {
            throw new RuntimeException("Eroare de comunicare cu serviciul MariaDB: " + e.getMessage());
        }

        String titluDecodificat = URLDecoder.decode(title, StandardCharsets.UTF_8);

        Optional<MaterialLaborator> materialLaboratorOptional = materialLaboratorRepository.findByCodDisciplina(codDisciplina);
        if (materialLaboratorOptional.isEmpty() || materialLaboratorOptional.get().getCapitole() == null) {
            throw new IllegalArgumentException("Capitolul nu exista pentru disciplina: " + codDisciplina);
        }

        Binary chapterContent = materialLaboratorOptional.get().getCapitole().get(titluDecodificat);
        if (chapterContent == null) {
            throw new IllegalArgumentException("Capitolul cu titlul '" + title + "' nu exista pentru disciplina: " + codDisciplina);
        }
        return chapterContent.getData();
    }

}
