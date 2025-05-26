package com.example.hiringProcess.Occupation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
public class OccupationController {

    private final OccupationService occupationService;

    @Autowired
    public OccupationController(OccupationService occupationService) {
        this.occupationService = occupationService;
    }

    @GetMapping(path="/occupations")
    public List<Occupation> getOccupations(){
        return occupationService.getOccupations();
    }

    @GetMapping (path="/occupation")
    public Optional<Occupation> getOccupation(Integer occupationId){
        return occupationService.getOccupation(occupationId);
    }

    @PostMapping(path="/newOccupation")
    public void addNewOccupation(Occupation occupation){
        occupationService.addNewOccupation(occupation);
    }
}
