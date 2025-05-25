package com.example.hiringProcess.InterviewReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/InterviewReport")
public class InterviewReportController {

    private final InterviewReportService interviewReportService;

    @Autowired
    public InterviewReportController(InterviewReportService interviewReportService) {
        this.interviewReportService = interviewReportService;
    }

    @GetMapping(path = "/interviewReports")
    public List<InterviewReport> getInterviewReports() {
        return interviewReportService.getInterviewReports();
    }

    @GetMapping(path = "/interviewReport")
    public Optional<InterviewReport> getInterviewReport(Integer interviewReportId) {
        return interviewReportService.getInterviewReport(interviewReportId);
    }

    @PostMapping(path = "/newInterviewReport")
    public void addNewInterviewReport(InterviewReport interviewReport) {
        interviewReportService.addNewInterviewReport(interviewReport);
    }

//    @DeleteMapping(path = "{interviewReportId}")
//    public void deleteInterviewReport(@PathVariable("interviewReportId") Integer id) {
//        interviewReportService.deleteInterviewReport(id);
//    }

//    @PutMapping(path = "{interviewReportId}")
//    public void updateInterviewReport(@PathVariable("interviewReportId") Integer interviewReportId,
//                                      @RequestParam(required = false) ...) {
//        interviewReportService.updateInterviewReport(interviewReportId, ...);
//    }
}
