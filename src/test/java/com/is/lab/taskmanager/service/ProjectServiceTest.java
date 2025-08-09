package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.exception.ResourceNotFoundException;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock // We are mocking the repository
    private ProjectRepository projectRepository;

    @Mock // We are also mocking the user repository
    private UserRepository userRepository;

    @InjectMocks // This creates an instance of ProjectService and injects the mocks into it
    private ProjectService projectService;

    private User user;
    private Project project;

    @BeforeEach
    void setUp() {
        // This method runs before each test, setting up common objects
        user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("A project for testing.");
        project.setOwner(user);
    }

    @Test
    void whenFindProjectById_andProjectExists_thenProjectShouldBeFound() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // Act
        Project foundProject = projectService.findProjectById(1L);

        // Assert
        assertThat(foundProject).isNotNull();
        assertThat(foundProject.getName()).isEqualTo("Test Project");
    }

    @Test
    void whenFindProjectById_andProjectDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> projectService.findProjectById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Project not found with id: 1");
    }

    @Test
    void whenCreateProject_thenProjectShouldBeSaved() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // We need to tell Mockito what to do when save() is called.
        // any(Project.class) matches any Project object.
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project createdProject = projectService.createProject("Test Project", "A project for testing.", 1L);

        // Assert
        assertThat(createdProject).isNotNull();
        assertThat(createdProject.getName()).isEqualTo("Test Project");
        // Verify that the save method on the repository was called exactly once
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void whenDeleteProject_andProjectExists_thenRepositoryDeleteShouldBeCalled() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        // The deleteById method is void, so we don't need to return anything.
        // We just need to make sure it's called.
        doNothing().when(projectRepository).deleteById(1L);

        // Act
        projectService.deleteProject(1L);

        // Assert/Verify
        // We verify that deleteById was called on the repository with the correct ID.
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteProject_andProjectDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> projectService.deleteProject(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Project not found with id: 1");

        // Verify that deleteById was NEVER called
        verify(projectRepository, never()).deleteById(anyLong());
    }
}
