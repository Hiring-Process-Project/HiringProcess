package com.example.hiringProcess.InterviewReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterviewReportService {
    private final InterviewReportRepository interviewReportRepository;

    @Autowired
    public InterviewReportService(InterviewReportRepository interviewReportRepository) {
        this.interviewReportRepository = interviewReportRepository;
    }

    public List<InterviewReport> getInterviewReports() {
        return interviewReportRepository.findAll();
    }

    public Optional<InterviewReport> getInterviewReport(Integer interviewReportId) {
        return interviewReportRepository.findById(interviewReportId);
    }

    public void addNewInterviewReport(InterviewReport interviewReport) {
        interviewReportRepository.save(interviewReport);
    }

    public void deleteInterviewReport(Integer interviewReportId) {
        boolean exists = interviewReportRepository.existsById(interviewReportId);
        if (!exists) {
            throw new IllegalStateException("InterviewReport with id " + interviewReportId + " does not exist");
        }
        interviewReportRepository.deleteById(interviewReportId);
    }

//    @Transactional
//    public void updateInterviewReport(Integer interviewReportId, ...) {
//        InterviewReport report = interviewReportRepository.findById(interviewReportId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "InterviewReport with id " + interviewReportId + " does not exist"));
//
//        // update logic here
//    }
}
