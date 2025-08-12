package com.example.hiringProcess.Interview;

import com.example.hiringProcess.Interview.InterviewDetailsDTO;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Step.Step;

import java.util.List;
import java.util.stream.Collectors;

public class InterviewMapper {

    public static InterviewDetailsDTO toDetailsDTO(Interview interview) {
        if (interview == null) {
            return null;
        }

        List<InterviewDetailsDTO.StepDTO> stepDTOs = interview.getSteps()
                .stream()
                .map(step -> new InterviewDetailsDTO.StepDTO(
                        step.getId(),
                        step.getTitle() // εδώ βάζεις μόνο το title που θες
                ))
                .collect(Collectors.toList());

        return new InterviewDetailsDTO(
                interview.getId(),
                interview.getTitle(),
                interview.getDescription(),
                stepDTOs
        );
    }
}
