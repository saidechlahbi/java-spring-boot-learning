
package com.taskflow.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Generated;
import java.time.LocalDateTime;

@Entity  // kat9ol l JPA java persistence api hada class katrepresonti task table 
@Table(name = "tasks")  // katlinki class b b table 
public class Task {

    @Id // pramiry key katkhalih ytjinirita automatique
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String status = "TODO";

    @Generated
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false,
        insertable = false
    )
    private LocalDateTime createdAt;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
