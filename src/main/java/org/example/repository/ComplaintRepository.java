package org.example.repository;

import org.example.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

    // 1. User wise total counts (Dashboard-la 12, 04, 08 kaatta)
    Long countByUsername(String username);

    // 2. User wise status counts (Pending vs Resolved)
    Long countByUsernameAndStatus(String username, String status);

    List<Complaint> findByDepartment(String department);

    // 3. Oru specific user-oda complaints list-a mattum fetch panna
    List<Complaint> findByUsername(String username);

    // 4. Global counts (Total Filed, Total Resolved in the whole app)
    long countByStatus(String status);

    // 5. Category wise filter (Optional: Roads, Electrical)
    long countByCategory(String category);

    // 6. Status filter (Optional: Activity screen-kaga)
    List<Complaint> findByStatus(String status);
}