package com.example.hiringProcess.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skill> getSkill(Integer skillId) {
        return skillRepository.findById(skillId);
    }

    public void addNewSkill(Skill skill) {
        skillRepository.save(skill);
    }

    public void deleteSkill(Integer skillId) {
        boolean exists = skillRepository.existsById(skillId);
        if (!exists) {
            throw new IllegalStateException("Skill with id " + skillId + " does not exist");
        }
        skillRepository.deleteById(skillId);
    }

//    @Transactional
//    public void updateSkill(Integer skillId, ...) {
//        Skill skill = skillRepository.findById(skillId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Skill with id " + skillId + " does not exist"));
//        // Ενημέρωση πεδίων εδώ
//    }
}
