package com.nt.Backend_NT.controllers;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<byte[]> getBestSellers(@RequestParam String startDate, 
			@RequestParam String endDate,@RequestParam int categoryId,
			@RequestParam int top) throws Exception{
		
		byte[] report = reportController.getBestSellers(startDate, endDate,categoryId,top);
		
		ResponseEntity<byte[]>  response= new ResponseEntity<byte[]>(report, HttpStatus.OK);

		return response;
	}

}
