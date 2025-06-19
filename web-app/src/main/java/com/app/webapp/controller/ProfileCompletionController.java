package com.app.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.app.webapp.client.InterestTagClient;
import com.app.webapp.client.SkillTagClient;
import com.app.webapp.client.UserClient;
import com.app.webapp.dto.InterestTagDTO;
import com.app.webapp.dto.LearningObjectiveDTO;
import com.app.webapp.dto.PersonalInfoDTO;
import com.app.webapp.dto.SkillTagDTO;
import com.app.webapp.security.CustomUserDetailsService;
import com.app.webapp.security.UserInfoSession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile-completion")
@SessionAttributes({"personalInfo", "selectedSkills", "selectedInterests", "learningObjectives"})
public class ProfileCompletionController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileCompletionController.class);

    private final InterestTagClient interestTagClient;
    private final SkillTagClient skillTagClient;
    private final UserClient userClient;
    private final UserInfoSession userInfoSession;
    private final CustomUserDetailsService userDetailsService;

    public ProfileCompletionController(InterestTagClient interestTagClient, SkillTagClient skillTagClient, UserClient userClient, UserInfoSession userInfoSession, CustomUserDetailsService userDetailsService) {
        this.interestTagClient = interestTagClient;
        this.skillTagClient = skillTagClient;
        this.userClient = userClient;
        this.userInfoSession = userInfoSession;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/step1")
    public String showPersonalInfoForm(Model model) {
        if (!model.containsAttribute("personalInfo")) {
            model.addAttribute("personalInfo", new PersonalInfoDTO());
        }
        return "profile_step1_personal";
    }

    @PostMapping("/step1")
    public String submitPersonalInfo(@ModelAttribute PersonalInfoDTO personalInfo, Model model) {
        model.addAttribute("personalInfo", personalInfo);
        
        // Vérifier si les informations utilisateur sont disponibles
        String userId = userInfoSession.getUserId();
        logger.info("Soumission des informations personnelles pour l'utilisateur: userId={}", userId);
        
        if (userId != null) {
            try {
                logger.info("Tentative de mise à jour des informations personnelles: {}", personalInfo);
                var response = userClient.updatePersonalInfo(userId, personalInfo);
                logger.info("Réponse de mise à jour des informations personnelles: {}", response);
            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour des informations personnelles", e);
            }
        } else {
            logger.warn("ID utilisateur non disponible en session, mise à jour impossible");
        }
        return "redirect:/profile-completion/step2";
    }

    @GetMapping("/step2")
    public String showSkillsForm(Model model) {
        if (!model.containsAttribute("selectedSkills")) {
            model.addAttribute("selectedSkills", new ArrayList<String>());
        }
        
        // Charger dynamiquement les tags de compétences depuis le backend
        List<SkillTagDTO> skillTags = new ArrayList<>();
        try {
            var response = skillTagClient.getAllSkillTags();
            if (response != null && response.getData() != null) {
                skillTags = response.getData();
                logger.info("Récupération de {} tags de compétences", skillTags.size());
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des tags de compétences", e);
        }
        model.addAttribute("skillTags", skillTags);
        return "profile_step2_skills";
    }

    @PostMapping("/step2")
    public String submitSkills(@RequestParam(value = "selectedSkills", required = false) List<String> selectedSkills, Model model) {
        if (selectedSkills == null) {
            selectedSkills = new ArrayList<>();
        }
        
        model.addAttribute("selectedSkills", selectedSkills);
        
        // Vérifier si les informations utilisateur sont disponibles
        String userId = userInfoSession.getUserId();
        logger.info("Soumission des compétences pour l'utilisateur: userId={}", userId);
        logger.info("Compétences sélectionnées: {}", selectedSkills);
        
        if (userId != null) {
            // On suppose que les noms des tags sont suffisants pour lier côté user-service
            List<SkillTagDTO> skillDTOs = new ArrayList<>();
            for (String name : selectedSkills) {
                SkillTagDTO skill = new SkillTagDTO(null, name, "GENERAL");
                skillDTOs.add(skill);
            }
            try {
                logger.info("Tentative de mise à jour des compétences: {}", skillDTOs);
                var response = userClient.updateSkills(userId, skillDTOs);
                logger.info("Réponse de mise à jour des compétences: {}", response);
            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour des compétences", e);
            }
        } else {
            logger.warn("ID utilisateur non disponible en session, mise à jour impossible");
        }
        return "redirect:/profile-completion/step3";
    }

    @GetMapping("/step3")
    public String showInterestsForm(Model model) {
        if (!model.containsAttribute("selectedInterests")) {
            model.addAttribute("selectedInterests", new ArrayList<String>());
        }
        
        // Charger dynamiquement les tags d'intérêt depuis le backend
        List<InterestTagDTO> interestTags = new ArrayList<>();
        try {
            var response = interestTagClient.getAllInterestTags();
            if (response != null && response.getData() != null) {
                interestTags = response.getData();
                logger.info("Récupération de {} tags d'intérêt", interestTags.size());
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des tags d'intérêt", e);
        }
        model.addAttribute("interestTags", interestTags);
        return "profile_step3_interests";
    }

    @PostMapping("/step3")
    public String submitInterests(@RequestParam(value = "selectedInterests", required = false) List<String> selectedInterests, Model model) {
        if (selectedInterests == null) {
            selectedInterests = new ArrayList<>();
        }
        
        model.addAttribute("selectedInterests", selectedInterests);
        
        // Vérifier si les informations utilisateur sont disponibles
        String userId = userInfoSession.getUserId();
        logger.info("Soumission des intérêts pour l'utilisateur: userId={}", userId);
        logger.info("Intérêts sélectionnés: {}", selectedInterests);
        
        if (userId != null) {
            // On suppose que les noms des tags sont suffisants pour lier côté user-service
            List<InterestTagDTO> interestDTOs = new ArrayList<>();
            for (String name : selectedInterests) {
                InterestTagDTO interest = new InterestTagDTO(null, name, "GENERAL");
                interestDTOs.add(interest);
            }
            try {
                logger.info("Tentative de mise à jour des intérêts: {}", interestDTOs);
                var response = userClient.updateInterests(userId, interestDTOs);
                logger.info("Réponse de mise à jour des intérêts: {}", response);
            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour des intérêts", e);
            }
        } else {
            logger.warn("ID utilisateur non disponible en session, mise à jour impossible");
        }
        return "redirect:/profile-completion/step4";
    }

    @GetMapping("/step4")
    public String showLearningObjectivesForm(Model model) {
        if (!model.containsAttribute("learningObjectives")) {
            model.addAttribute("learningObjectives", new ArrayList<LearningObjectiveDTO>());
        }
        return "profile_step4_goals";
    }

    @PostMapping("/step4")
    public String submitLearningObjectives(@RequestParam(value = "learningObjectives", required = false) List<String> learningObjectivesTitles, 
                                          SessionStatus sessionStatus,
                                          HttpServletRequest request) {
        // Vérifier si les informations utilisateur sont disponibles
        String userId = userInfoSession.getUserId();
        String userEmail = userInfoSession.getEmail();
        
        logger.info("Soumission des objectifs d'apprentissage pour l'utilisateur: userId={}, email={}", 
                   userId, userEmail);
        
        if (learningObjectivesTitles == null) {
            learningObjectivesTitles = new ArrayList<>();
            logger.warn("Aucun objectif d'apprentissage sélectionné");
        } else {
            logger.info("Objectifs d'apprentissage sélectionnés: {}", learningObjectivesTitles);
        }
        
        List<LearningObjectiveDTO> learningObjectives = new ArrayList<>();
        for (String title : learningObjectivesTitles) {
            LearningObjectiveDTO objective = new LearningObjectiveDTO(null, title, userId, 0);
            learningObjectives.add(objective);
        }
        
        if (userId != null) {
            try {
                logger.info("Tentative de mise à jour des objectifs d'apprentissage: {}", learningObjectives);
                var response = userClient.updateLearningObjectives(userId, learningObjectives);
                logger.info("Réponse de mise à jour des objectifs d'apprentissage: {}", response);
            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour des objectifs d'apprentissage", e);
            }
        } else {
            logger.warn("ID utilisateur non disponible en session, mise à jour impossible");
        }
        
        // Vérifier si l'utilisateur est déjà authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            logger.info("Utilisateur non authentifié, tentative d'authentification avec email: {}", userEmail);
            
            // Tenter de réauthentifier l'utilisateur
            if (userEmail != null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                    // Force la création de la session si elle n'existe pas
                    HttpSession session = request.getSession(true);
                    logger.info("Authentification réussie, session ID: {}", session.getId());
                } catch (Exception e) {
                    logger.error("Erreur lors de la réauthentification", e);
                }
            }
        } else {
            logger.info("Utilisateur déjà authentifié: {}", authentication.getName());
        }
        
        // Terminer la session des attributs mais pas la session HTTP
        sessionStatus.setComplete();
        logger.info("Fin de la création de profil, redirection vers le dashboard");
        
        return "redirect:/dashboard";
    }
} 