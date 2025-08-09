package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.exception.ResourceNotFoundException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Project project;
    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setProject(project);
        task.setStatus(TaskStatus.TO_DO);
    }

    @Test
    void whenCreateTask_thenTaskShouldBeSavedWithToDoStatus() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task createdTask = taskService.createTask(1L, "Test Task", "Task Description");

        // Assert
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getName()).isEqualTo("Test Task");
        assertThat(createdTask.getStatus()).isEqualTo(TaskStatus.TO_DO);
        assertThat(createdTask.getProject().getName()).isEqualTo("Test Project");
    }

    @Test
    void whenUpdateTaskStatus_thenStatusShouldBeChanged() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Create a separate task object to represent the saved state
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task updatedTask = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        // Assert
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void whenAssignTaskToUser_andTaskDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.assignTaskToUser(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found with id: 99");
    }
}