package com.example.hiringProcess.Organisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/Organisation")
public class OrganisationController {

    private final OrganisationService organisationService;

    @Autowired
    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping(path = "/organisations")
    public List<Organisation> getOrganisations() {
        return organisationService.getOrganisations();
    }

    @GetMapping(path = "/organisation")
    public Optional<Organisation> getOrganisation(Integer organisationId) {
        return organisationService.getOrganisation(organisationId);
    }

    @PostMapping(path = "/newOrganisation")
    public void addNewOrganisation(Organisation organisation) {
        organisationService.addNewOrganisation(organisation);
    }

//    @DeleteMapping(path = "{organisationId}")
//    public void deleteOrganisation(@PathVariable("organisationId") Integer id) {
//        organisationService.deleteOrganisation(id);
//    }

//    @PutMapping(path = "{organisationId}")
//    public void updateOrganisation(@PathVariable("organisationId") Integer organisationId,
//                                   @RequestParam(required = false) String name) {
//        organisationService.updateOrganisation(organisationId, name);
//    }
}
