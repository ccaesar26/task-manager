package com.is.lab.taskmanager.config;

import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.Task;
import com.is.lab.taskmanager.model.TaskStatus;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.TaskRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DataLoader(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Clear existing data for a clean start
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        // Create Users (in a real app, passwords would be hashed)
        var owner1 = new User();
        owner1.setUsername("project_owner_1");
        owner1.setPassword("pass123");
        owner1.setEmail("owner1@example.com");

        var collaborator1 = new User();
        collaborator1.setUsername("collaborator_1");
        collaborator1.setPassword("pass123");
        collaborator1.setEmail("collab1@example.com");

        var collaborator2 = new User();
        collaborator2.setUsername("collaborator_2");
        collaborator2.setPassword("pass123");
        collaborator2.setEmail("collab2@example.com");

        userRepository.saveAll(Arrays.asList(owner1, collaborator1, collaborator2));

        // Create a Project
        var project1 = new Project();
        project1.setName("Website Redesign");
        project1.setDescription("Complete redesign of the company website.");
        project1.setOwner(owner1);
        project1.setCollaborators(new HashSet<>(Arrays.asList(collaborator1, collaborator2)));

        projectRepository.save(project1);

        // Create Tasks for the Project
        var task1 = new Task();
        task1.setName("Design Mockups");
        task1.setDescription("Create new mockups in Figma.");
        task1.setProject(project1);
        task1.setStatus(TaskStatus.TO_DO);
        task1.setAssignee(collaborator1);

        var task2 = new Task();
        task2.setName("Develop Frontend");
        task2.setDescription("Implement the new design using React.");
        task2.setProject(project1);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setAssignee(collaborator2);

        var task3 = new Task();
        task3.setName("Setup Backend API");
        task3.setDescription("Create REST APIs for the frontend.");
        task3.setProject(project1);
        task3.setStatus(TaskStatus.DONE);
        task3.setAssignee(owner1);

        taskRepository.saveAll(Arrays.asList(task1, task2, task3));
    }
}