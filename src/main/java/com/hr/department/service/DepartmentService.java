package com.hr.department.service;

import com.hr.department.entity.Department;
import com.hr.department.repository.DepartmentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Optional<Department> getDepartmentById(Long departmentId){
        log.info("getting department by id: {}", departmentId);
        return departmentRepository.findById(departmentId);
    }

    public List<Department> getAllDepartments(){
        log.info("getting all departments");
        return departmentRepository.findAll();
    }

    public Optional<Department>  getDepartmentByName(String departmentName){
        log.info("getting department by name: {}", departmentName);
        return departmentRepository.findAll().stream().filter(department -> department.getDepartmentName().equalsIgnoreCase(departmentName)).findFirst();
    }

    public List<Department> getDepartmentsByHod(Long hodId){
        log.info("getting departments by hod id: {}", hodId);
        return departmentRepository.findAll().stream().filter(department -> department.getHod()!=null && department.getHod().equals(hodId)).toList();
    }

    public Department saveDepartment(Department department){
        log.info("saving department: {}", department);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Department department){
        log.info("updating department: {}", department);
        Optional<Department> departmentOptional = departmentRepository.findById(department.getId());
        if(departmentOptional.isEmpty()){
      log.error("Department with id {} not found", department.getId());
      throw new RuntimeException("Department not found");
    }else{
      Department existingDepartment = departmentOptional.get();
      existingDepartment.setDepartmentName(department.getDepartmentName());
      existingDepartment.setHod(department.getHod());
      return departmentRepository.save(existingDepartment);
    }
    }
     
}


