package com.nt.Backend_NT.jasper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.nt.Backend_NT.entities.UnderThresholdEntity;
import com.nt.Backend_NT.repositories.UnderThresholdReportRepository;
import com.nt.Backend_NT.services.CategoryService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Controller
public class InventoryReportController implements JRDataSource {

	@Autowired
	private UnderThresholdReportRepository underThresholdReportRepository;
	private List<UnderThresholdEntity> products;
	@Autowired
	private CategoryService categoryService;
	private Map<String, Object> reportKeys;
	private int index;
	private UnderThresholdEntity defaultRecord;

	@PostConstruct
	private void init() {
		index = -1;
		defaultRecord = new UnderThresholdEntity();
	}

	public void cleanData() {
		index = -1;
		products = null;
	}

	public void fillData(int categoryId) throws Exception {
		generateKeys(categoryId);

		if (categoryId != 0) {
			products = underThresholdReportRepository.findInventoryByCategory(categoryId);

		} else {

			products = underThresholdReportRepository.findInventory();
		}
		
		if(products.size() == 0) {
			products.add(defaultRecord);
		}

	}

	@Override
	public boolean next() throws JRException {
		index++;
		return index < products.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Object value = null;

		switch (jrField.getName()) {
		case "numer":
			value = index + 1 + "";
			break;
		case "reference":
			value = products.get(index).getReferencia();
			break;
		case "name":
			value = products.get(index).getNombre();
			break;
		case "description":
			value = products.get(index).getDescripcion();
			break;
		case "cu":
			value = products.get(index).getCostoxunidad() + "";
			break;
		case "threshold":
			value = products.get(index).getUmbral() + "";
			break;
		case "category":
			value = products.get(index).getCategoria();
			break;
		case "amount":
			value = products.get(index).getCantidad() + "";
			break;

		}

		return value;
	}

	public void generateKeys(int categoryId) throws Exception {
		Map<String, Object> keys = new HashMap<String, Object>();
		String categoryName = "TODAS";
		if (categoryId != 0) {
			categoryName = categoryService.getCategory(categoryId).getNombre();
		}
		keys.put("category", categoryName);
		reportKeys = keys;
	}

	public Map<String, Object> getReportKeys() {
		return reportKeys;
	}

}
