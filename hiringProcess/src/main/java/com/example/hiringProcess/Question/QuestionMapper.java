// QuestionMapper.java
package com.example.hiringProcess.Question;

import com.example.hiringProcess.Skill.SkillDTO;
import com.example.hiringProcess.Skill.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    // Question -> QuestionLiteDTO (id, name)   |  entity.title -> dto.name
    @Mapping(source = "id",    target = "id")
    @Mapping(source = "title", target = "name")
    QuestionLiteDTO toLite(Question q);

    List<QuestionLiteDTO> toLite(List<Question> list);

    // Question -> QuestionDetailsDTO
    // entity.title -> dto.name, description passthrough,
    // skills: Set<Skill> -> List<SkillDTO> (βοηθάει το default toSkillDTOs)
    @Mapping(source = "id",          target = "id")
    @Mapping(source = "title",       target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "skills",      target = "skills")
    QuestionDetailsDTO toDetails(Question q);

    // ---- Helpers for Skill mapping ----
    default SkillDTO toSkillDTO(Skill s) {
        if (s == null) return null;
        return new SkillDTO(s.getId(), s.getTitle()); // entity έχει 'title'
    }

    default List<SkillDTO> toSkillDTOs(Set<Skill> set) {
        return set == null ? List.of() : set.stream()
                .map(this::toSkillDTO)
                .toList();
    }

    // (Προαιρετικό) DTO για updates, αν το χρησιμοποιείς:
    // void update(@MappingTarget Question q, QuestionUpdateDTO dto);
}
