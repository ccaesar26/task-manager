package com.is.lab.taskmanager.controller;

import com.is.lab.taskmanager.dto.ProjectFormDto;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.service.ProjectService;
import com.is.lab.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    // Display a list of all projects
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAllProjects());
        return "projects/list";
    }

    // Display details of a specific project
    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findProjectById(id));
        return "projects/details";
    }

    // Show the form for creating a new project
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Provide an empty form DTO for data binding
        model.addAttribute("projectForm", new ProjectFormDto());
        return "projects/form";
    }

    @PostMapping
    public String createProject(@ModelAttribute("projectForm") ProjectFormDto formDto,
                                @AuthenticationPrincipal User currentUser) {
        projectService.createProject(formDto, currentUser);
        return "redirect:/projects";
    }

    // Show the form for editing an existing project
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        // Get the full project details
        var projectDetail = projectService.findProjectById(id);

        // Populate a form DTO with existing data for the form
        ProjectFormDto formDto = new ProjectFormDto();
        formDto.setName(projectDetail.getName());
        formDto.setDescription(projectDetail.getDescription());

        model.addAttribute("projectForm", formDto);
        model.addAttribute("projectId", id); // Pass the ID for the form action URL
        return "projects/form-edit"; // We will use a dedicated edit form template
    }

    // Process the form for updating a project
    @PostMapping("/{id}/edit")
    public String updateProject(@PathVariable Long id, @ModelAttribute("projectForm") ProjectFormDto formDto) {
        projectService.updateProject(id, formDto);
        return "redirect:/projects/" + id;
    }

    // Handle project deletion
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

    @GetMapping("/{projectId}/collaborators/add")
    public String showAddCollaboratorForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("project", projectService.findProjectById(projectId));
        model.addAttribute("users", userService.findAllUsers()); // Trimitem lista de useri
        return "projects/add-collaborator-form";
    }

    @PostMapping("/{projectId}/collaborators/add")
    public String addCollaborator(@PathVariable Long projectId,
                                  @RequestParam Long userId,
                                  @AuthenticationPrincipal User currentUser) {
        projectService.addCollaborator(projectId, userId, currentUser);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/collaborators/{collaboratorId}/remove")
    public String removeCollaborator(@PathVariable Long projectId,
                                     @PathVariable Long collaboratorId,
                                     @AuthenticationPrincipal User currentUser) {
        projectService.removeCollaborator(projectId, collaboratorId, currentUser);
        return "redirect:/projects/" + projectId;
    }
}