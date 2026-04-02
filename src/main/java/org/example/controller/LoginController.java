package org.example.controller;

import org.example.User;
import org.example.Complaint;
import org.example.SupportTicket;
import org.example.repository.UserRepository;
import org.example.repository.ComplaintRepository;
import org.example.repository.SupportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Temporary storage for OTP (Presentation-ku idhu dhaan easy)
    private Map<String, String> tempOtpStorage = new HashMap<>();

    // 1. Signup / Register
    @PostMapping(value = {"/signup", "/register"})
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getUsername() == null || userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Error: Username missing or already exists!");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok(existingUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed!");
    }

    // 3. Send OTP (Signup-ku munnadi check pannama anupum)
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) return ResponseEntity.badRequest().body("Email is required!");

        try {
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

            // Temporary-ah save panrom, verify panna easy-ah irukum
            tempOtpStorage.put(email, otp);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("artinsocial.founder@gmail.com");
            message.setTo(email);
            message.setSubject("Agenta App - OTP Code");
            message.setText("Unga verification code: " + otp);
            mailSender.send(message);

            System.out.println("OTP Sent to " + email + " : " + otp);
            return ResponseEntity.ok("OTP sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Mail error: " + e.getMessage());
        }
    }

    // 4. Verify OTP
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String enteredOtp = request.get("otp");

        if (tempOtpStorage.containsKey(email) && tempOtpStorage.get(email).equals(enteredOtp)) {
            tempOtpStorage.remove(email); // Verified, so clear it
            return ResponseEntity.ok(Map.of("message", "OTP Verified Success!"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
    }

    // --- Other Methods (Profile Stats) ---
    @GetMapping("/profile-stats/{username}")
    public ResponseEntity<?> getProfileStats(@PathVariable String username) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", complaintRepository.countByUsername(username));
        stats.put("pending", complaintRepository.countByUsernameAndStatus(username, "Pending"));
        stats.put("resolved", complaintRepository.countByUsernameAndStatus(username, "Resolved"));
        return ResponseEntity.ok(stats);
    }
}