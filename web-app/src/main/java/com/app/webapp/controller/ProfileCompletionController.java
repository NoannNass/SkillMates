package com.app.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.app.webapp.client.InterestTagClient;
import com.app.webapp.dto.InterestTagDTO;
import com.app.webapp.dto.LearningObjectiveDTO;
import com.app.webapp.dto.PersonalInfoDTO;

@Controller
@RequestMapping("/profile-completion")
@SessionAttributes({"personalInfo", "selectedInterests", "learningObjectives"})
public class ProfileCompletionController {

    private final InterestTagClient interestTagClient;

    public ProfileCompletionController(InterestTagClient interestTagClient) {
        this.interestTagClient = interestTagClient;
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
        return "redirect:/profile-completion/step2";
    }

    @GetMapping("/step2")
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
            }
        } catch (Exception e) {
            // Gérer l'erreur ou laisser la liste vide
        }
        model.addAttribute("interestTags", interestTags);
        return "profile_step2_interests";
    }

    @PostMapping("/step2")
    public String submitInterests(@ModelAttribute("selectedInterests") List<String> selectedInterests, Model model) {
        model.addAttribute("selectedInterests", selectedInterests);
        return "redirect:/profile-completion/step3";
    }

    @GetMapping("/step3")
    public String showLearningObjectivesForm(Model model) {
        if (!model.containsAttribute("learningObjectives")) {
            model.addAttribute("learningObjectives", new ArrayList<LearningObjectiveDTO>());
        }
        return "profile_step3_goals";
    }

    @PostMapping("/step3")
    public String submitLearningObjectives(@ModelAttribute("learningObjectives") List<LearningObjectiveDTO> learningObjectives, SessionStatus sessionStatus) {
        // TODO: Sauvegarder toutes les infos du profil ici
        sessionStatus.setComplete();
        return "redirect:/dashboard";
    }
} 