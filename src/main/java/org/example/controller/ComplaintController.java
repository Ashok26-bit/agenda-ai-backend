package org.example.controller;

import org.example.Complaint;
import org.example.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/complaints") 
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintRepository repository;

    // ✅ 1. Report Save panna (Flutter 'Submit' button-ku idhu thaan mukkiyam)
    @PostMapping("/submit") 
    public ResponseEntity<?> createComplaint(@RequestBody Complaint complaint) {
        try {
            // Status initial-ah Pending-nu vekkalaam
            complaint.setStatus("Pending");
            Complaint saved = repository.save(complaint);
            System.out.println("DEBUG: Report saved for " + complaint.getUsername());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // ✅ 2. Home Screen reports fetch panna (404 error fix panna idhu thaan vazhi)
    @GetMapping("/user/{username}") 
    public ResponseEntity<List<Complaint>> getUserComplaints(@PathVariable String username) {
        System.out.println("DEBUG: Fetching reports for " + username);
        List<Complaint> complaints = repository.findByUsername(username);
        return ResponseEntity.ok(complaints);
    }

    // ✅ 3. Total complaints count fetch panna (Flutter & Website-ku)
    @GetMapping("/count")
    public ResponseEntity<?> getCounts() {
        long total = repository.count();
        // ⚡ Case-insensitive check: "Pending" sariyaa matching-ah nu paarunga
        long pending = repository.findAll().stream()
                .filter(c -> "Pending".equalsIgnoreCase(c.getStatus()))
                .count();
        
        return ResponseEntity.ok(Map.of("total", total, "pending", pending));
    }

    // ✅ 4. User specific counts (Analytics screen-kaga)
    @GetMapping("/count/{username}")
    public ResponseEntity<?> getUserCounts(@PathVariable String username) {
        List<Complaint> userComplaints = repository.findByUsername(username);
        long total = userComplaints.size();
        
        // ⚡ Case-insensitive matching for "Pending" and "Resolved"
        long pending = userComplaints.stream()
                .filter(c -> "Pending".equalsIgnoreCase(c.getStatus()))
                .count();
        long resolved = userComplaints.stream()
                .filter(c -> "Resolved".equalsIgnoreCase(c.getStatus()))
                .count();

        return ResponseEntity.ok(Map.of(
            "total", total,
            "pending", pending,
            "resolved", resolved
        ));
    }
}