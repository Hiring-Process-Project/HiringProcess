package com.example.hiringProcess.Candidate;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class CandidateService {

    @GetMapping
    public List<Candidate> getCandidates(){
        return List.of(
                new Candidate(23,
                        "Johny")
        );
    }
}
