package com.is.lab.taskmanager.mapper;

import com.is.lab.taskmanager.dto.ProjectDetailDto;
import com.is.lab.taskmanager.dto.ProjectListDto;
import com.is.lab.taskmanager.dto.TaskDto;
import com.is.lab.taskmanager.dto.UserDto;
import com.is.lab.taskmanager.model.Project;
import com.is.lab.taskmanager.model.Task;
import com.is.lab.taskmanager.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Component responsible for mapping JPA entities to Data Transfer Objects (DTOs).
 * This ensures that the data sent to the web layer is clean, safe, and detached
 * from the database session, preventing LazyInitializationException.
 */
@Component
public class DtoMapper {

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user The User entity.
     * @return A UserDto containing safe-to-display user information.
     */
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        var dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    /**
     * Converts a Task entity to a TaskDto.
     *
     * @param task The Task entity.
     * @return A TaskDto.
     */
    public TaskDto toTaskDto(Task task) {
        if (task == null) {
            return null;
        }
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setProjectId(task.getProject().getId());

        // Safely get the assignee's username, if an assignee exists
        if (task.getAssignee() != null) {
            dto.setAssigneeUsername(task.getAssignee().getUsername());
        }

        return dto;
    }

    /**
     * Converts a Project entity to a detailed ProjectDetailDto.
     * This is the main method that recursively calls other mappers
     * to build a complete object graph for the view layer.
     *
     * @param project The Project entity.
     * @return A ProjectDetailDto containing all necessary details for the project view.
     */
    public ProjectDetailDto toProjectDetailDto(Project project) {
        if (project == null) {
            return null;
        }
        var dto = new ProjectDetailDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());

        // Map the owner using the dedicated mapper method
        dto.setOwner(toUserDto(project.getOwner()));

        // Map the sets of related entities to their respective DTOs
        // This is where we solve the Lazy Loading issue, as these calls
        // happen inside a transactional service method.
        dto.setTasks(project.getTasks().stream()
                .map(this::toTaskDto)
                .collect(Collectors.toSet()));

        dto.setCollaborators(project.getCollaborators().stream()
                .map(this::toUserDto)
                .collect(Collectors.toSet()));

        return dto;
    }

    /**
     * Converts a Project entity to a lightweight ProjectListDto.
     *
     * @param project The Project entity.
     * @return A ProjectListDto, suitable for lists.
     */
    public ProjectListDto toProjectListDto(Project project) {
        if (project == null) {
            return null;
        }
        var dto = new ProjectListDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setOwnerUsername(project.getOwner().getUsername());
        return dto;
    }
}