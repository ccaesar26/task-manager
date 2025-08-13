package com.is.lab.taskmanager.config;

import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.Task;
import com.is.lab.taskmanager.model.TaskStatus;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.TaskRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            // Data already loaded, skip
            return;
        }

        // Create Users
        User owner1 = new User();
        owner1.setUsername("project_owner_1");
        owner1.setPassword(passwordEncoder.encode("pass123"));
        owner1.setEmail("owner1@example.com");

        User collaborator1 = new User();
        collaborator1.setUsername("collaborator_1");
        collaborator1.setPassword(passwordEncoder.encode("pass123"));
        collaborator1.setEmail("collab1@example.com");

        User collaborator2 = new User();
        collaborator2.setUsername("collaborator_2");
        collaborator2.setPassword(passwordEncoder.encode("pass123"));
        collaborator2.setEmail("collab2@example.com");

        userRepository.saveAll(Arrays.asList(owner1, collaborator1, collaborator2));

        // Create a Project and establish relationships using helper methods
        Project project1 = new Project();
        project1.setName("Website Redesign");
        project1.setDescription("Complete redesign of the company website.");
        project1.setOwner(owner1);

        // Use helper methods to add collaborators
        project1.addCollaborator(collaborator1);
        project1.addCollaborator(collaborator2);

        // Create Tasks
        Task task1 = new Task();
        task1.setName("Design Mockups");
        task1.setDescription("Create new mockups in Figma.");
        task1.setStatus(TaskStatus.TO_DO);
        task1.setAssignee(collaborator1);

        Task task2 = new Task();
        task2.setName("Develop Frontend");
        task2.setDescription("Implement the new design using React.");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setAssignee(collaborator2);

        Task task3 = new Task();
        task3.setName("Setup Backend API");
        task3.setDescription("Create REST APIs for the frontend.");
        task3.setStatus(TaskStatus.DONE);
        task3.setAssignee(owner1);

        // Use the helper method to add tasks to the project
        project1.addTask(task1);
        project1.addTask(task2);
        project1.addTask(task3);

        // Now, save the parent object (Project).
        // Due to CascadeType.ALL, the tasks will be saved automatically with it.
        projectRepository.save(project1);
    }
}