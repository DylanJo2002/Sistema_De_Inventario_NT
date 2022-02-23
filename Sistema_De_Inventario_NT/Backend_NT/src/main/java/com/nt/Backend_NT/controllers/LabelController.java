package com.nt.Backend_NT.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.model.LabelsResponse;
import com.nt.Backend_NT.services.LabelService;

@RestController
@RequestMapping("/labels")
public class LabelController {
	
	@Autowired
	private LabelService labelService;
	
	@GetMapping("/categories")
	public ResponseEntity<LabelsResponse> getLabelsByCategory(@RequestParam int categoryId) 
			throws Exception{
		List<LabelEntity> labels = labelService.getLabelsByCategory(categoryId);
		LabelsResponse response = new LabelsResponse(labels);
		return new ResponseEntity<LabelsResponse>(response,HttpStatus.OK);	
	}
	
	@GetMapping("/reference")
	public ResponseEntity<LabelsResponse> getLabelsByReference(@RequestParam String reference) 
			throws Exception{
		List<LabelEntity> labels = labelService.getLabelsByReference(reference);
		LabelsResponse response = new LabelsResponse(labels);
		return new ResponseEntity<LabelsResponse>(response,HttpStatus.OK);	
	}	
	
	@PostMapping
	public ResponseEntity<LabelEntity> createLabel(@RequestBody LabelEntity label)
			throws Exception{
		LabelEntity response = labelService.createLabel(label);
		return new ResponseEntity<LabelEntity>(response,HttpStatus.OK);
	}
	
	@PutMapping("/{labelId}")
	public ResponseEntity<LabelEntity> updateLabel(@PathVariable int labelId
			,@RequestBody LabelEntity label) throws Exception{
		
		LabelEntity response = labelService.updateLabel(labelId,label);
		return new ResponseEntity<LabelEntity>(response,HttpStatus.OK);
	}	
	
	@DeleteMapping("/{labelId}")
	public ResponseEntity<LabelEntity> deleteLabel(@PathVariable int labelId)
			throws Exception{
		LabelEntity response = labelService.deleteLabel(labelId);
		return new ResponseEntity<LabelEntity>(response,HttpStatus.OK);
	}	

}
