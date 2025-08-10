package com.is.lab.taskmanager.controller;

import com.is.lab.taskmanager.dto.TaskFormDto;
import com.is.lab.taskmanager.service.ProjectService;
import com.is.lab.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    // Show the form for adding a new task to a project
    @GetMapping("/new")
    public String showCreateTaskForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("taskForm", new TaskFormDto());
        // We still need project details for the view (e.g., project name in the title)
        model.addAttribute("project", projectService.findProjectById(projectId));
        return "tasks/form";
    }

    // Process the form for creating a new task
    @PostMapping
    public String createTask(@PathVariable Long projectId, @ModelAttribute("taskForm") TaskFormDto formDto) {
        taskService.createTask(projectId, formDto);
        return "redirect:/projects/" + projectId;
    }

    // Handle task deletion
    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/projects/" + projectId;
    }
}