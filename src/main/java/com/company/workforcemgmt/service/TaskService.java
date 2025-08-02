package com.company.workforcemgmt.service;

import com.company.workforcemgmt.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final Map<Long, Task> taskMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Task createTask(Task task) {
        Long id = idGenerator.getAndIncrement();
        task.setId(id);
        taskMap.put(id, task);
        return task;
    }

    public List<Task> getTasksForDateRange(LocalDate start, LocalDate end) {
        return taskMap.values().stream()
                .filter(task -> !"CANCELLED".equalsIgnoreCase(task.getStatus()))
                .filter(task -> {
                    LocalDate taskStart = task.getStartDate();
                    return taskStart != null && !taskStart.isBefore(start) && !taskStart.isAfter(end);
                })
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public Optional<Task> getTaskById(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    public void cancelTask(Long id) {
        Task task = taskMap.get(id);
        if (task != null) {
            task.setStatus("CANCELLED");
        }
    }

    public void updateTask(Task updatedTask) {
        if (taskMap.containsKey(updatedTask.getId())) {
            taskMap.put(updatedTask.getId(), updatedTask);
        }
    }
}
