package org.example.controller;

import org.example.Complaint;
import org.example.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
    @Autowired private ComplaintRepository repository;

    @PutMapping("/update-status/{id}")
    public Complaint updateStatus(@PathVariable String id, @RequestParam String status) {
        Complaint complaint = repository.findById(id).orElseThrow();
        complaint.setStatus(status); // Example: "Solved" or "In Progress"
        return repository.save(complaint);
    }
}