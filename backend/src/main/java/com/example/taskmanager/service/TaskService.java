
package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.Task.Priority;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        if (task.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Optional<LocalDate> dueBefore, Optional<Priority> priority, Optional<Boolean> completed) {
        return taskRepository.findAll().stream()
            .filter(task -> dueBefore.map(date -> task.getDueDate().isBefore(date)).orElse(true))
            .filter(task -> priority.map(p -> p.equals(task.getPriority())).orElse(true))
            .filter(task -> completed.map(c -> c.equals(task.isCompleted())).orElse(true))
            .toList();
    }

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(UUID id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setDueDate(updatedTask.getDueDate());
            task.setPriority(updatedTask.getPriority());
            task.setCompleted(updatedTask.isCompleted());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }
}
