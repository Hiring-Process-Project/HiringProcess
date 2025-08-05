package com.example.hiringProcess.Interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;

    @Autowired
    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping(path = "/interviews")
    public List<Interview> getInterviews() {
        return interviewService.getInterviews();
    }

    @GetMapping(path = "/interview")
    public Optional<Interview> getInterview(@RequestParam Integer interviewId) {
        return interviewService.getInterview(interviewId);
    }

    @PostMapping(path = "/newinterview")
    public void addNewInterview(@RequestBody Interview interview) {
        interviewService.addNewInterview(interview);
    }

}
