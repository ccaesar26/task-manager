package com.is.lab.taskmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING) // Stocheaza enum-ul ca String în DB
    private TaskStatus status;

    private Date deadline;

    // Relatia Many-to-One: mai multe task-uri apartin unui singur proiect
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id") // Specifica coloana FK in tabelul 'tasks'
    private Project project;
}
