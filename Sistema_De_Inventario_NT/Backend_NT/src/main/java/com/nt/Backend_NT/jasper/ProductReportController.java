package com.nt.Backend_NT.jasper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.ProductReportEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.jasper.model.Product;
import com.nt.Backend_NT.repositories.ProductRepository;
import com.nt.Backend_NT.repositories.ReportRepository;
import com.nt.Backend_NT.services.CategoryService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Controller
public class ProductReportController implements JRDataSource {

	
	@Autowired
	private ReportRepository productRepository;
	private List<ProductReportEntity> products;
	@Autowired
	private CategoryService  categoryService;
	private Map<String,Object> reportKeys;
	private int index;
	@PostConstruct
	private void init() {
		index = -1;
		products = null;
	}
	
	public void cleanData() {
		index = -1;
	}
	
	public void fillDataBestSeller(String dateStart, String dateEnd, int categoryId, int top) throws Exception{
		generateKeys(dateStart, dateEnd, categoryId, top);
		if(!dateStart.isBlank() && !dateEnd.isBlank()) {
			
			if(categoryId != 0) {
				products = productRepository.findProductReportByDatesAndCategory(dateStart,dateEnd,categoryId,top);
				
			}else {
				
				products = productRepository.findProductReportByDates(dateStart,dateEnd,top);
			}
		}
		
		if(dateStart.isBlank() && dateEnd.isBlank()) {
			
			if(categoryId != 0) {
				products = productRepository.findProductReportByCategory(categoryId,top);
				
			}else {
				
				products = productRepository.findProductReport(top);
			}
		}
		
	}

	public void fillDataLeastSold(String dateStart, String dateEnd, int categoryId, int top) throws Exception{
		generateKeys(dateStart, dateEnd, categoryId, top);
		if(!dateStart.isBlank() && !dateEnd.isBlank()) {
			
			if(categoryId != 0) {
				products = productRepository.findProductReportByDatesAndCategory2(dateStart,dateEnd,categoryId,top);
				
			}else {
				
				products = productRepository.findProductReportByDates2(dateStart,dateEnd,top);
			}
		}
		
		if(dateStart.isBlank() && dateEnd.isBlank()) {
			
			if(categoryId != 0) {
				products = productRepository.findProductReportByCategory2(categoryId,top);
				
			}else {
				
				products = productRepository.findProductReport2(top);
			}
		}
		
	}
	
	@Override
	public boolean next() throws JRException {
		index++;
		return index<products.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Object value = null;
		
		switch (jrField.getName()) {
			case "numer": value = index+1+""; break; 	
			case "reference": value = products.get(index).getReferencia(); break; 	
			case "name": value = products.get(index).getNombre(); break; 	
			case "description": value = products.get(index).getDescripcion(); break;
			case "cu": value = products.get(index).getCostoxunidad()+""; break; 	
			case "threshold": value = products.get(index).getUmbral()+""; break; 	
			case "category": value = products.get(index).getCategoria(); break; 	
			case "amount": value = products.get(index).getVentas()+""; break; 	

		}
		
		
		return value;
	}

	public void generateKeys(String dateStart, String dateEnd, int categoryId, int top)
			throws Exception{
		Map<String,Object> keys = new HashMap<String,Object>();
		String categoryName = "TODAS";
		if(categoryId != 0) {
			categoryName = categoryService.getCategory(categoryId).getNombre();
		}
		keys.put("startDate", dateStart);
		keys.put("endDate", dateEnd);
		keys.put("category",categoryName+"");
		keys.put("top",top+"");
		
		reportKeys =  keys;
	}

	public Map<String, Object> getReportKeys() {
		return reportKeys;
	}
	
}
