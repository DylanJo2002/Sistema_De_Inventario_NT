package com.nt.Backend_NT.jasper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.ProductReportEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.jasper.model.Product;
import com.nt.Backend_NT.repositories.ProductRepository;
import com.nt.Backend_NT.repositories.ReportRepository;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Controller
public class BestSellersController implements JRDataSource {

	
	@Autowired
	private ReportRepository productRepository;
	private List<ProductReportEntity> products;
	private int index;
	@PostConstruct
	private void init() {
		index = -1;
	}
	
	public void cleanData() {
		index = -1;
	}
	
	public void fillData(String dateStart, String dateEnd, int categoryId, int top){
		
		if(!dateStart.isBlank() && !dateEnd.isBlank()) {
			
			if(categoryId != 0) {
				products = productRepository.findProductReportByDatesAndCategory(dateStart,dateEnd,categoryId,top);
				
			}else {
				
				
			}
			
		}
		
		if(dateStart.isBlank() && dateEnd.isBlank()) {
			
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
			case "name": value = products.get(index).getProducto(); break; 	
			case "description": value = products.get(index).getDescripcion(); break;
			case "cu": value = products.get(index).getCostoxunidad()+""; break; 	
			case "threshold": value = products.get(index).getUmbral()+""; break; 	
			case "category": value = products.get(index).getCategoria(); break; 	
			case "amount": value = products.get(index).getVentas()+""; break; 	

		}
		
		
		return value;
	}

}
