package com.example.hiringProcess.Step;

// Candidates tab
public class StepQuestionsDTO {
    private int id;
    private String title;

    public StepQuestionsDTO() {}

    public StepQuestionsDTO(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
