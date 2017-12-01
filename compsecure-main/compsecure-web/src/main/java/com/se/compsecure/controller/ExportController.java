package com.se.compsecure.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.Questions;
import com.se.compsecure.model.Subdomain;
import com.se.compsecure.service.CompSecureService;

@Controller
public class ExportController {

	private static final Logger LOGGER = Logger.getLogger(ComplianceController.class.getName());

	@Autowired
	private CompSecureService compSecureService;
	
	@RequestMapping(value="/exportToExcel")
	@ResponseBody
    public String exportToExcel(@RequestParam("assessmentId") String assessmentId,@RequestParam("complianceName") String complianceName,
    		@RequestParam("complianceId") String complianceId,HttpSession httpSession) {
		       
		LOGGER.info("***************** INSIDE exportToExcel **********************");
		
		List<Entry<String, Domain>> domainDetailsList = null;
		
		
//		complianceId = (String)httpSession.getAttribute("complianceDesc");
		System.out.println("Inside getCompleteDetails");
		System.out.println("Compliance Id " + complianceId);
		System.out.println("Assessment Id " + assessmentId);
		
		if(assessmentId.isEmpty()){
			assessmentId = (String)httpSession.getAttribute("assessmentId");
		}
		domainDetailsList = compSecureService.getCompleteDetails(assessmentId,complianceName);
		
		List<Domain> domainList = new ArrayList<Domain>();

		for (Iterator iterator = domainDetailsList.iterator(); iterator.hasNext();) {
			Entry<String, Domain> entry = (Entry<String, Domain>) iterator.next();
			domainList.add(entry.getValue());
		}
		
		String fileName = writeToExcel(domainList,complianceName,assessmentId);
		
		Gson gson = new Gson();
		return gson.toJson(fileName);
    }

	private String writeToExcel(List<Domain> domainList, String complianceName, String assessmentId) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Complete Compliance Details");
		int rowCount = 0;
		
		Row row0 = sheet.createRow(++rowCount);
		int columnCount0 = 0;
		
		Cell cellDomainCode = row0.createCell(++columnCount0);
		cellDomainCode.setCellValue("Domain Code");
		Cell cellDomainName = row0.createCell(++columnCount0);
		cellDomainName.setCellValue("Domain Name");
		Cell cellSubdomainCode = row0.createCell(++columnCount0);
		cellSubdomainCode.setCellValue("Subdomain Code");
		Cell cellSubdomainName = row0.createCell(++columnCount0);
		cellSubdomainName.setCellValue("Subdomain Name");
		Cell cellPrinciple = row0.createCell(++columnCount0);
		cellPrinciple.setCellValue("Principle");
		Cell cellObjective = row0.createCell(++columnCount0);
		cellObjective.setCellValue("Objective");
		Cell cellControlCode = row0.createCell(++columnCount0);
		cellControlCode.setCellValue("Control Code");
		Cell cellControlValue = row0.createCell(++columnCount0);
		cellControlValue.setCellValue("Control Value");
		Cell cellQuestionCode = row0.createCell(++columnCount0);
		cellQuestionCode.setCellValue("Question Code");
		Cell cellQuestion = row0.createCell(++columnCount0);
		cellQuestion.setCellValue("Question");
		
		for (Iterator iterator = domainList.iterator(); iterator.hasNext();) {
			Domain domain = (Domain) iterator.next();
			Row row = sheet.createRow(++rowCount);
			int columnCount = 0;
			Cell cell = row.createCell(++columnCount);
			cell.setCellValue((String) domain.getDomainCode());
			Cell cell1 = row.createCell(++columnCount);
			cell1.setCellValue(domain.getDomainName());
			Cell cell2 = row.createCell(++columnCount);
			
			List<Subdomain> subdomain = domain.getSubdomain();
			int subdomainCount=0;
			int subdomainColumnCount = columnCount;
			for (Iterator iterator2 = subdomain.iterator(); iterator2.hasNext();) {
				if(subdomainCount>0){
					row = sheet.createRow(++rowCount);
					columnCount = subdomainColumnCount;
				}
				Subdomain subdomain2 = (Subdomain) iterator2.next();
				cell2.setCellValue(subdomain2.getSubdomainCode());
				Cell cell3 = row.createCell(++columnCount);
				cell3.setCellValue(subdomain2.getSubdomainValue());
				Cell cell4 = row.createCell(++columnCount);
				cell4.setCellValue(subdomain2.getPrinciple());
				Cell cell5 = row.createCell(++columnCount);
				cell5.setCellValue(subdomain2.getObjective());
				
				subdomainCount++;
				
				List<Control> control = subdomain2.getControl();
				int controlCount = 0;
				int controlColumnCount = columnCount;
				for (Iterator iterator3 = control.iterator(); iterator3.hasNext();) {
					if(controlCount>0){
						row = sheet.createRow(++rowCount);
						columnCount = controlColumnCount;
					}
					Control control2 = (Control) iterator3.next();
					Cell cell6 = row.createCell(++columnCount);
					cell6.setCellValue(control2.getControlCode());
					Cell cell7 = row.createCell(++columnCount);
					cell7.setCellValue(control2.getControlValue());
					
					controlCount++;
					
					List<Questions> questions = compSecureService.getQuestions(control2.getControlCode(), assessmentId);
					int questionCount = 0;
					int questionColumnCount = columnCount;
					
					if(questions!=null){
					
					for (Iterator iterator4 = questions.iterator(); iterator4.hasNext();) {
						if(questionCount>0){
							row = sheet.createRow(++rowCount);
							columnCount = questionColumnCount;
						}
						Questions questions2 = (Questions) iterator4.next();
						Cell cell8 = row.createCell(++columnCount);
						cell8.setCellValue(questions2.getQuestionCode());
						Cell cell9 = row.createCell(++columnCount);
						cell9.setCellValue(questions2.getQuestion());
						questionCount++;
					}
					}
				}
			}
			
			
		}
		FileOutputStream outputStream =null;
		File yourFile = null;
		try {
			 yourFile = new File("D:/ComplianceDetailsFor-"+complianceName+"-"+assessmentId+".xlsx");
			yourFile.createNewFile();
			 outputStream = new FileOutputStream(yourFile,false);
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				outputStream.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return yourFile.getPath();
	}
	
}
