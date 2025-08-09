package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.exception.ResourceNotFoundException;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @Transactional
    public Project createProject(String name, String description, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerId));

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);

        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long id, String name, String description) {
        Project project = findProjectById(id);
        project.setName(name);
        project.setDescription(description);
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public Project addCollaborator(Long projectId, Long userId) {
        Project project = findProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        project.getCollaborators().add(user);
        return projectRepository.save(project);
    }

    @Transactional
    public Project removeCollaborator(Long projectId, Long userId) {
        Project project = findProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        project.getCollaborators().remove(user);
        return projectRepository.save(project);
    }
}
