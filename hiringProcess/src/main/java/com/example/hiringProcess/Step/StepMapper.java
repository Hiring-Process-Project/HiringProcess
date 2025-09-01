package com.example.hiringProcess.Step;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StepMapper {

    // Entity -> ResponseDTO
    StepResponseDTO toResponseDTO(Step step);

    // Entity -> QuestionsDTO (για left panel / candidates tab)
    StepQuestionsDTO toQuestionsDTO(Step step);

    // DTO -> Entity (για update/patch)
    Step toEntity(StepUpdateDTO dto);
}
