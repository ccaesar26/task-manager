package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.dto.TaskDto;
import com.is.lab.taskmanager.dto.TaskFormDto;
import com.is.lab.taskmanager.mapper.DtoMapper;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.Task;
import com.is.lab.taskmanager.model.TaskStatus;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.TaskRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private DtoMapper dtoMapper = new DtoMapper();

    @InjectMocks
    private TaskService taskService;

    private Project project;
    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setProject(project);
        task.setStatus(TaskStatus.TO_DO);
        task.setAssignee(user);
    }

    @Test
    void whenCreateTask_thenTaskShouldBeSavedAndDtoReturned() {
        // Arrange
        TaskFormDto formDto = new TaskFormDto();
        formDto.setName("New Task");
        formDto.setDescription("Description");

        // When service asks for the project, return our mock project
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        // When service saves the new task, return our mock task
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDto resultDto = taskService.createTask(1L, formDto);

        // Assert
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getName()).isEqualTo("Test Task");
        assertThat(resultDto.getAssigneeUsername()).isEqualTo("test_user");
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(dtoMapper, times(1)).toTaskDto(any(Task.class));
    }

    @Test
    void whenUpdateTaskStatus_thenStatusShouldBeChangedAndDtoReturned() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Create a separate task object to represent the "saved" state with new status
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setProject(project);
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        TaskDto resultDto = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        // Assert
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
}