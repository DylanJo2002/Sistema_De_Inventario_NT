package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.jasper.ReportService;

@RestController
@RequestMapping("reports")
public class ReportController {
	
	@Autowired
	private ReportService reportController;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json",path = "/bestSeller")
	public ResponseEntity<byte[]> getBestSellers(@RequestParam String startDate, 
			@RequestParam String endDate,@RequestParam int categoryId,
			@RequestParam int top) throws Exception{
		
		byte[] report = reportController.getBestSellers(startDate, endDate,categoryId,top);
		
		ResponseEntity<byte[]>  response= new ResponseEntity<byte[]>(report, HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json",path = "/leastSold")
	public ResponseEntity<byte[]> getLeasSold(@RequestParam String startDate, 
			@RequestParam String endDate,@RequestParam int categoryId,
			@RequestParam int top) throws Exception{
		
		byte[] report = reportController.getLeastSold(startDate, endDate,categoryId,top);
		
		ResponseEntity<byte[]>  response= new ResponseEntity<byte[]>(report, HttpStatus.OK);

		return response;
	}	

	@RequestMapping(method = RequestMethod.GET, produces = "application/json",path = "/underThreshole")
	public ResponseEntity<byte[]> getUnderThreshole(@RequestParam int categoryId)
			throws Exception{
		
		byte[] report = reportController.getUnderThreshole(categoryId);
		
		ResponseEntity<byte[]>  response= new ResponseEntity<byte[]>(report, HttpStatus.OK);

		return response;
	}	
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json",path = "/inventory")
	public ResponseEntity<byte[]> getInventory(@RequestParam int categoryId)
			throws Exception{
		
		byte[] report = reportController.getInventoryExistence(categoryId);
		
		ResponseEntity<byte[]>  response= new ResponseEntity<byte[]>(report, HttpStatus.OK);

		return response;
	}	
}
