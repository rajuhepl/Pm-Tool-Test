package com.example.ProjectTemplate.Service;

import com.example.ProjectTemplate.Model.*;
import com.example.ProjectTemplate.ModelTemplate.*;
import com.example.ProjectTemplate.Repository.*;
import com.example.ProjectTemplate.RepositoryTemplate.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepository;
    @Autowired
    private MilestoneRepo milestoneRepository;
    @Autowired
    private TaskListRepo taskListRepository;
    @Autowired
    private TaskRepo taskRepository;
    @Autowired
    private SubTaskRepo subTaskRepository;
    @Autowired
    private ProjectTemplateRepo projectTemplateRepo;
    @Autowired
    private MilestoneTemplateRepo milestoneTemplateRepo;
    @Autowired
    private TaskListTemplateRepo taskListTemplateRepo;
    @Autowired
    private TaskTemplateRepo taskTemplateRepo;
    @Autowired
    private SubTaskTemplateRepo subTaskTemplateRepo;

    @Autowired
    private ModelMapper modelMapper;

    private long calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        long workingDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            // Increment working days if the current day is not Saturday or Sunday
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    public String saveSubTasks(List<SubTask> subTaskList) {
        for (SubTask subTask : subTaskList) {
            Task task = subTask.getTask();
            if (task != null) {
                // Save the task first to generate its ID
                taskRepository.save(task);

                TaskList taskList = task.getTaskList();
                if (taskList != null) {
                    // Save the task list
                    taskListRepository.save(taskList);

                    Milestone milestone = taskList.getMilestone();
                    if (milestone != null) {
                        // Save the milestone
                        milestoneRepository.save(milestone);

                        Project project = milestone.getProject();
                        if (project != null) {
                            // Save the project
                            projectRepository.save(project);
                        }
                    }
                }
            }


            // Save the subtask after all associations are set
            subTaskRepository.save(subTask);
        }
        return "Saved successfully";
    }

    public List<SubTask> getAllProjects() {

        return subTaskRepository.findAll();
    }

    private LocalDate extractLocalDateFromInstant(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static LocalDate calculateEndDate(LocalDate startDate, long templateDuration) {
        // Initialize current date to start date
        LocalDate currentDate = startDate;

        // Iterate through each day and skip weekends
        for (int i = 0; i < templateDuration; i++) {
            // Skip Saturdays and Sundays
            while (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                currentDate = currentDate.plusDays(1); // Move to the next day
            }
            currentDate = currentDate.plusDays(1); // Move to the next day
        }

        // The current date is the end date
        return currentDate.minusDays(1); // Subtract 1 day to get the correct end date
    }

    public String getFromTemplateAndAssignToProject(String templateId, String projectName) {
        Optional<ProjectTemplate> templateOptional = projectTemplateRepo.findById(templateId);
        if (templateOptional.isEmpty()) {
            return "Template not found";
        }


        ProjectTemplate template = templateOptional.get();

        Project newProject = new Project();
        newProject.setName(projectName);

        newProject.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        newProject.setActive(true);
        newProject.setDeleteFlag(false);
        newProject.setDesc(template.getDesc());
        newProject.setCreatedBy(template.getCreatedBy());
        newProject.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        newProject.setUpdatedBy(template.getUpdatedBy());
        newProject.setStartDate(extractLocalDateFromInstant(Instant.now()));

        // Calculate the end date based on the start date and the duration of the project
        LocalDate startDate = newProject.getStartDate();

        newProject.setEndDate(calculateEndDate(startDate, template.getDuration()));
        projectRepository.save(newProject);


        Optional<MilestoneTemplate> milestoneTemplateOptional = milestoneTemplateRepo.findByProjectTemplate(template);
        if (milestoneTemplateOptional.isEmpty()) {
            return "Milestone template not found for the given project template";
        }


        MilestoneTemplate milestoneTemplate = milestoneTemplateOptional.get();
        Milestone milestone = new Milestone();
        milestone.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        milestone.setActive(true);
        milestone.setDeleteFlag(false);
        milestone.setCreatedBy(milestoneTemplate.getCreatedBy());
        milestone.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        milestone.setUpdatedBy(milestoneTemplate.getUpdatedBy());
        milestone.setStartDate(extractLocalDateFromInstant(Instant.now()));
        LocalDate milestoneStartDate = milestone.getStartDate();
        // Calculate the end date based on the start date and the duration of the project
        milestone.setEndDate(calculateEndDate(milestoneStartDate, milestoneTemplate.getDuration()));
        Project project = modelMapper.map(milestoneTemplate.getProjectTemplate(), Project.class);
        project.setId(newProject.getId());
        milestone.setProject(project);
        milestoneRepository.save(milestone);


        Optional<TaskListTemplate> taskListTemplateOptional = taskListTemplateRepo.findByMilestoneTemplate(milestoneTemplate);

        if (taskListTemplateOptional.isEmpty()) {
            return "Task list template not found for the milestone template";
        }
        TaskListTemplate taskListTemplate = taskListTemplateOptional.get();
        TaskList taskList = new TaskList();
        taskList.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        taskList.setActive(true);
        taskList.setDeleteFlag(false);
        taskList.setCreatedBy(taskListTemplate.getCreatedBy());
        taskList.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        taskList.setUpdatedBy(taskListTemplate.getUpdatedBy());
        taskList.setStartDate(extractLocalDateFromInstant(Instant.now()));
        LocalDate taskListStartDate = taskList.getStartDate();
        // Calculate the end date based on the start date and the duration of the project
        milestone.setEndDate(calculateEndDate(taskListStartDate, taskListTemplate.getDuration()));
        Milestone milestone1 = modelMapper.map(taskListTemplate.getMilestoneTemplate(), Milestone.class);
        milestone1.setId(milestone.getId());
        taskList.setMilestone(milestone1);
        taskListRepository.save(taskList);


        Optional<TaskTemplate> taskTemplateOptional = taskTemplateRepo.findByTaskListTemplate(taskListTemplate);

        if (taskTemplateOptional.isEmpty()) {
            return "Task template not found for the task list template";
        }
        TaskTemplate taskTemplate = taskTemplateOptional.get();
        Task task = new Task();
        task.setTaskName(taskTemplate.getTaskName());
        task.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        task.setActive(true);
        task.setDeleteFlag(false);
        task.setCreatedBy(taskTemplate.getCreatedBy());
        task.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        task.setUpdatedBy(taskTemplate.getUpdatedBy());
        task.setStartDate(extractLocalDateFromInstant(Instant.now()));
        LocalDate taskStartDate = task.getStartDate();
        // Calculate the end date based on the start date and the duration of the project
        task.setEndDate(calculateEndDate(taskStartDate, taskTemplate.getDuration()));
        TaskList taskList1 = modelMapper.map(taskTemplate.getTaskListTemplate(), TaskList.class);
        taskList1.setId(taskList.getId());
        task.setTaskList(taskList1);
        taskRepository.save(task);


        List<SubTaskTemplate> subtaskTemplates = subTaskTemplateRepo.findByTaskTemplateId(taskTemplate);
        if (subtaskTemplates.isEmpty()) {
            return "Subtask templates not found for the task template";
        }

        // Save the new project first to generate its ID
        Project savedProject = projectRepository.save(newProject);

        for (SubTaskTemplate subtaskTemplate : subtaskTemplates) {
            SubTask subtask = new SubTask();

            subtask.setSubtaskName(subtaskTemplate.getSubtaskName());
            subtask.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
            subtask.setActive(true);
            subtask.setDeleteFlag(false);
            subtask.setCreatedBy(subtaskTemplate.getCreatedBy());
            subtask.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
            subtask.setUpdatedBy(subtaskTemplate.getUpdatedBy());
            subtask.setStartDate(extractLocalDateFromInstant(Instant.now()));
            LocalDate subtaskStartDate = subtask.getStartDate();
            // Calculate the end date based on the start date and the duration of the project
            subtask.setEndDate(calculateEndDate(subtaskStartDate, subtaskTemplate.getDuration()));
            Task task1 = modelMapper.map(subtaskTemplate.getTaskTemplate(), Task.class);
            task1.setId(task.getId());
            subtask.setTask(task1);
            // Save the subtask

            subTaskRepository.save(subtask);
        }

        return "New project created and subtasks assigned successfully";
    }


}