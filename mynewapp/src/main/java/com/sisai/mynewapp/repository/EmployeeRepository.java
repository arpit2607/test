package com.sisai.mynewapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sisai.mynewapp.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long>{

}
