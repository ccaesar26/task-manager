package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.dto.ProjectDetailDto;
import com.is.lab.taskmanager.dto.ProjectFormDto;
import com.is.lab.taskmanager.dto.ProjectListDto;
import com.is.lab.taskmanager.exception.ResourceNotFoundException;
import com.is.lab.taskmanager.mapper.DtoMapper;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public List<ProjectListDto> findAllProjects() {
        return projectRepository.findAll().stream()
                .map(dtoMapper::toProjectListDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDetailDto findProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return dtoMapper.toProjectDetailDto(project);
    }

    @Transactional
    public ProjectDetailDto createProject(ProjectFormDto formDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User (owner) not found with id: " + ownerId));

        Project project = new Project();
        project.setName(formDto.getName());
        project.setDescription(formDto.getDescription());
        project.setOwner(owner);

        Project savedProject = projectRepository.save(project);
        return dtoMapper.toProjectDetailDto(savedProject);
    }

    @Transactional
    public ProjectDetailDto updateProject(Long projectId, ProjectFormDto formDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        project.setName(formDto.getName());
        project.setDescription(formDto.getDescription());

        Project updatedProject = projectRepository.save(project);
        return dtoMapper.toProjectDetailDto(updatedProject);
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public ProjectDetailDto addCollaborator(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User (collaborator) not found with id: " + userId));

        project.getCollaborators().add(user);
        Project updatedProject = projectRepository.save(project);
        return dtoMapper.toProjectDetailDto(updatedProject);
    }

    @Transactional
    public ProjectDetailDto removeCollaborator(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        project.getCollaborators().remove(user);
        Project updatedProject = projectRepository.save(project);
        return dtoMapper.toProjectDetailDto(updatedProject);
    }
}
