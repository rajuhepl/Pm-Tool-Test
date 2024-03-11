package com.example.ProjectTemplate.Service;

import com.example.ProjectTemplate.Model.*;
import com.example.ProjectTemplate.ModelTemplate.*;
import com.example.ProjectTemplate.Repository.*;
import com.example.ProjectTemplate.RepositoryTemplate.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectTemplateService {
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
    private ModelMapper modelMapper;


    public String saveSubTasksTemplate(List<SubTaskTemplate> subTaskTemplateList) {
        for (SubTaskTemplate subTaskTemplate : subTaskTemplateList) {
            TaskTemplate taskTemplate = subTaskTemplate.getTaskTemplate();
            if (taskTemplate != null) {
                LocalDate tstartDate = taskTemplate.getStartDate();
                LocalDate tendDate = taskTemplate.getEndDate();
                long tduration = calculateWorkingDays(tstartDate, tendDate);
                taskTemplate.setDuration(tduration);
                // Save the task first to generate its ID
                taskTemplateRepo.save(taskTemplate);

                TaskListTemplate taskListTemplate = taskTemplate.getTaskListTemplate();
                if (taskListTemplate != null) {
                    LocalDate tlstartDate = taskListTemplate.getStartDate();
                    LocalDate tlendDate = taskListTemplate.getEndDate();
                    long tlduration = calculateWorkingDays(tlstartDate, tlendDate);
                    taskListTemplate.setDuration(tlduration);
                    // Save the task list
                    taskListTemplateRepo.save(taskListTemplate);

                    MilestoneTemplate milestoneTemplate = taskListTemplate.getMilestoneTemplate();
                    if (milestoneTemplate != null) {
                        // Calculate the duration excluding Saturdays and Sundays
                        LocalDate startDate = milestoneTemplate.getStartDate();
                        LocalDate endDate = milestoneTemplate.getEndDate();
                        long duration = calculateWorkingDays(startDate, endDate);
                        milestoneTemplate.setDuration(duration);
                        // Save the milestone
                        milestoneTemplateRepo.save(milestoneTemplate);

                        ProjectTemplate projectTemplate = milestoneTemplate.getProjectTemplate();
                        if (projectTemplate != null) {
                            // Calculate the duration excluding Saturdays and Sundays
                            LocalDate pstartDate = projectTemplate.getStartDate();
                            LocalDate pendDate = projectTemplate.getEndDate();
                            long durations = calculateWorkingDays(pstartDate, pendDate);
                            projectTemplate.setDuration(durations);
                            // Save the project
                            projectTemplateRepo.save(projectTemplate);
                        }
                    }
                }
            }
            // Save the subtask after all associations are set
            // Calculate the duration excluding Saturdays and Sundays
            LocalDate startDate = subTaskTemplate.getStartDate();
            LocalDate endDate = subTaskTemplate.getEndDate();
            long duration = calculateWorkingDays(startDate, endDate);
            subTaskTemplate.setDuration(duration);

            subTaskTemplateRepo.save(subTaskTemplate);
        }
        return "Saved successfully";
    }




    public List<SubTaskTemplate> getAllProjectsss() {

        return subTaskTemplateRepo.findAll();
    }

    private long calculateDuration(LocalDate startDate, LocalDate endDate) {
        // Calculate the duration between start and end dates
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
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
    public String getFromTemplateAndAssignToProject(String projectId, String templateName) {
        Optional<Project> projectOptional=projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            return "Project not found";
        }
        Project project = projectOptional.get();

        ProjectTemplate template = new ProjectTemplate();
        template.setName(templateName);


        template.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        template.setActive(true);
        template.setDeleteFlag(false);
        template.setDesc(project.getDesc());
        template.setCreatedBy(project.getCreatedBy());
        template.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        template.setUpdatedBy(project.getUpdatedBy());
        template.setStartDate(project.getStartDate());
        template.setEndDate(project.getEndDate());

        LocalDate projectStartDate=project.getStartDate();
        LocalDate projectEndDate=project.getEndDate();

        long duration = calculateWorkingDays(projectStartDate, projectEndDate);
        template.setDuration(duration);

        projectTemplateRepo.save(template);

        Optional<Milestone> milestoneOptional = milestoneRepository.findByProject(project);
        if (milestoneOptional.isEmpty()) {
            return "Milestone not found for the given project template";
        }
        Milestone milestone = milestoneOptional.get();
        MilestoneTemplate milestoneTemplate = new MilestoneTemplate();

        milestoneTemplate.setActive(true);
        milestoneTemplate.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        milestoneTemplate.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        milestoneTemplate.setDeleteFlag(false);
        milestoneTemplate.setCreatedBy(milestone.getCreatedBy());
        milestoneTemplate.setUpdatedBy(milestone.getUpdatedBy());

        milestoneTemplate.setStartDate(milestone.getStartDate());
        milestoneTemplate.setEndDate(milestone.getEndDate());
        LocalDate milStartDate=milestone.getStartDate();
        LocalDate milEndDate=milestone.getEndDate();
        long milDuration=calculateWorkingDays(milStartDate,milEndDate);
        milestoneTemplate.setDuration(milDuration);

        ProjectTemplate project1 = modelMapper.map(milestone.getProject(), ProjectTemplate.class);
        project1.setId(template.getId());
        milestoneTemplate.setProjectTemplate(project1);
        milestoneTemplateRepo.save(milestoneTemplate);


        Optional<TaskList> taskListOptional = taskListRepository.findByMilestone(milestone);

        if (taskListOptional.isEmpty()) {
            return "Task list template not found for the milestone template";
        }
        TaskList taskList = taskListOptional.get();
        TaskListTemplate taskListTemplate = new TaskListTemplate();

        taskListTemplate.setActive(true);
        taskListTemplate.setDeleteFlag(false);
        taskListTemplate.setCreatedBy(taskList.getCreatedBy());
        taskListTemplate.setUpdatedBy(taskList.getUpdatedBy());
        taskListTemplate.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        taskListTemplate.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        taskListTemplate.setStartDate(taskList.getStartDate());
        taskListTemplate.setEndDate(taskList.getEndDate());
        LocalDate taslisStartDate=taskList.getStartDate();
        LocalDate taslisEndDate=taskList.getEndDate();
        long taslisDuration=calculateWorkingDays(taslisStartDate,taslisEndDate);
        taskListTemplate.setDuration(taslisDuration);


        MilestoneTemplate milestoneTemplate1 = modelMapper.map(taskList.getMilestone(), MilestoneTemplate.class);
        milestoneTemplate1.setId(milestoneTemplate.getId());
        taskListTemplate.setMilestoneTemplate(milestoneTemplate1);
        taskListTemplateRepo.save(taskListTemplate);
        //-----
        Optional<Task> taskOptional = taskRepository.findByTaskList(taskList);

        if (taskOptional.isEmpty()) {
            return "Task template not found for the task list template";
        }
        Task task = taskOptional.get();
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setActive(true);
        //taskTemplate.setId(taskListTemplate.getId());
        taskTemplate.setDeleteFlag(false);
        taskTemplate.setCreatedBy(task.getCreatedBy());
        taskTemplate.setUpdatedBy(task.getUpdatedBy());
        taskTemplate.setCreatedAt(extractLocalDateFromInstant(Instant.now()));
        taskTemplate.setUpdatedAt(extractLocalDateFromInstant(Instant.now()));
        taskTemplate.setStartDate(task.getStartDate());
        taskTemplate.setEndDate(task.getEndDate());
        LocalDate taskStartDate=task.getStartDate();
        LocalDate taskEndDate=task.getEndDate();
        long taskDuration=calculateWorkingDays(taskStartDate,taskEndDate);
        taskTemplate.setDuration(taskDuration);

        TaskListTemplate taskListTemplate1 = modelMapper.map(task.getTaskList(), TaskListTemplate.class);
        taskListTemplate1.setId(taskListTemplate.getId());
        taskTemplate.setTaskListTemplate(taskListTemplate1);
        taskTemplateRepo.save(taskTemplate);
        //-----
        List<SubTask> subTasks = subTaskRepository.findByTaskId(task);
        if (subTasks.isEmpty()) {
            return "Subtask templates not found for the task template";
        }

        // Save the new project first to generate its ID
        ProjectTemplate savedProjectTemplate = projectTemplateRepo.save(template);

        for (SubTask subtask : subTasks) {
            SubTaskTemplate subTaskTemplate = new SubTaskTemplate();

            subTaskTemplate.setSubtaskName(subtask.getSubtaskName());
            subTaskTemplate.setStartDate(subtask.getStartDate());

            subTaskTemplate.setEndDate(subtask.getEndDate());
            subTaskTemplate.setActive(subtask.isActive());
            subTaskTemplate.setDeleteFlag(subtask.isDeleteFlag());
            subTaskTemplate.setCreatedAt(template.getCreatedAt());
            subTaskTemplate.setCreatedBy(subtask.getCreatedBy());

            LocalDate subtaskStartDate=subtask.getStartDate();
            LocalDate subtaskEndDate=subtask.getEndDate();
            long subtaskDuration=calculateWorkingDays(subtaskStartDate,subtaskEndDate);
            subTaskTemplate.setDuration(subtaskDuration);

            // You may need to set other properties of the subtask here
            TaskTemplate TaskTemplate1 = modelMapper.map(subtask.getTask(), TaskTemplate.class);
            TaskTemplate1.setId(taskTemplate.getId());
            subTaskTemplate.setTaskTemplate(TaskTemplate1);

            // Save the subtask
            subTaskTemplateRepo.save(subTaskTemplate);
        }

        return "New projectTemplate created and subtasksTemplate assigned successfully";


    }
}
