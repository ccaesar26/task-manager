package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.dto.TaskDto;
import com.is.lab.taskmanager.dto.TaskFormDto;
import com.is.lab.taskmanager.exception.ResourceNotFoundException;
import com.is.lab.taskmanager.mapper.DtoMapper;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.Task;
import com.is.lab.taskmanager.model.TaskStatus;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.repository.ProjectRepository;
import com.is.lab.taskmanager.repository.TaskRepository;
import com.is.lab.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public TaskDto findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return dtoMapper.toTaskDto(task);
    }

    @Transactional
    public TaskDto createTask(Long projectId, TaskFormDto formDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Task task = new Task();
        task.setName(formDto.getName());
        task.setDescription(formDto.getDescription());
        task.setProject(project);
        task.setStatus(TaskStatus.TO_DO); // Default status

        Task savedTask = taskRepository.save(task);
        return dtoMapper.toTaskDto(savedTask);
    }

    @Transactional
    public TaskDto updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return dtoMapper.toTaskDto(updatedTask);
    }

    @Transactional
    public TaskDto assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!task.getProject().getCollaborators().contains(user) && !task.getProject().getOwner().equals(user)) {
            throw new IllegalArgumentException("User is not a collaborator or owner of the project.");
        }

        task.setAssignee(user);
        Task updatedTask = taskRepository.save(task);
        return dtoMapper.toTaskDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}