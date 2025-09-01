package com.hr.department.controller;
import com.hr.department.entity.Department;
import com.hr.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    // GET department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET all departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    // GET department by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
        return departmentService.getDepartmentByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET departments by HOD
    @GetMapping("/hod/{hodId}")
    public List<Department> getDepartmentsByHod(@PathVariable Long hodId) {
        return departmentService.getDepartmentsByHod(hodId);
    }

    // POST - Save new department
    @PostMapping
    public ResponseEntity<Department> saveDepartment(@RequestBody Department department) {
        Department saved = departmentService.saveDepartment(department);
        return ResponseEntity.ok(saved);
    }

    // PUT - Update existing department
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        try {
            department.setId(id);
            Department updated = departmentService.updateDepartment(department);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            log.error("Update failed: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
