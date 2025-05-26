package com.example.hiringProcess.Occupation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Optional;

@Service
public class OccupationService {
    private final OccupationRepository occupationRepository;

    @Autowired
    public OccupationService(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    @GetMapping(path="/occupations")
    public List<Occupation> getOccupations(){
        return occupationRepository.findAll();
    }

    @GetMapping(path="/occupation")
    public Optional<Occupation> getOccupation(Integer occupationId) {
        return occupationRepository.findById(occupationId);
    }

    public void addNewOccupation(Occupation occupation) {
        Optional<Occupation> occupationByName = occupationRepository.findOccupationByTitle(occupation.getTitle());
        if (occupationByName.isPresent()) {
            throw new IllegalStateException("Title already taken");
        }
        occupationRepository.save(occupation);
    }

    public void deleteOccupation(Integer occupationId) {
        boolean exists = occupationRepository.existsById(occupationId);

        if(!exists){
            throw  new IllegalStateException("Occupation with id" + occupationId + "does not exists");
        }
        occupationRepository.deleteById(occupationId);
    }
}
