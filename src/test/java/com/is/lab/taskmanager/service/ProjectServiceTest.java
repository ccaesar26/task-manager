package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.dto.ProjectDetailDto;
import com.is.lab.taskmanager.dto.ProjectFormDto;
import com.is.lab.taskmanager.exception.ResourceNotFoundException;
import com.is.lab.taskmanager.mapper.DtoMapper;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Spy // Use @Spy for the real mapper to perform actual conversions
    private DtoMapper dtoMapper = new DtoMapper();

    @InjectMocks
    private ProjectService projectService;

    private User user;
    private Project project;

    @BeforeEach
    void setUp() {
        // Setup mock entities that the repositories will "find"
        user = new User();
        user.setId(1L);
        user.setUsername("test_owner");
        user.setEmail("owner@test.com");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("A project for testing.");
        project.setOwner(user);
        project.setTasks(Collections.emptySet()); // Initialize collections
        project.setCollaborators(Collections.emptySet());
    }

    @Test
    void whenFindProjectById_andProjectExists_thenDtoShouldBeReturned() {
        // Arrange
        // When the repository is called, it returns our mock entity
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // Act
        // The service method is called
        ProjectDetailDto resultDto = projectService.findProjectById(1L);

        // Assert
        // We assert that the returned DTO contains the correct data from the mock entity
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getName()).isEqualTo("Test Project");
        assertThat(resultDto.getOwner().getUsername()).isEqualTo("test_owner");
        verify(dtoMapper, times(1)).toProjectDetailDto(any(Project.class));
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
    void whenCreateProject_thenProjectShouldBeSavedAndDtoReturned() {
        // Arrange
        // This is the input from the controller
        ProjectFormDto formDto = new ProjectFormDto();
        formDto.setName("New Project");
        formDto.setDescription("New Description");

        // We need to mock what the repositories do
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // When save is called, return the mock project so the mapper has something to convert
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        ProjectDetailDto resultDto = projectService.createProject(formDto, 1L);

        // Assert
        assertThat(resultDto).isNotNull();
        // Verify that the repository's save method was called once
        verify(projectRepository, times(1)).save(any(Project.class));
        // Verify that the mapper was called to convert the result
        verify(dtoMapper, times(1)).toProjectDetailDto(any(Project.class));
    }

    @Test
    void whenDeleteProject_andProjectExists_thenRepositoryDeleteIsCalled() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        // Act
        projectService.deleteProject(1L);

        // Assert/Verify
        verify(projectRepository, times(1)).deleteById(1L);
    }
}