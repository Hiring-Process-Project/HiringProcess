package com.example.hiringProcess.Occupation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional
    public void updateOccupation(Integer occupationId, Occupation updatedOccupation) {
        Occupation existingOccupation = occupationRepository.findById(occupationId)
                .orElseThrow(() -> new IllegalStateException(
                        "Occupation with id " + occupationId + " does not exist"));

        if (updatedOccupation.getTitle() != null) {
            existingOccupation.setTitle(updatedOccupation.getTitle());
        }

        if (updatedOccupation.getEscoId() != null) {
            existingOccupation.setEscoId(updatedOccupation.getEscoId());
        }

        // Προσοχή: Η ενημέρωση των jobAds συνήθως γίνεται αλλού
        // Αν χρειαστεί να τα ενημερώσεις και εδώ, πρόσθεσε χειρισμό
    }

}
