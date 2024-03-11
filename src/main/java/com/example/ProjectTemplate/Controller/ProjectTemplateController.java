package com.example.ProjectTemplate.Controller;

import com.example.ProjectTemplate.Model.SubTask;
import com.example.ProjectTemplate.ModelTemplate.SubTaskTemplate;
import com.example.ProjectTemplate.Service.ProjectTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectTemplateController {
    @Autowired
    ProjectTemplateService projectTemplateService;
    @PostMapping("/postOneProjectTemplate")
    public String postOneProjectTemplate(@RequestBody List<SubTaskTemplate> subTaskTemplateList){
        return projectTemplateService.saveSubTasksTemplate(subTaskTemplateList);
    }
    @GetMapping("/getAllProjectsss")
    public List<SubTaskTemplate> getAllProjects(){
        return projectTemplateService.getAllProjectsss();
    }

    @PostMapping("/createTemplateFromProject")
    public String createTemplateFromProject(@RequestParam String projectId, @RequestParam String templateName) {
        return projectTemplateService.getFromTemplateAndAssignToProject(projectId, templateName);
    }

}
