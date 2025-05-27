package com.example.hiringProcess.Organisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganisationService {
    private final OrganisationRepository organisationRepository;

    @Autowired
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    public List<Organisation> getOrganisations() {
        return organisationRepository.findAll();
    }

    public Optional<Organisation> getOrganisation(Integer organisationId) {
        return organisationRepository.findById(organisationId);
    }

    public void addNewOrganisation(Organisation organisation) {
        organisationRepository.save(organisation);
    }

    public void deleteOrganisation(Integer organisationId) {
        boolean exists = organisationRepository.existsById(organisationId);
        if (!exists) {
            throw new IllegalStateException("Organisation with id " + organisationId + " does not exist");
        }
        organisationRepository.deleteById(organisationId);
    }



//    @Transactional
//    public void updateOrganisation(Integer organisationId, String name) {
//        Organisation organisation = organisationRepository.findById(organisationId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Organisation with id " + organisationId + " does not exist"));
//
//        if (name != null && !name.isEmpty() && !Objects.equals(organisation.getName(), name)) {
//            organisation.setName(name);
//        }
//    }
}
