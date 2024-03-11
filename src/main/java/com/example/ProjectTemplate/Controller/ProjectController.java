package com.example.ProjectTemplate.Controller;

import com.example.ProjectTemplate.Model.Project;
import com.example.ProjectTemplate.Model.SubTask;
import com.example.ProjectTemplate.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;


    @PostMapping("/postOneProject")
    public String postOneProject(@RequestBody List<SubTask> subTaskList){
        return projectService.saveSubTasks(subTaskList);
    }
    @GetMapping("/getAllProjects")
    public List<SubTask> getAllProjects(){
        return projectService.getAllProjects();
    }


    @PostMapping("/createProjectFromTemplate")
    public String createProjectFromTemplate(@RequestParam String templateId, @RequestParam String projectName) {
        return projectService.getFromTemplateAndAssignToProject(templateId, projectName);
    }




}

