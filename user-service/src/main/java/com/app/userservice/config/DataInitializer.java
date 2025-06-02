package com.app.userservice.config;

import com.app.userservice.model.InterestTag;
import com.app.userservice.model.SkillTag;
import com.app.userservice.repository.InterestTagRepository;
import com.app.userservice.repository.SkillTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Classe d'initialisation des données pour les tags prédéfinis.
 * Cette classe s'exécute au démarrage de l'application pour précharger
 * les compétences et intérêts prédéfinis dans la base de données.
 */
@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Crée un CommandLineRunner qui initialise la base de données au démarrage.
     * Les données ne sont insérées que si les collections sont vides.
     * 
     * @param skillTagRepository Repository pour les compétences
     * @param interestTagRepository Repository pour les intérêts
     * @return Un CommandLineRunner pour l'exécution au démarrage
     */
    @Bean
    public CommandLineRunner initDatabase(SkillTagRepository skillTagRepository,
                                          InterestTagRepository interestTagRepository) {
        return args -> {
            // Vérifier si des données existent déjà
            if (skillTagRepository.count() == 0) {
                logger.info("Initialisation des compétences prédéfinies");
                initializeSkills(skillTagRepository);
            } else {
                logger.info("Les compétences sont déjà initialisées, ignoré");
            }
            
            if (interestTagRepository.count() == 0) {
                logger.info("Initialisation des intérêts prédéfinis");
                initializeInterests(interestTagRepository);
            } else {
                logger.info("Les intérêts sont déjà initialisés, ignoré");
            }
        };
    }
    
    /**
     * Initialise les compétences prédéfinies par catégorie.
     * 
     * @param repository Repository pour les compétences
     */
    private void initializeSkills(SkillTagRepository repository) {
        // Catégorie Programmation
        List<SkillTag> programmingSkills = Arrays.asList(
            new SkillTag(null, "Java", "Programmation", true),
            new SkillTag(null, "Python", "Programmation", true),
            new SkillTag(null, "JavaScript", "Programmation", true),
            new SkillTag(null, "TypeScript", "Programmation", true),
            new SkillTag(null, "C#", "Programmation", true),
            new SkillTag(null, "C++", "Programmation", true),
            new SkillTag(null, "PHP", "Programmation", true),
            new SkillTag(null, "Ruby", "Programmation", true),
            new SkillTag(null, "Swift", "Programmation", true),
            new SkillTag(null, "Kotlin", "Programmation", true)
        );
        
        // Catégorie Web
        List<SkillTag> webSkills = Arrays.asList(
            new SkillTag(null, "HTML", "Web", true),
            new SkillTag(null, "CSS", "Web", true),
            new SkillTag(null, "React", "Web", true),
            new SkillTag(null, "Angular", "Web", true),
            new SkillTag(null, "Vue.js", "Web", true),
            new SkillTag(null, "Spring Boot", "Web", true),
            new SkillTag(null, "Node.js", "Web", true),
            new SkillTag(null, "Express", "Web", true),
            new SkillTag(null, "Django", "Web", true),
            new SkillTag(null, "Laravel", "Web", true)
        );
        
        // Catégorie Design
        List<SkillTag> designSkills = Arrays.asList(
            new SkillTag(null, "UI Design", "Design", true),
            new SkillTag(null, "UX Design", "Design", true),
            new SkillTag(null, "Graphic Design", "Design", true),
            new SkillTag(null, "Figma", "Design", true),
            new SkillTag(null, "Adobe XD", "Design", true),
            new SkillTag(null, "Adobe Illustrator", "Design", true),
            new SkillTag(null, "Adobe Photoshop", "Design", true),
            new SkillTag(null, "Sketch", "Design", true)
        );
        
        // Catégorie Data
        List<SkillTag> dataSkills = Arrays.asList(
            new SkillTag(null, "SQL", "Data", true),
            new SkillTag(null, "MongoDB", "Data", true),
            new SkillTag(null, "PostgreSQL", "Data", true),
            new SkillTag(null, "MySQL", "Data", true),
            new SkillTag(null, "Data Analysis", "Data", true),
            new SkillTag(null, "Machine Learning", "Data", true),
            new SkillTag(null, "Big Data", "Data", true),
            new SkillTag(null, "Data Visualization", "Data", true)
        );
        
        // Sauvegarder toutes les compétences
        repository.saveAll(programmingSkills);
        repository.saveAll(webSkills);
        repository.saveAll(designSkills);
        repository.saveAll(dataSkills);
        
        logger.info("Compétences prédéfinies initialisées avec succès: {} compétences ajoutées", 
                programmingSkills.size() + webSkills.size() + designSkills.size() + dataSkills.size());
    }
    
    /**
     * Initialise les intérêts prédéfinis par catégorie.
     * 
     * @param repository Repository pour les intérêts
     */
    private void initializeInterests(InterestTagRepository repository) {
        // Catégorie Développement
        List<InterestTag> devInterests = Arrays.asList(
            new InterestTag(null, "Web Development", "Développement", true),
            new InterestTag(null, "Mobile App Development", "Développement", true),
            new InterestTag(null, "Game Development", "Développement", true),
            new InterestTag(null, "DevOps", "Développement", true),
            new InterestTag(null, "Cloud Computing", "Développement", true),
            new InterestTag(null, "Blockchain", "Développement", true),
            new InterestTag(null, "Artificial Intelligence", "Développement", true),
            new InterestTag(null, "Cybersecurity", "Développement", true)
        );
        
        // Catégorie Design
        List<InterestTag> designInterests = Arrays.asList(
            new InterestTag(null, "User Interface Design", "Design", true),
            new InterestTag(null, "User Experience Design", "Design", true),
            new InterestTag(null, "Responsive Design", "Design", true),
            new InterestTag(null, "Motion Design", "Design", true),
            new InterestTag(null, "3D Modeling", "Design", true),
            new InterestTag(null, "Animation", "Design", true),
            new InterestTag(null, "Typography", "Design", true)
        );
        
        // Catégorie Business
        List<InterestTag> businessInterests = Arrays.asList(
            new InterestTag(null, "Digital Marketing", "Business", true),
            new InterestTag(null, "Entrepreneurship", "Business", true),
            new InterestTag(null, "Product Management", "Business", true),
            new InterestTag(null, "E-commerce", "Business", true),
            new InterestTag(null, "Content Strategy", "Business", true),
            new InterestTag(null, "Project Management", "Business", true),
            new InterestTag(null, "Business Analytics", "Business", true)
        );
        
        // Catégorie Education
        List<InterestTag> educationInterests = Arrays.asList(
            new InterestTag(null, "Online Learning", "Education", true),
            new InterestTag(null, "Teaching", "Education", true),
            new InterestTag(null, "Academic Research", "Education", true),
            new InterestTag(null, "E-Learning Development", "Education", true),
            new InterestTag(null, "Educational Technology", "Education", true),
            new InterestTag(null, "Mentoring", "Education", true)
        );
        
        // Sauvegarder tous les intérêts
        repository.saveAll(devInterests);
        repository.saveAll(designInterests);
        repository.saveAll(businessInterests);
        repository.saveAll(educationInterests);
        
        logger.info("Intérêts prédéfinis initialisés avec succès: {} intérêts ajoutés", 
                devInterests.size() + designInterests.size() + businessInterests.size() + educationInterests.size());
    }
} 