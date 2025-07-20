package com.example.controller;

import com.example.model.DepartmentProgress;
import com.example.repository.DepartmentProgressRepository;
import com.example.model.DepartmentProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentProgressRepository repository;

    @GetMapping
    public List<DepartmentProgress> getAllDepartments() {
            return repository.findAll(Sort.by(Sort.Direction.DESC, "achievementRate"));
    }
}