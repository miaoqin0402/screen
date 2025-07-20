package com.example.repository;

import com.example.model.DepartmentProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentProgressRepository extends JpaRepository<DepartmentProgress, String> {
}