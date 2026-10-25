
package com.taskflow.task;

import org.springframework.stereotype.Service;

import com.taskflow.exception.TaskNotFoundException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public Task updateTaskStatus(Long id, String status) {
        if (!List.of("TODO", "IN_PROGRESS", "DONE").contains(status)) {
            throw new IllegalArgumentException("Invalid task status");
        }

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        task.setStatus(status);

        return taskRepository.save(task);
    }
    public Task getTaskById(Long id) {
    return taskRepository.findById(id)
        .orElseThrow(() ->
            new TaskNotFoundException("Task not found"));
    }
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() ->
                new TaskNotFoundException("Task not found"));

        taskRepository.delete(task);
    }
}
