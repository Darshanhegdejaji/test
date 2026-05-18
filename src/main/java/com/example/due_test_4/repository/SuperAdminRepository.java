package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.example.due_test_4.entity.*;

@Repository()
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, String> {
}
