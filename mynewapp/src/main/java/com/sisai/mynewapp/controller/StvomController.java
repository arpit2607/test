package com.sisai.mynewapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisai.mynewapp.exception.ResourceNotFoundException;
import com.sisai.mynewapp.listener.Person;
import com.sisai.mynewapp.model.Employee;
import com.sisai.mynewapp.repository.StvomRepository;
import com.sisai.mynewapp.service.SequenceGeneratorService;

@RestController
@RequestMapping("api/v2")
public class StvomController {
	@Autowired
	private StvomRepository stvomRepository;
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	@GetMapping("/stvom")
	public List<Person>getAllData(){
		return stvomRepository.findAll();
	}
	
	@GetMapping("/stvom/{id}")
	public ResponseEntity <Person> getDataById(@PathVariable(value = "id") Long dataId) throws ResourceNotFoundException{
		Person data=stvomRepository.findById(dataId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + dataId));
		return ResponseEntity.ok().body(data);
	}
	
	@PostMapping("/stvom")
	public void createData(@Valid @RequestBody Person data) throws NullPointerException {
		//data.setId(sequenceGeneratorService.generateSequence(Person.SEQUENCE_NAME));
		stvomRepository.save(data);
	}
	
	@PutMapping("/stvom/{id}")
	public ResponseEntity <Person> updateData(@PathVariable(value = "id") Long dataId,
        @Valid @RequestBody Person dataDetails) throws ResourceNotFoundException{
		Person data=stvomRepository.findById(dataId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + dataId));
		data.setGx(dataDetails.getGx());
		data.setGy(dataDetails.getGy());
		data.setGz(dataDetails.getGz());
		data.setThetaX(dataDetails.getThetaX());
		data.setThetaY(dataDetails.getThetaY());
		data.setTemp(dataDetails.getTemp());
		final Person updatedData=stvomRepository.save(data);
		return ResponseEntity.ok(updatedData);
	}
	
	@DeleteMapping("/stvom/{id}")
	public Map<String,Boolean> deleteData(@PathVariable (value="id") Long dataId) throws ResourceNotFoundException{
		Person data=stvomRepository.findById(dataId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + dataId));
		stvomRepository.delete(data);
		Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
	}
	
}
