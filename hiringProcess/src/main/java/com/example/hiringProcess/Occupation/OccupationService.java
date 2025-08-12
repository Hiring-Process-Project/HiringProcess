package com.example.hiringProcess.Occupation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OccupationService {

    private final OccupationRepository occupationRepository;

    @Autowired
    public OccupationService(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    public List<Occupation> getOccupations() {
        return occupationRepository.findAll();
    }

    public Optional<Occupation> getOccupation(Integer occupationId) {
        return occupationRepository.findById(occupationId);
    }

    public void addNewOccupation(Occupation occupation) {
        occupationRepository.save(occupation);
    }

    public void deleteOccupation(Integer occupationId) {
        occupationRepository.deleteById(occupationId);
    }

    public void updateOccupation(Integer occupationId, Occupation updatedOccupation) {
        updatedOccupation.setId(occupationId);
        occupationRepository.save(updatedOccupation);
    }

    public List<OccupationNameDTO> getOccupationNamesByDepartment(Integer deptId) {
        return occupationRepository.findAllByDepartmentIdViaJobAds(deptId)
                .stream()
                .map(o -> new OccupationNameDTO(o.getId(), o.getTitle()))
                .toList();
    }
}
