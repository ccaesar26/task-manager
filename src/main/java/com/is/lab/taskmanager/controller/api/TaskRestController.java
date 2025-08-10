package com.is.lab.taskmanager.controller.api;

import com.is.lab.taskmanager.dto.TaskDto;
import com.is.lab.taskmanager.dto.TaskFormDto;
import com.is.lab.taskmanager.model.TaskStatus;
import com.is.lab.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    // Note: GET for all tasks is implicitly handled by getProjectById in ProjectRestController,
    // which returns a ProjectDetailDto containing all tasks.
    // We could add a dedicated GET here if needed, e.g., for pagination or filtering.

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        TaskDto task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@PathVariable Long projectId, @RequestBody TaskFormDto formDto) {
        TaskDto createdTask = taskService.createTask(projectId, formDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, String> statusUpdate) {
        // Using a Map allows for a simple JSON body like: { "status": "IN_PROGRESS" }
        String statusString = statusUpdate.get("status");
        if (statusString == null) {
            return ResponseEntity.badRequest().build(); // Or a more descriptive error
        }
        TaskStatus status = TaskStatus.valueOf(statusString.toUpperCase());
        TaskDto updatedTask = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskDto> assignTaskToUser(@PathVariable Long taskId, @RequestBody Map<String, Long> assignment) {
        // Using a Map for a JSON body like: { "userId": 2 }
        Long userId = assignment.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        TaskDto updatedTask = taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}