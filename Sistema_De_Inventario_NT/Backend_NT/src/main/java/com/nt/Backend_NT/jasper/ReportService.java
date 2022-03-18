package com.nt.Backend_NT.jasper;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.lowagie.text.pdf.codec.Base64.InputStream;

import io.github.classgraph.Resource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService {
	@Autowired
	private ProductReportController bestSellersController;
	@Autowired
	private InventoryReportController inventoryReportController;
	
	public byte[] getReport(String report, Map<String, Object> keys, JRDataSource source)
			throws Exception {
		try {
			Logger log = Logger.getLogger(getClass().toString());
			File file = new ClassPathResource(report).getFile();
			JasperReport jasper = (JasperReport) JRLoader.loadObject(file);
			
			JasperPrint print = JasperFillManager.fillReport(jasper, keys, source);
			
			byte[] pdfBytes = JasperExportManager.exportReportToPdf(print);
			
			return pdfBytes;
			
			
		}catch(Exception e) {
			throw new Exception(String.format("Ocurrió el siguiente error al tratar se generar el reporte de "
					.concat("los productos mejores ventidos: %s"), e.getMessage()));
		}
	}
	

	public byte[] getBestSellers(String dateStart, String dateEnd, int categoryId, int top)
			throws Exception {
		bestSellersController.cleanData();
		bestSellersController.fillDataBestSeller(dateStart, dateEnd, categoryId, top);
		Map<String, Object> keys = bestSellersController.getReportKeys();
		return getReport("static/jasper/bestSellers.jasper",keys,bestSellersController);
	}
	
	public byte[] getLeastSold(String dateStart, String dateEnd, int categoryId, int top)
			throws Exception {
		bestSellersController.cleanData();
		bestSellersController.fillDataLeastSold(dateStart, dateEnd, categoryId, top);
		Map<String, Object> keys = bestSellersController.getReportKeys();
		return getReport("static/jasper/leastSold.jasper",keys,bestSellersController );
	}
	
	public byte[] getUnderThreshole(int categoryId)
			throws Exception {
		inventoryReportController.cleanData();
		inventoryReportController.fillData(categoryId);
		Map<String, Object> keys = inventoryReportController.getReportKeys();
		return getReport("static/jasper/underThreshole.jasper",keys, inventoryReportController);
	}
	
	public byte[] getInventoryExistence(int categoryId)
			throws Exception {
		inventoryReportController.cleanData();
		inventoryReportController.fillInventoryExistenceData(categoryId);
		Map<String, Object> keys = inventoryReportController.getReportKeys();
		return getReport("static/jasper/underThreshole.jasper",keys, inventoryReportController);
	}
	
}
