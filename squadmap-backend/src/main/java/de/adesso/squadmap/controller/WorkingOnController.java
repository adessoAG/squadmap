package de.adesso.squadmap.controller;

import de.adesso.squadmap.models.WorkingOn;
import de.adesso.squadmap.service.WorkingOnService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/workingOn")
public class WorkingOnController {

    private WorkingOnService workingOnService;

    public WorkingOnController(WorkingOnService workingOnService) {
        this.workingOnService = workingOnService;
    }

    @GetMapping("/all")
    public Iterable<WorkingOn> getAllWorkingOn() {
        return workingOnService.findAll();
    }

    @GetMapping("/{workingOnId}")
    public Optional<WorkingOn> getWorkingOnById(@PathVariable long workingOnId) {
        return workingOnService.findById(workingOnId);
    }

    @PostMapping("/create")
    public WorkingOn createWorkingOn(WorkingOn workingOn) {
        return workingOnService.save(workingOn);
    }

    @PutMapping("/update/{workingOnId}")
    public WorkingOn updateWorkingOn(@PathVariable long workingOnId, WorkingOn workingOn) {
        return workingOnService.save(workingOn);
    }

    @DeleteMapping("/delete/{workingOnId}")
    public void deleteWorkingOn(@PathVariable long workingOnId) {
        workingOnService.deleteById(workingOnId);
    }
}
