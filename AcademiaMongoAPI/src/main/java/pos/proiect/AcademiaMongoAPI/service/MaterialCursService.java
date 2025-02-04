package pos.proiect.AcademiaMongoAPI.service;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import pos.proiect.AcademiaMongoAPI.entity.MaterialCurs;
import pos.proiect.AcademiaMongoAPI.enums.Tip;
import pos.proiect.AcademiaMongoAPI.exception.DisciplinaNotFoundException;
import pos.proiect.AcademiaMongoAPI.exception.MaterialCursAlreadyExistsException;
import pos.proiect.AcademiaMongoAPI.mapper.MaterialCursMapper;
import pos.proiect.AcademiaMongoAPI.repository.MaterialCursRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialCursService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MaterialCursRepository materialCursRepository;

    @Value("${api.mariadb.url}")
    private String URL;


    public List<MaterialCursDTO> getAll() {
        return materialCursRepository.findAll().stream()
                .map(MaterialCursMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<MaterialCursDTO> getByCodDisciplina(String codDisciplina) {
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

        return materialCursRepository.findByCodDisciplina(codDisciplina).map(MaterialCursMapper.INSTANCE::toDTO);
    }

    public MaterialCursDTO addMaterial(String codDisciplina,Tip tip, String titlu, MultipartFile multipartFile) throws IOException {
        MaterialCurs materialCurs ;
        if(materialCursRepository.existsByCodDisciplina(codDisciplina)) {
            materialCurs=materialCursRepository.findByCodDisciplina(codDisciplina).get();
            if (tip == Tip.simplu && materialCurs.getContinut() != null) {
                throw new MaterialCursAlreadyExistsException(codDisciplina);
            }
        }
        else
        {
            materialCurs = new MaterialCurs();
            materialCurs.setCodDisciplina(codDisciplina);
            materialCurs=materialCursRepository.save(materialCurs);
        }

        if(tip == Tip.simplu)
        {
            materialCurs.setContinut(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
            materialCurs.setTitlu(titlu);
            materialCurs=materialCursRepository.save(materialCurs);
        }
        else
        {
            if(materialCurs.getCapitole() ==null)
            {
                materialCurs.setCapitole(new HashMap<String,Binary>());
            }
            materialCurs.getCapitole().put(titlu,new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
            materialCurs=materialCursRepository.save(materialCurs);
        }
        return MaterialCursMapper.INSTANCE.toDTO(materialCurs);
    }

    public MaterialCursDTO updateMaterial(String codDisciplina, Tip tip, String titlu, MultipartFile multipartFile) throws IOException {
        MaterialCurs materialCurs = materialCursRepository.findByCodDisciplina(codDisciplina)
                .orElseThrow(() -> new DisciplinaNotFoundException(codDisciplina));

        if (tip == Tip.simplu) {
            materialCurs.setContinut(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
            materialCurs.setTitlu(titlu);
        } else {
            if (materialCurs.getCapitole() == null || !materialCurs.getCapitole().containsKey(titlu)) {
                throw new IllegalArgumentException("Capitolul specificat nu există: " + titlu);
            }
            materialCurs.getCapitole().put(titlu, new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
        }

        materialCurs = materialCursRepository.save(materialCurs);
        return MaterialCursMapper.INSTANCE.toDTO(materialCurs);
    }

    public void deleteMaterial(String codDisciplina, Tip tip, String titlu) {
        MaterialCurs materialCurs = materialCursRepository.findByCodDisciplina(codDisciplina)
                .orElseThrow(() -> new DisciplinaNotFoundException(codDisciplina));

        if (tip == Tip.simplu) {
            if (materialCurs.getContinut() == null) {
                throw new IllegalArgumentException("Materialul complet nu există pentru această disciplină.");
            }
            materialCurs.setContinut(null);
            materialCurs.setTitlu(null);
        } else {
            if (materialCurs.getCapitole() == null || !materialCurs.getCapitole().containsKey(titlu)) {
                throw new IllegalArgumentException("Capitolul specificat nu există: " + titlu);
            }
            materialCurs.getCapitole().remove(titlu);
        }

        materialCursRepository.save(materialCurs);
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
        Optional<MaterialCurs> materialCursOptional = materialCursRepository.findByCodDisciplina(codDisciplina);
        if (materialCursOptional.isEmpty() || materialCursOptional.get().getContinut() == null) {
            throw new IllegalArgumentException("Nu exista continut: " + codDisciplina);
        }
        return materialCursOptional.get().getContinut().getData();
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

        Optional<MaterialCurs> materialCursOptional = materialCursRepository.findByCodDisciplina(codDisciplina);
        if (materialCursOptional.isEmpty() || materialCursOptional.get().getCapitole() == null) {
            throw new IllegalArgumentException("Capitolul nu exista pentru disciplina: " + codDisciplina);
        }

        Binary chapterContent = materialCursOptional.get().getCapitole().get(titluDecodificat);
        if (chapterContent == null) {
            throw new IllegalArgumentException("Capitolul cu titlul '" + title + "' nu exista pentru disciplina: " + codDisciplina);
        }
        return chapterContent.getData();
    }
}
