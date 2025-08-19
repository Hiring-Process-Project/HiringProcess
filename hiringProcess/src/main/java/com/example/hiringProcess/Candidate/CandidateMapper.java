package com.example.hiringProcess.Candidate;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    // Entity -> List DTO (Candidates tab)
    @Mapping(source = "id",         target = "id")
    @Mapping(source = "firstName",  target = "firstName")
    @Mapping(source = "lastName",   target = "lastName")
    @Mapping(source = "email",      target = "email")
    @Mapping(source = "status",     target = "status")
    @Mapping(source = "cvPath",     target = "cvPath")
    CandidateDTO toListDto(Candidate candidate);

    // Entity -> Comment DTO (read)
    @Mapping(target = "candidateId", source = "id")
    @Mapping(target = "comments",    source = "comments")
    CandidateCommentDTO toCommentDto(Candidate candidate);

    // comments από DTO -> Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "comments", target = "comments")
    void updateCommentsFromDto(CandidateCommentDTO dto, @MappingTarget Candidate target);
}
