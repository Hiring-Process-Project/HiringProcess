package com.example.hiringProcess.Occupation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class OccupationController {

    private final OccupationService occupationService;

    @Autowired
    public OccupationController(OccupationService occupationService) {
        this.occupationService = occupationService;
    }


    @GetMapping(path = "/occupation-titles")
    public List<OccupationDTO> getOccupationTitles() {
        return occupationService.getOccupationDTOs();
    }


    @GetMapping (path="/occupation")
    public Optional<Occupation> getOccupation(Integer occupationId){
        return occupationService.getOccupation(occupationId);
    }

    @PostMapping(path="/newOccupation")
    public void addNewOccupation(Occupation occupation){
        occupationService.addNewOccupation(occupation);
    }

    @DeleteMapping("/{occupationId}")
    public void deleteOccupation(@PathVariable Integer occupationId) {
        occupationService.deleteOccupation(occupationId);
    }

    @PutMapping("/{occupationId}")
    public void updateOccupation(@PathVariable Integer occupationId,
                                 @RequestBody Occupation updatedOccupation) {
        occupationService.updateOccupation(occupationId, updatedOccupation);
    }
}
