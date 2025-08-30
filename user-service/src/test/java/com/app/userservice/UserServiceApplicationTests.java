package com.app.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.app.userservice.repository.InterestTagRepository;
import com.app.userservice.repository.LearningObjectiveRepository;
import com.app.userservice.repository.SkillTagRepository;
import com.app.userservice.repository.UserRepository;

@SpringBootTest
class UserServiceApplicationTests {

    @MockBean
    private InterestTagRepository interestTagRepository;

    @MockBean
    private SkillTagRepository skillTagRepository;

    @MockBean
    private LearningObjectiveRepository learningObjectiveRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

}
