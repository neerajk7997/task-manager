package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.Task.Priority;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody Task task) {
        return service.createTask(task);
    }

    @GetMapping
    public List<Task> getTasks(
            @RequestParam Optional<LocalDate> dueBefore,
            @RequestParam Optional<Priority> priority,
            @RequestParam Optional<Boolean> completed) {
        return service.getTasks(dueBefore, priority, completed);
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable UUID id) {
        return service.getTaskById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable UUID id, @Valid @RequestBody Task task) {
        return service.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable UUID id) {
        service.deleteTask(id);
    }
}
