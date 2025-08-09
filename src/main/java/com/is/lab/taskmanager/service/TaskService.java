package com.is.lab.taskmanager.service;

import com.is.lab.taskmanager.exception.ResourceNotFoundException;
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

    @Transactional
    public Task createTask(Long projectId, String name, String description) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setProject(project);
        task.setStatus(TaskStatus.TO_DO); // Default status

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    @Transactional
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = findTaskById(taskId);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignTaskToUser(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Optional: Check if the user is a collaborator on the project
        if (!task.getProject().getCollaborators().contains(user) && !task.getProject().getOwner().equals(user)) {
            throw new IllegalArgumentException("User is not a collaborator or owner of the project.");
        }

        task.setAssignee(user);
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}