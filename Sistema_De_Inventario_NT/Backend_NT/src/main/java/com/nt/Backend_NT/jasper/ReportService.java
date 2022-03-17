package com.nt.Backend_NT.jasper;

import java.io.File;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.lowagie.text.pdf.codec.Base64.InputStream;

import io.github.classgraph.Resource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService {
	@Autowired
	private BestSellersController bestSellersController;
	
	public byte[] getReport()
			throws Exception {
		try {
			Logger log = Logger.getLogger(getClass().toString());
			File file = new ClassPathResource("static/jasper/bestSellers.jasper").getFile();
			JasperReport jasper = (JasperReport) JRLoader.loadObject(file);
			
			JasperPrint print = JasperFillManager.fillReport(jasper, null, bestSellersController);
			
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
		bestSellersController.fillData(dateStart, dateEnd, categoryId, top);
		
		return getReport();
	}
}
