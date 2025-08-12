
package com.example.hiringProcess.Step;

public class StepResponseDTO {
    private int id;
    private String title;
    public StepResponseDTO() {}
    public StepResponseDTO(int id, String title) { this.id = id; this.title = title; }
    public int getId() { return id; }
    public String getTitle() { return title; }
}
