// QuestionMapper.java
package com.example.hiringProcess.Question;

import com.example.hiringProcess.Skill.SkillDTO;
import com.example.hiringProcess.Skill.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    // Question -> QuestionLiteDTO (id, name)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    QuestionLiteDTO toLite(Question q);

    List<QuestionLiteDTO> toLite(List<Question> list);

    // Question -> QuestionDetailsDTO
    @Mapping(source = "id",          target = "id")
    @Mapping(source = "name",        target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "skills",      target = "skills")
    QuestionDetailsDTO toDetails(Question q);

    // Skill -> SkillDTO (αν δεν το έχεις ήδη)
    default SkillDTO toSkillDTO(Skill s) {
        if (s == null) return null;
        return new SkillDTO(s.getId(), s.getTitle());
    }

    default List<SkillDTO> toSkillDTOs(Set<Skill> set) {
        return set == null ? List.of() : set.stream()
                .map(this::toSkillDTO)
                .toList();
    }
}
