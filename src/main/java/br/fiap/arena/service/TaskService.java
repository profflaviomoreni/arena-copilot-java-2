package br.fiap.arena.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.fiap.arena.domain.Task;
import br.fiap.arena.domain.TaskStatus;
import br.fiap.arena.repo.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) { this.repository = repository; }

    public Task create(Task t) { return repository.save(t); }

    public List<Task> listAll() { return repository.findAll(); }

    public List<Task> listByStatus(TaskStatus status) { return repository.findByStatus(status); }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Map<String, Object> stats() {
        List<Task> all = repository.findAll();
        LocalDate today = LocalDate.now();
        
        long overdue = all.stream()
            .filter(t -> t.getDueDate() != null && !today.isBefore(t.getDueDate()))
            .count();
            
        Map<Integer, Integer> hist = all.stream()
            .map(t -> t.getPriority() == null ? 0 : t.getPriority())
            .collect(Collectors.groupingBy(
                priority -> priority,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
            
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", all.size());
        result.put("overdueCount", overdue);
        result.put("priorityHistogram", hist);
        return result;
    }
}
