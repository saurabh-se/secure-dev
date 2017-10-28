package com.se.compsecure.utility;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.se.compsecure.model.Questions;

public class CompSecureUtil {

//	public static void main(String[] args) {
//		final String string = "qControlLabel=16.1.1-1&controlQuestionCode=16.1.1&controlQuestion=Q16&controlQuestionCode=16.1.2"
//						+ "	&controlQuestion=Q17&controlQuestionCode=16.1.3&controlQuestion=Q18&qControlLabel=1.1-1.1&controlQuestionCode=1.1&controlQuestion=Q3&controlQuestionCode=1.2&controlQuestion=Q4";
//		CompSecureUtil breakingQuestions = new CompSecureUtil();
//		String jsonString = breakingQuestions.getKeyValuePair(string);
//		System.out.println(jsonString);
//	}
	
	public List<QuestionsUtil> getKeyValuePair(String str){
		
		List<QuestionsUtil> list = new ArrayList<QuestionsUtil>();
		Map<String, List<QuestionsUtil>> map = new HashMap<String, List<QuestionsUtil>>();
		QuestionsUtil newObj = null;
		String qControlLabel = "";
		
		String [] splitString = str.replace("\"", "").split("&");
		for (int i = 0; i < splitString.length; i++) {
			System.out.println(splitString[i]);
			
			String [] keyValue = splitString[i].split("=");	
			if(keyValue[0].equals("qControlLabel")){
				newObj = new QuestionsUtil();
				newObj.setqControlLabel(keyValue[1]);				
				qControlLabel = keyValue[1];
			}
			if(keyValue[0].equals("controlQuestionCode")){
				if(newObj==null){
					newObj = new QuestionsUtil();
					newObj.setqControlLabel(decode(qControlLabel));
				}
				newObj.setControlQuestionCode(decode(keyValue[1]));
			}
			if(keyValue[0].equals("controlQuestion")){
				newObj.setControlQuestion(decode(keyValue[1]));
			}
			if(newObj!=null){
				if(newObj.getqControlLabel()!=null & newObj.getControlQuestionCode()!=null & newObj.getControlQuestion()!=null){
				list.add(newObj);
				newObj = null;
				}
			}
		}
		
		return list;
	}

	private String decode(String string) {
		return URLDecoder.decode(string);
	}
}
