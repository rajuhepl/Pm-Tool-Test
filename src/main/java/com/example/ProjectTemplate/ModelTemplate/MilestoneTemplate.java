package com.example.ProjectTemplate.ModelTemplate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "milestonetemplate")
public class MilestoneTemplate {
    @Id
    private String id;
    //private String milestoneId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long duration;
    private boolean isActive;
    private boolean deleteFlag;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
    @DocumentReference
    private ProjectTemplate projectTemplate;

}
