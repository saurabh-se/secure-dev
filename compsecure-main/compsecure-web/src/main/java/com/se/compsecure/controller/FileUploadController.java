package com.se.compsecure.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.se.compsecure.model.UploadFile;
import com.se.compsecure.service.CompSecureService;

@Controller
public class FileUploadController {
	
	private static final Logger LOGGER = Logger.getLogger(FileUploadController.class.getName());
	
	@Autowired
	private CompSecureService compSecureService; 
	
	@Autowired
	private UploadFile uploadFile;
	
	
	@RequestMapping(value = "/doUpload/{docUploadType}/{controlCode}/", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile fileUpload,@PathVariable String docUploadType,
    		@PathVariable String controlCode,HttpSession httpSession) throws Exception {
        
				String assessmentId = (String)httpSession.getAttribute("assessmentId");
		
                LOGGER.info("Name: " + fileUpload.getOriginalFilename().toString());
                LOGGER.info("Content-Type: " + fileUpload.getContentType());
                LOGGER.info("Saving file: " + fileUpload.getBytes());
                LOGGER.info("** \t Doc Upload Type - "  + docUploadType);
                
                compSecureService.save(fileUpload,docUploadType,assessmentId,controlCode);             
                
                Gson gson = new Gson();
                return gson.toJson("success");
  
    }   
	
	
	@RequestMapping(value = "/getFile")
	public @ResponseBody File getFile(@RequestParam("filename") String filename, HttpSession httpSession,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		System.out.println("FILE to download : " + filename);
		String assessmentId = (String)httpSession.getAttribute("assessmentId");
		UploadFile uploadFile = compSecureService.getUploadedFile(filename,assessmentId);
		
		InputStream targetStream = new ByteArrayInputStream(uploadFile.getData());
				
		String fileType = FilenameUtils.getExtension(uploadFile.getFileName());
		System.out.println("extn : " + fileType);
		
		String fileName = uploadFile.getFileName().replace("."+fileType, "");
		System.out.println("FILENAME " + fileName);
		
		File tempFile = File.createTempFile(fileName, uploadFile.getContentType());
	    tempFile.deleteOnExit();
	    
	    FileOutputStream out = new FileOutputStream(tempFile);
	    
	    
	        response.setContentType(request.getServletContext().getMimeType(uploadFile.getFileName()));
            response.addHeader("Content-Disposition", "attachment; filename="+ uploadFile.getFileName());
            try
            {
            	response.getOutputStream().write(uploadFile.getData());
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        
	    return null;
        
	}
	
	
//	@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
//    public String handleFileUpload(HttpServletRequest request,
//            @RequestParam CommonsMultipartFile[] fileUpload) throws Exception {
//          
//        if (fileUpload != null && fileUpload.length > 0) {
//            for (CommonsMultipartFile aFile : fileUpload){
//                  
//                System.out.println("Saving file: " + aFile.getOriginalFilename());
//
////                document.setName(multipartFile.getOriginalFilename());
////                document.setDescription(fileBucket.getDescription());
////                document.setType(multipartFile.getContentType());
////                document.setContent(multipartFile.getBytes());
//                
//                
//                uploadFile.setFileName(aFile.getOriginalFilename());
//                uploadFile.setData(aFile.getBytes());
//                compSecureService.save(uploadFile);               
//            }
//        }
//  
//        return "Success";
//    }   

}
