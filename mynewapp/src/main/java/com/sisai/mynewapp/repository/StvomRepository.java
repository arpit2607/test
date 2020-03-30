package com.sisai.mynewapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sisai.mynewapp.listener.Person;

@Repository
public interface StvomRepository extends MongoRepository<Person, Long>{

}
