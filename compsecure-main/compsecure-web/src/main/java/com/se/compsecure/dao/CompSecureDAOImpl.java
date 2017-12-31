package com.se.compsecure.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.se.compsecure.model.AssessmentDetails;
import com.se.compsecure.model.ComplianceHeader;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.ControlEffectiveness;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.OrganizationDetails;
import com.se.compsecure.model.Questions;
import com.se.compsecure.model.QuestionsResponse;
import com.se.compsecure.model.Regulator;
import com.se.compsecure.model.Subdomain;
import com.se.compsecure.model.UploadFile;
import com.se.compsecure.model.User;
import com.se.compsecure.model.UserRoles;
import com.se.compsecure.utility.CompSecureConstants;
import com.se.compsecure.utility.ValidityObj;

@Component
public class CompSecureDAOImpl implements CompSecureDAO {

	private JdbcTemplate jdbcTemplate;
	
	private final Logger LOGGER = Logger.getLogger(CompSecureDAOImpl.class.getName());

	public CompSecureDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * TODO to be moved to a utility class
	 */
	private Object convertToString(Date date) {
		if(date==null||date.equals("")){
			return "";
		} else {
			String converted = null;
			Date convertedDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				converted = sdf.format(date);
				convertedDate = sdf.parse(converted);
				System.out.println("Converted Date : " + convertedDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return converted;
		}
	}
	
	public void addFramework(Regulator regulator) {

	}

	/**
	 * assessment_id organization_id assessment_status remarks
	 * assessment_start_date assessment_to_date
	 */
	public List<AssessmentDetails> getAssessmentDetails(String organizationId) {

		String sql = "select * from assessment_details where organization_id=" + organizationId + " order by creation_date desc";

		List<AssessmentDetails> assessmentDetailsList = new ArrayList<AssessmentDetails>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			AssessmentDetails assessmentDetails = new AssessmentDetails();
			assessmentDetails.setAssessmentId((String.valueOf(row.get("assessment_id"))));
			assessmentDetails.setOrganizationId((String.valueOf(row.get("organization_id"))));
			assessmentDetails.setAssessmentStatus((String) row.get("assessment_status"));
			assessmentDetails.setAssessmentDesc((String) row.get("remarks"));
			assessmentDetails.setAssessmentStartDate((String) (convertToString((Date) row.get("assessment_start_date"))));
			assessmentDetails.setAssessmentToDate((String) (convertToString((Date) row.get("assessment_to_date"))));
			assessmentDetails.setAssessmentName((String)row.get("assessment_name"));
			assessmentDetailsList.add(assessmentDetails);
		}
		return assessmentDetailsList;
	}

	/**
	 * compliance details
	 * 
	 */
	public List<ComplianceHeader> getComplianceDetails(String assessmentId) {
		
		LOGGER.info("inside getComplianceDetails method : assessmentId " + assessmentId);
		
		String sql = "select * from compliance_header ch join assessment_details ad on ch.compliance_id = ad.compliance_id and ad.assessment_id = '" + assessmentId +"' order by ch.creation_date desc";
//		String sql = " select * from compsecure_sama.compliance_header ch join compsecure_sama.assessment_details ad "
//				+ "on ch.compliance_id = ad.compliance_id where ad.assessment_id= " + assessmentId;
		
// TODO : Remove this sql string. Redundant. Commented on 30-Sep		
//		String sql = " select ad.assessment_id,c.control_code,c.control_value,s.subdomain_name,s.subdomain_code,po.principle,po.objective,d.domain_name,d.domain_code "+
//					" from controls c join subdomain s on s.subdomain_id = c.subdomain_id " +
//					" join domain d on d.domain_id = s.domain_id join principle_objective po on po.subdomain_id = s.subdomain_id " +
//					" join compliance_header ch on d.compliance_id = ch.compliance_id " +
//					" join assessment_details ad on ad.assessment_id = ch.assessment_id " +
//					" and ad.assessment_id = "+ assessmentId;
		
		if(assessmentId.equals(null)|| assessmentId.isEmpty()){
			sql = "select * from compliance_header";
		}
		
		List<ComplianceHeader> complianceDetailsList = new ArrayList<ComplianceHeader>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			ComplianceHeader complianceHeader = new ComplianceHeader();
			complianceHeader.setComplianceName((String)row.get("compliance_name"));
			complianceHeader.setComplianceDescription((String)row.get("compliance_description"));
			complianceHeader.setComplianceId(Integer.valueOf((String)row.get("compliance_id")));
			complianceDetailsList.add(complianceHeader);
		}
		return complianceDetailsList;
	}
	
	/**
	 *  Return a list of compliance based on organization
	 */
	public List<ComplianceHeader> getComplianceDetailsForOrg(String organizationId) {
		String sql = "select * from compliance_header where organization_id=" + organizationId + " order by creation_date desc";

		List<ComplianceHeader> complianceDetailsList = new ArrayList<ComplianceHeader>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			ComplianceHeader complianceHeader = new ComplianceHeader();
			complianceHeader.setComplianceName((String)row.get("compliance_name"));
			complianceHeader.setComplianceDescription((String)row.get("compliance_description"));
			complianceHeader.setComplianceId((Integer)row.get("compliance_id"));
			complianceDetailsList.add(complianceHeader);
		}
		return complianceDetailsList;
	}
	
	
	// TODO to retrieve all controls

	// to retrieve control object
	public Control getControls(String controlCode) {
		String sql = "select * from controls where control_code='" + controlCode + "'";
		Control control = jdbcTemplate.queryForObject(sql, new RowMapper<Control>() {

			public Control mapRow(ResultSet rs, int rowNum) throws SQLException {
				Control control = new Control();
				control.setControlId(rs.getInt("control_id"));
				control.setControlHeaderName(rs.getString("control_header_name"));
				control.setControlValue(rs.getString("control_value"));
				control.setLevelHeaderId(rs.getInt("level_header_id"));
				control.setControlCode(rs.getString("control_code"));
				return control;
			}
		});
		return control;
	}

	public List<Questions> getQuestions(String controlCode) {

		List<Questions> questionsList = new ArrayList<Questions>();

		String sql = "select * from compsecure_sama.questionnaire_master qm join controls c on qm.control_id = c.control_id where control_code='" + controlCode+"'";
		System.out.println(" in getQuestions :\t" + sql);
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
		
				Questions question = new Questions();
				question.setQuestionCode((String)row.get("question_code"));
				question.setQuestion((String)row.get("question"));
				questionsList.add(question);
			}
		
		return questionsList;
	}
	
	public List<Questions> getQuestions(String controlCode, String assessmentId) {
		List<Questions> questionsList = new ArrayList<Questions>();

		String sql = "select distinct  qr.question_code,qr.question_response,qm.question,c.control_code,qr.assessment_id " 
				+ "from compsecure_sama.question_response qr join compsecure_sama.questionnaire_master qm "
				+ "on qr.question_code = qm.question_code join compsecure_sama.controls c on qm.control_id= c.control_id where c.control_code=? and qr.assessment_id= ?";

		System.out.println(" in getQuestions :\t" + sql);
		
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,new Object[]{controlCode,assessmentId});
		for (Map row : rows) {
				Questions question = new Questions();
				question.setQuestionCode((String)row.get("question_code"));
				question.setQuestion((String)row.get("question"));
				question.setQuestionResponse((String)row.get("question_response"));
				questionsList.add(question);
			}
		
		return questionsList;
	}

	/**
	 * Lists all the organizations that have been registered with the
	 * application
	 */
	public List<OrganizationDetails> getOrganizationList() {

		List<OrganizationDetails> organizationList = new ArrayList<OrganizationDetails>();
		String sql = "select * from organization_details";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			OrganizationDetails orgDetails = new OrganizationDetails();
			orgDetails.setOrganizationId((Integer) row.get("organization_id"));
			orgDetails.setOrganizationName((String) row.get("organization_name"));
			orgDetails.setOrgAdminId((String) row.get("org_admin_id"));
			orgDetails.setCreationDate((String) row.get("org_creation_date"));
			orgDetails.setStatus((String) row.get("status"));
			organizationList.add(orgDetails);
		}
		System.out.println(organizationList.size());
		return organizationList;
	}

	public String getOrganizationBasedOnLogin(String userId) {
		return null;
	}
	
	public List<Entry<String , Domain>> getDomainDetails(String assessmentId,String complianceDesc){
		
		System.out.println("Inside getDomainDetails for assessmentId and ComplianceId ");
		
		List<Domain> domainList = new ArrayList<Domain>();		
//		String sql = "select c.control_code,c.control_value,sd.subdomain_code,sd.subdomain_name,ab.domain_name,ab.domain_code"
//				+ ",po.principle,po.objective "
//				+ " from 			compsecure_sama.subdomain sd, compsecure_sama.controls c,compsecure_sama.principle_objective po "
//				+ " left join		(select domain_id,domain_code,domain_name from 	compsecure_sama.domain d "
//				+ " inner join 		compsecure_sama.compliance_header ch "
//				+ " on 				ch.compliance_id = d.compliance_id join assessment_details ad on ad.compliance_id = ch.compliance_id where ad.assessment_id='"+assessmentId+"' and ch.compliance_name = '" + complianceDesc +"') ab "
//				+ " on 				ab.domain_id where ab.domain_id= sd.domain_id and c.subdomain_id = sd.subdomain_id "
//				+ " and 			po.subdomain_id = sd.subdomain_id "
//				+ "group by c.control_id";
		
		String sql = " SELECT c.control_code, c.control_value, sd.subdomain_code, sd.subdomain_name, d.domain_name, d.domain_code, "
				+ "	po.principle, po.objective FROM controls c, subdomain sd LEFT JOIN principle_objective po "
				+ "	ON sd.subdomain_id = po.subdomain_id JOIN domain d ON d.domain_id = sd.domain_id JOIN compliance_header ch "
				+ "	ON ch.compliance_id = d.compliance_id JOIN assessment_details ad on ad.compliance_id = ch.compliance_id "
				+ "	WHERE ch.compliance_name = '"+complianceDesc+"' and ad.assessment_id='"+assessmentId+"' AND c.subdomain_id = sd.subdomain_id "
				+ "	ORDER By c.control_code";
		
		System.out.println(sql);
		
		return listDomainDetails(sql,assessmentId);
	}
	
	public List<Entry<String, Domain>> getDomainDetailsForCompliance(String complianceName) {
		System.out.println("Inside getDomainDetailsForCompliance");
		
		List<Domain> domainList = new LinkedList();		
//		String sql = "select c.control_code,c.control_value,sd.subdomain_code,sd.subdomain_name,ab.domain_name,ab.domain_code"
//				+ ",po.principle,po.objective "
//				+ " from 			compsecure_sama.subdomain sd, compsecure_sama.controls c,compsecure_sama.principle_objective po "
//				+ " left join		(select domain_id,domain_code,domain_name from 	compsecure_sama.domain d "
//				+ " inner join 		compsecure_sama.compliance_header ch "
//				+ " on 				ch.compliance_id = d.compliance_id " + " where 	ch.compliance_name='"+complianceDesc.trim()+"') ab "
//				+ " on 				ab.domain_id where ab.domain_id= sd.domain_id and c.subdomain_id = sd.subdomain_id "
//				+ "po.subdomain_id = sd.subdomain_id "
//				+ "group by c.control_id";
		String sql = "SELECT c.control_code, c.control_value, sd.subdomain_code, sd.subdomain_name, d.domain_name, d.domain_code, "
				+ "	po.principle, po.objective FROM controls c, subdomain sd LEFT JOIN principle_objective po ON "
				+ "	sd.subdomain_id = po.subdomain_id JOIN domain d ON d.domain_id = sd.domain_id JOIN compliance_header ch ON "
				+ "	ch.compliance_id = d.compliance_id WHERE ch.compliance_name = '"+ complianceName +"' AND c.subdomain_id = sd.subdomain_id "
				+ "	order by c.control_code";
		System.out.println(sql);
//	    SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
		
		return listDomainDetails(sql,null);
	}

public List<Entry<String , Domain>> getCompleteDetails(String assessmentId,String complianceDesc){
		
		System.out.println("Inside getCompleteDetails for assessmentId and ComplianceId ");
		
		List<Domain> domainList = new ArrayList<Domain>();		
//		String sql = "select c.control_code,c.control_value,sd.subdomain_code,sd.subdomain_name,ab.domain_name,ab.domain_code,po.principle,po.objective "
//				+ " from 			compsecure_sama.subdomain sd, compsecure_sama.controls c,compsecure_sama.principle_objective po "
//				+ " left join		(select domain_id,domain_code,domain_name from 	compsecure_sama.domain d "
//				+ " inner join 		compsecure_sama.compliance_header ch "
//				+ " on 				ch.compliance_id = d.compliance_id join assessment_details ad on ad.compliance_id = ch.compliance_id where ad.assessment_id='"+assessmentId+"' and ch.compliance_description = '" + complianceDesc +"') ab "
//				+ " on 				ab.domain_id where ab.domain_id= sd.domain_id and c.subdomain_id = sd.subdomain_id and po.subdomain_id = sd.subdomain_id group by c.control_id";
		
		String sql = " SELECT c.control_code, c.control_value, sd.subdomain_code, sd.subdomain_name, d.domain_name, d.domain_code, "
				+ "	po.principle, po.objective FROM controls c, subdomain sd LEFT JOIN principle_objective po "
				+ "	ON sd.subdomain_id = po.subdomain_id JOIN domain d ON d.domain_id = sd.domain_id JOIN compliance_header ch "
				+ "	ON ch.compliance_id = d.compliance_id JOIN assessment_details ad on ad.compliance_id = ch.compliance_id "
				+ "	WHERE ch.compliance_name = '"+complianceDesc+"' and ad.assessment_id='"+assessmentId+"' AND c.subdomain_id = sd.subdomain_id "
				+ "	ORDER By c.control_code";
		
		System.out.println(sql);
		
		return listDomainDetails(sql,assessmentId);
	}

	private List<Entry<String, Domain>> listDomainDetails(String sql, String assessmentId) {
		Map<String, Domain> domainMap = new LinkedHashMap<String, Domain>();
		
		Map<String, Subdomain> subdomainMap = new LinkedHashMap<String, Subdomain>();
		Map<Subdomain, Control> subdomainControlMap = new LinkedHashMap<Subdomain, Control>();
		
		List<Subdomain> subdomains = new ArrayList<Subdomain>();
		List<Control> controls = new ArrayList<Control>();
	    
	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			Domain domain = new Domain();
			domain.setDomainCode((String)row.get("domain_code"));
			domain.setDomainName((String)row.get("domain_name"));
			
			domainMap.put(domain.getDomainCode(), domain);
			
			Subdomain subdomain = new Subdomain();
			subdomain.setSubdomainCode((String)row.get("subdomain_code"));
			subdomain.setSubdomainValue((String)row.get("subdomain_name"));
			subdomain.setDomainCode((String)row.get("domain_code"));
			subdomain.setPrinciple((String)row.get("principle"));
			subdomain.setObjective((String)row.get("objective"));
		
			subdomainMap.put(subdomain.getSubdomainCode(), subdomain);
			
			Control control = new Control();
			control.setSubdomainId((String)row.get("subdomain_code"));
			control.setControlCode((String)row.get("control_code"));
			control.setControlValue((String)row.get("control_value"));
			
			controls.add(control);
		}
		
		List<Control> controls2 = new ArrayList<Control>();
		
		for(Map.Entry<String, Subdomain> entry : subdomainMap.entrySet()){
		  String subdomainCode = entry.getValue().getSubdomainCode();
			for (Iterator iterator = controls.iterator(); iterator.hasNext();) {
					Control control = (Control)iterator.next();
					if(control.getSubdomainId().equals(subdomainCode)){
						
						ControlEffectiveness controlEffectivenessesForControl = getControlEffectivenessDetailsIfExists(control.getControlCode(),assessmentId);
						if(controlEffectivenessesForControl!=null){
							control.setControlEffectiveness(controlEffectivenessesForControl);
						}
						controls2.add(control);
					}
			}
			entry.getValue().setControl(controls2);
			controls2 = new ArrayList<Control>();
		}
		
		// for testing
		for(Map.Entry<String, Subdomain> entry : subdomainMap.entrySet()){
			System.out.println( " key " + entry.getKey() + " Value " + entry.getValue().getControl().size());
			
			for (Iterator iterator = entry.getValue().getControl().iterator(); iterator.hasNext();) {
				Control control = (Control) iterator.next();
				System.out.println( control.getControlCode());
			}
		}
		
		Set<Entry<String,Subdomain>> set =  subdomainMap.entrySet();
		List<Entry<String , Subdomain>> subDomainList = new ArrayList<Entry<String , Subdomain>>(set);
		
	    for (Iterator iterator = subDomainList.iterator(); iterator.hasNext();) {
			Entry<String, Subdomain> entry = (Entry<String, Subdomain>) iterator.next();
			System.out.println(entry.getValue().getControl().size());
		}
	    
	    for(Map.Entry<String, Domain> entry : domainMap.entrySet()){
	    	String domainCode = entry.getValue().getDomainCode();
			for (Iterator iterator = subDomainList.iterator(); iterator.hasNext();) {
				@SuppressWarnings("unchecked")
				Entry<String, Subdomain> subdomainEntry = (Entry<String, Subdomain>) iterator.next();
					if(subdomainEntry.getValue().getDomainCode().equals(domainCode)){
						subdomains.add(subdomainEntry.getValue());
					}
			}
			entry.getValue().setSubdomain(subdomains);
			subdomains = new ArrayList<Subdomain>();
	    }

	    Set<Entry<String,Domain>> setDomain =  domainMap.entrySet();
		List<Entry<String , Domain>> domainListFinal = new LinkedList<Entry<String ,Domain>>(setDomain);
	    
		return domainListFinal;
	}

	private ControlEffectiveness getControlEffectivenessDetailsIfExists(String controlCode,String assessmentId) {
		
		return geControlEffectivenessDataForControl(controlCode, assessmentId);
		
	}

	public List<Questions> getComplianceQuestions(String complianceDescription,String assessmentId) {
		
		List<Questions> complianceQuestionsList = new ArrayList<Questions>();
		
		 String sql = "select qm.question_code,qm.question from questionnaire_master qm join controls c on c.control_id = qm.control_id join subdomain sd "
				+ "	on sd.subdomain_id = c.subdomain_id join domain d on d.domain_id = sd.domain_id join compliance_header ch on ch.compliance_id = d.compliance_id "
				+ "	where ch.compliance_name =  '"+ complianceDescription +"'";
		
//		if (StringUtils.isNullOrEmpty(assessmentId)) {
//			sql = " select distinct question_code,question from compsecure_sama.questionnaire_master qm join "
//					+ " (select control_code,control_id from compsecure_sama.controls c join "
//					+ " compsecure_sama.subdomain sd on c.subdomain_id = sd.subdomain_id join compsecure_sama.domain d on sd.domain_id = d.domain_id"
//					+ " join compsecure_sama.compliance_header ch on ch.compliance_id = d.compliance_id join compsecure_sama.assessment_details ad on ad.assessment_id"
//					+ " = ch.assessment_id " + " where ch.compliance_name='" + complianceDescription.trim() + "') "
//					+ "abc on abc.control_id = qm.control_id ";
//		} else if(StringUtils.isNullOrEmpty(complianceDescription)){
//			sql = "select distinct qm.question_code, qm.question, qr.question_response, qr.question_remarks FROM compsecure_sama.questionnaire_master qm join "
//					+ "(select c.control_id,c.control_code from controls c join subdomain sd on sd.subdomain_id = c.subdomain_id join domain d on d.domain_id = sd.domain_id "
//					+ " join compliance_header ch on d.compliance_id = ch.compliance_id where ch.compliance_id = (select ch.compliance_id from compliance_header ch "
//					+ " join assessment_details ad on ad.compliance_id = ch.compliance_id where 	ad.assessment_id= "+ assessmentId +")) abc on abc.control_id = qm.control_id "
//					+ " join question_response qr ON qm.question_code = qr.question_code";
//		}
//		else{	
//			sql = getSQLQueryForQuestionnaire(complianceDescription,assessmentId);
//		}
		
		LOGGER.info(" Get Compliance Questions :" + sql);
		
		List<Map<String, Object>> rows = null; 
				rows = jdbcTemplate.queryForList(sql);
		for(Map row : rows){
			Questions questions = new Questions();
			questions.setQuestionCode((String)row.get("question_code"));
			questions.setQuestion((String)row.get("question"));
			questions.setQuestionResponse((String)row.get("question_response"));
			questions.setQuestionRemarks((String)row.get("question_remarks"));
			complianceQuestionsList.add(questions);
		}
		return complianceQuestionsList;
	}

/**
 * REDUNDANT CODE
 */
	
//	private String getSQLQueryForQuestionnaire(String complianceDescription, String assessmentId) {
////		String sql = "SELECT DISTINCT  qm.question_code,   qm.question,    qr.question_response,    qr.question_remarks "
////				+ " FROM    compsecure_sama.questionnaire_master qm        JOIN"
////				+ "    (SELECT control_code, control_id,ad.assessment_id   FROM        compsecure_sama.controls c    JOIN compsecure_sama.subdomain sd ON c.subdomain_id"
////				+ "    JOIN compsecure_sama.domain d ON sd.domain_id    JOIN compsecure_sama.compliance_header ch ON ch.compliance_id"
////				+ "    JOIN compsecure_sama.assessment_details ad ON ad.assessment_id    WHERE        ad.assessment_id = ch.assessment_id"
////				+ "    AND ch.compliance_id = d.compliance_id            AND sd.subdomain_id = c.subdomain_id"
////				+ "    AND ch.compliance_name = '"+ complianceDescription.trim() +"' AND ad.assessment_id = '"+ assessmentId +"') abc ON abc.control_id = qm.control_id"
////				+ "   JOIN    compsecure_sama.question_response qr ON qm.question_code = qr.question_code and abc.assessment_id = qr.assessment_id";
//		
//		String sql = "SELECT DISTINCT  qm.question_code,   qm.question,    qr.question_response,    qr.question_remarks  "
//				+ "FROM    compsecure_sama.questionnaire_master qm JOIN    "
//				+ "(SELECT control_code, control_id   FROM        compsecure_sama.controls c    "
//				+ "JOIN compsecure_sama.subdomain sd ON c.subdomain_id = sd.subdomain_id "
//				+ "JOIN compsecure_sama.domain d ON sd.domain_id =d.domain_id "
//				+ "JOIN compsecure_sama.compliance_header ch ON ch.compliance_id =d.compliance_id "
//				+ "JOIN compsecure_sama.assessment_details ad ON ad.compliance_id = ch.compliance_id AND sd.subdomain_id = c.subdomain_id    "
//				+ "AND ch.compliance_name = '"+ complianceDescription.trim() +"' AND ad.assessment_id = '"+ assessmentId  +"') abc ON abc.control_id = qm.control_id  left JOIN compsecure_sama.question_response qr ON qm.question_code = qr.question_code";
//		
//		return sql;
//	}

	public Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes,String assessmentId) {
		
		Integer count =0;
		
		for (Iterator iterator = questRes.iterator(); iterator.hasNext();) {
			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			if(questionsResponse.getQuestionCode()!=null && !questionsResponse.getQuestionResponse().equals("Select")){
				String sql = "insert into question_response (question_code,question_response,assessment_id,question_remarks) values ('" 	+ questionsResponse.getQuestionCode() + "','"
																  	+ questionsResponse.getQuestionResponse()+"','"
																  	+ assessmentId + "','"	
																  	+ questionsResponse.getQuestionRemarks() +"')";
			
				System.out.println("Save QuestionsResponse query \t" + sql);
				try{
				count = jdbcTemplate.update(sql);
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
			}
			
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public User authenticateUser(User user,String salt) {
		
		User authenticatedUser = null;
		try{
		String sql = "select password from login_details where username = ?";
		
		String password = jdbcTemplate.queryForObject(sql,new Object[]{user.getUsername()},String.class);
		
		String saltedPassword = getHashedValue((password.toLowerCase()+salt));
		
		if(saltedPassword.toLowerCase().equals(user.getPassword())){
			String sqlForUser = "select * from login_details where username = ? and password = ?";
			authenticatedUser = (User)jdbcTemplate.queryForObject(sqlForUser, new Object[]{user.getUsername(),password},new BeanPropertyRowMapper(User.class));
		}
		else{
			return null;
		}
		}catch(Exception ex){
			LOGGER.info(ex.getMessage());
		}
		return authenticatedUser;
	}
	
	
	public static String getHashedValue(String pwd) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-512");
	        byte[] hash = digest.digest(pwd.getBytes("ISO-8859-1"));
	        StringBuffer hexString = new StringBuffer();

		    for (int i = 0; i < hash.length; i++) {
		    	hexString.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
		    }
		    return hexString.toString();
	       
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}

	@SuppressWarnings("unchecked")
	public UserRoles getRole(User user) {
		
		UserRoles userRoles = null;
		
		String sql = " select r.role_id,r.role_desc from compsecure_sama.login_details ld join compsecure_sama.roles r on r.role_id  = ld.role_id and ld.user_id=?";
//		UserRoles role = (UserRoles)jdbcTemplate.queryForObject(sql,new Object[]{user.getUserId()},new BeanPropertyRowMapper<UserRoles>(UserRoles.class));
		
		try{
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,user.getUserId());
		for(Map row : rows){
			userRoles = new UserRoles();
			userRoles.setRoleId((Integer)row.get("role_id"));
			userRoles.setRoleDescription((String)row.get("role_desc"));
		}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return userRoles;
	}

	public List<OrganizationDetails> getOrganizationList(String userId) {
		List<OrganizationDetails> organizationList = new ArrayList<OrganizationDetails>();
		String sql = "select od.organization_id,od.organization_name from 	compsecure_sama.organization_details od join compsecure_sama.login_details ld on ld.organization_id = od.organization_id and ld.user_id =?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,userId);
		for (Map row : rows) {
			OrganizationDetails orgDetails = new OrganizationDetails();
			orgDetails.setOrganizationId((Integer) row.get("organization_id"));
			orgDetails.setOrganizationName((String) row.get("organization_name"));
			organizationList.add(orgDetails);
		}
		System.out.println(organizationList.size());
		return organizationList;
	}

	public void uploadFile(final UploadFile uploadFile,final String docToUpload) {
		String columnName = "";
		
		 if(docToUpload.equals(CompSecureConstants.DOC_EFFECTIVE)){
         	columnName = "doc_eff_evidence";
         }
         else if(docToUpload.equals(CompSecureConstants.IMPL_EFFECTIVE)){
        	 columnName = "impl_eff_evidence";
         }else{
        	 columnName = "rec_eff_evidence";
         }
		 
		final String evidenceTypeName = docToUpload; 
		final String sql = "insert into evidences (assessment_id,control_code,"+ columnName + ",file_name,content_type,evidence_type_name) values (?,?,?,?,?,?)";
		
	        try {
	            synchronized(this) {
	                jdbcTemplate.update(new PreparedStatementCreator() {
	 
	                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                        PreparedStatement statement = con.prepareStatement(sql);
	                        statement.setString(1, uploadFile.getAssessmentId());
	                        statement.setString(2, uploadFile.getControlCode());
	                        statement.setBytes(3, uploadFile.getData());
	                        statement.setString(4, uploadFile.getFileName());
	                        statement.setString(5, uploadFile.getContentType());
	                        statement.setString(6, evidenceTypeName);
//	                        statement.setBytes(4, null);
//	                        statement.setBytes(5, null);
	                        return statement;
	                    }
	                });
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
//		testUpload(3);
	}

	@SuppressWarnings("unchecked")
	private void testUpload(int id) {
		String sql = "select evidence_id,impl_eff_evidence,control_code,file_name,content_type from compsecure_sama.evidences where assessment_id = ?";
		FileOutputStream fileOuputStream = null;
		System.out.println(sql);
		UploadFile file = null;
		try {
               file = (UploadFile) jdbcTemplate.queryForObject(sql, new Object[] {id},
                new RowMapper() {
            	UploadFile fl;
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        fl = new UploadFile();
                        fl.setId(rs.getInt("evidence_id"));
                        fl.setControlCode((String)rs.getString("control_code"));
                        fl.setData(rs.getBytes("impl_eff_evidence"));
                        fl.setFileName(rs.getString("file_name"));
                        fl.setContentType(rs.getString("content_type"));
                        return fl;
                    }
            });
            
//            OutputStream outputStream = new FileOutputStream("D:\\pdf-output\\" + file.getFileName()+ file.getContentType());
            

            try {
                fileOuputStream = new FileOutputStream("D:\\pdf-output\\" + file.getFileName());//+"."+ file.getContentType());
                fileOuputStream.write(file.getData());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOuputStream != null) {
                    try {
                        fileOuputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("File saved");
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	public void saveControlEffectivenessDetails(final ControlEffectiveness controlEffectiveness,final String assessmentId) {
//		throw new NotImplementedException("This method is yet to be implemented!!");
		final StringBuffer sb = new StringBuffer();
		sb.append(" insert into control_effectiveness ( ");
		sb.append("control_code,doc_effectiveness,doc_eff_evidence_id,doc_eff_remarks,");
		sb.append("impl_effectiveness,impl_eff_evidence_id,impl_eff_remarks,");
		sb.append("rec_effectiveness,rec_eff_evidence_id,rec_eff_remarks,assessment_id ) values (?,?,?,?,?,?,?,?,?,?,?)");
		
		try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sb.toString());
                        statement.setString(1, controlEffectiveness.getControlCode());
                        statement.setString(2, controlEffectiveness.getDocEffectiveness());
                        statement.setString(3, controlEffectiveness.getDocEffEvidenceId());
                        statement.setString(4, controlEffectiveness.getDocEffRemarks());
                        statement.setString(5, controlEffectiveness.getImplEffectiveness());
                        statement.setString(6, controlEffectiveness.getImplEffEvidenceId());
                        statement.setString(7, controlEffectiveness.getImplEffRemarks());
                        statement.setString(8, controlEffectiveness.getRecEffectiveness());
                        statement.setString(9, controlEffectiveness.getRecEffEvidenceId());
                        statement.setString(10, controlEffectiveness.getRecEffRemarks());
                        statement.setString(11,assessmentId);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	
	public Integer updateControlEffectivenessDetails(final ControlEffectiveness controlEffectiveness, final String assessmentId) {

		final StringBuffer sb = new StringBuffer();
		sb.append(" update control_effectiveness set ");
		sb.append("doc_effectiveness = ?,doc_eff_evidence_id = ?,doc_eff_remarks = ?,");
		sb.append("impl_effectiveness = ?,impl_eff_evidence_id = ?,impl_eff_remarks = ?,");
		sb.append("rec_effectiveness = ?,rec_eff_evidence_id = ?,rec_eff_remarks = ? where control_code = ? and assessment_id = ?");
		int updatedRows = 0;
		
		try {
		    System.out.println(sb.toString());
            synchronized(this) {
            	updatedRows = jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sb.toString());                        
                        statement.setString(1, controlEffectiveness.getDocEffectiveness());
                        statement.setString(2, controlEffectiveness.getDocEffEvidenceId());
                        statement.setString(3, controlEffectiveness.getDocEffRemarks());
                        statement.setString(4, controlEffectiveness.getImplEffectiveness());
                        statement.setString(5, controlEffectiveness.getImplEffEvidenceId());
                        statement.setString(6, controlEffectiveness.getImplEffRemarks());
                        statement.setString(7, controlEffectiveness.getRecEffectiveness());
                        statement.setString(8, controlEffectiveness.getRecEffEvidenceId());
                        statement.setString(9, controlEffectiveness.getRecEffRemarks());
                        statement.setString(10, controlEffectiveness.getControlCode());
                        statement.setString(11,assessmentId);
                        return statement;
                    }
                });
            }
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return updatedRows;
	}
	
	
	public Boolean checkIfControlExists(String controlCode, String assessmentId){
		String countQuery = "select count(*) from control_effectiveness where control_code = '" + controlCode + "' and assessment_id = '" + assessmentId +"'";
		String count = jdbcTemplate.queryForObject(countQuery,String.class);
		
		if(count.equals("0")){
			return false;
		}else{
			return true;
		}
	}

	public Integer alterComplianceQuestionsResponse(List<QuestionsResponse> questionResponseList) {
		// TODO Auto-generated method stub
		return null;
	}

	public void createCompliance(final ComplianceHeader complianceHeader) {
		
		String countQuery = "select count(compliance_name) from compliance_header where compliance_name = '" + 
							complianceHeader.getComplianceName() +"'";
		String count = jdbcTemplate.queryForObject(countQuery,String.class);
		
		if(count.equals("0")){
			final String sql = "insert into compliance_header (compliance_name,regulator_id,compliance_description,"
					+ "number_of_levels,assessment_id,organization_id) values (?,?,?,?,?,?)";
					
	        try {
	            synchronized(this) {
	                jdbcTemplate.update(new PreparedStatementCreator() {
	 
	                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                        PreparedStatement statement = con.prepareStatement(sql);
	                        statement.setString(1, complianceHeader.getComplianceName());
	                        statement.setString(2, complianceHeader.getRegulatorId());
	                        statement.setString(3, complianceHeader.getComplianceDescription());
	                        statement.setInt(4, 0);
	                        statement.setString(5, "1");
	                        statement.setString(6, "1");
	//                        statement.setBytes(4, null);
	//                        statement.setBytes(5, null);
	                        return statement;
	                    }
	                });
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		}
	}

	public String saveAssessmentDetails(final AssessmentDetails assessmentDetails,String self_assessment_option) {
		
		
	String count = "0";
	final String complianceId = getComplianceId(assessmentDetails.getComplianceId());	
	LOGGER.info(" Inside saveAssessmentDetails. \t Compliance ID " + complianceId);
	
	String countQuery = "select count(assessment_id) from assessment_details where organization_id= " + assessmentDetails.getOrganizationId() + 
			 " and compliance_id = " + complianceId;
	
	String assessmentIdSQL = "select max(assessment_id) from assessment_details where organization_id= " + assessmentDetails.getOrganizationId() + 
			 " and compliance_id = " + complianceId;
	
	if(!self_assessment_option.equals(CompSecureConstants.NEW)){
		 count = jdbcTemplate.queryForObject(countQuery,String.class);
	}
		
	if(count.equals("0")){		
		final String sql = "insert into assessment_details (organization_id,assessment_status,remarks,assessment_start_date,assessment_to_date,assessment_name,compliance_id) values (?,?,?,?,?,?,?)";
		
        try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, assessmentDetails.getOrganizationId());
                        statement.setString(2, assessmentDetails.getAssessmentStatus());
                        statement.setString(3, assessmentDetails.getAssessmentDesc());
                        statement.setString(4, assessmentDetails.getAssessmentStartDate());
                        statement.setString(5, assessmentDetails.getAssessmentToDate());
                        statement.setString(6, assessmentDetails.getAssessmentName());
                        statement.setString(7, complianceId);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	return jdbcTemplate.queryForObject(assessmentIdSQL,String.class);
        
	}

	public List<Questions> getComplianceQuestionsForExistingAssessment(String assessmentId) {
		List<Questions> complianceQuestionsList = new ArrayList<Questions>();
		
		String sql = "select qm.question,qm.question_code,qr.question_response,qr.question_remarks from question_response qr "
				+ "join questionnaire_master qm on qm.question_code = qr.question_code where qr.assessment_id='" + assessmentId +"'";
		
		LOGGER.info(" Get Compliance Questions for Existing Assessment :" + sql);
		
		List<Map<String, Object>> rows = null; 
				rows = jdbcTemplate.queryForList(sql);
		for(Map row : rows){
			Questions questions = new Questions();
			questions.setQuestionCode((String)row.get("question_code"));
			questions.setQuestion((String)row.get("question"));
			questions.setQuestionResponse((String)row.get("question_response"));
			questions.setQuestionRemarks((String)row.get("question_remarks"));
			complianceQuestionsList.add(questions);
		}
		return complianceQuestionsList;
	}

	public List<ControlEffectiveness> getControlEffectivenessDetails(String assessmentId, String controlCode) {
		
		List<ControlEffectiveness> controlEffectivenessesList = new ArrayList<ControlEffectiveness>();
		
		String sql = "select * from control_effectiveness where assessment_id ='" + assessmentId+"' and control_code='" + controlCode +"'";
		
		LOGGER.info(" In getControlEffectivenessDetails :" + sql);
		
		List<Map<String, Object>> rows = null; 
				rows = jdbcTemplate.queryForList(sql);
		for(Map row : rows){
			ControlEffectiveness controlEffectiveness = new ControlEffectiveness();
			controlEffectiveness.setControlCode((String)row.get("control_code"));
			controlEffectiveness.setDocEffectiveness((String)row.get("doc_effectiveness"));
			controlEffectiveness.setDocEffEvidenceId((String)row.get("doc_eff_evidence_id"));
			controlEffectiveness.setDocEffRemarks((String)row.get("doc_eff_remarks"));
			controlEffectiveness.setImplEffectiveness((String)row.get("impl_effectiveness"));
			controlEffectiveness.setImplEffEvidenceId((String)row.get("impl_eff_evidence_id"));
			controlEffectiveness.setImplEffRemarks((String)row.get("impl_eff_remarks"));
			controlEffectiveness.setRecEffectiveness((String)row.get("rec_effectiveness"));
			controlEffectiveness.setRecEffEvidenceId((String)row.get("rec_eff_evidence_id"));
			controlEffectiveness.setRecEffRemarks((String)row.get("rec_eff_remarks"));
			List<String> docEffectiveEvidenceFileNames = getDocEffectiveFileNames((String)row.get("control_code"),assessmentId);
			List<String> recEffectiveEvidenceFileNames = getRecEffectiveFileNames((String)row.get("control_code"),assessmentId);
			List<String> implEffectiveEvidenceFileNames = getImplEffectiveFileNames((String)row.get("control_code"),assessmentId);
			controlEffectiveness.setDocEffEvidences(docEffectiveEvidenceFileNames);
			controlEffectiveness.setRecEffEvidences(recEffectiveEvidenceFileNames);
			controlEffectiveness.setImplEffEvidences(implEffectiveEvidenceFileNames);
			controlEffectivenessesList.add(controlEffectiveness);
		}
		LOGGER.info(" Control Effectiveness Details " + controlEffectivenessesList.toString());
		return controlEffectivenessesList;
	}

	private List<String> getDocEffectiveFileNames(String controlCode,String assessmentId) {
		
		List<String> docEffectiveEvidenceFileNames = new ArrayList<String>();
		String sql = "select * from evidences where assessment_id ='" + assessmentId+"' and control_code='" + controlCode +"' and evidence_type_name='docEff'";
		LOGGER.info(" In getControlEffectivenessDetails :" + sql);
		List<Map<String, Object>> rows = null; 
				rows = jdbcTemplate.queryForList(sql);
		for(Map row : rows){
			String fileName = (String)row.get("file_name");
			docEffectiveEvidenceFileNames.add(fileName);
		}
		return docEffectiveEvidenceFileNames;
	}
	
	private List<String> getRecEffectiveFileNames(String controlCode,String assessmentId) {
			
			List<String> recEffectiveEvidenceFileNames = new ArrayList<String>();
			String sql = "select * from evidences where assessment_id ='" + assessmentId+"' and control_code='" + controlCode +"' and evidence_type_name='recEff'";
			LOGGER.info(" In getControlEffectivenessDetails :" + sql);
			List<Map<String, Object>> rows = null; 
					rows = jdbcTemplate.queryForList(sql);
			for(Map row : rows){
				String fileName = (String)row.get("file_name");
				recEffectiveEvidenceFileNames.add(fileName);
			}
			return recEffectiveEvidenceFileNames;
		}
	
	private List<String> getImplEffectiveFileNames(String controlCode,String assessmentId) {
		
		List<String> implEffectiveEvidenceFileNames = new ArrayList<String>();
		String sql = "select * from evidences where assessment_id ='" + assessmentId+"' and control_code='" + controlCode +"' and evidence_type_name='implEff'";
		LOGGER.info(" In getControlEffectivenessDetails :" + sql);
		List<Map<String, Object>> rows = null; 
				rows = jdbcTemplate.queryForList(sql);
		for(Map row : rows){
			String fileName = (String)row.get("file_name");
			implEffectiveEvidenceFileNames.add(fileName);
		}
		return implEffectiveEvidenceFileNames;
	}

	public String getComplianceId(String complianceDescription) {
		
		LOGGER.info(" Inside getComplianceId. \t Compliance Name " + complianceDescription);
		
		String sql = "select compliance_id from compliance_header where compliance_name = ?";
		return jdbcTemplate.queryForObject(sql,new Object[]{complianceDescription},String.class);
	}

	public void saveComplianceDefinitionData(ComplianceHeader complianceHeader) {
		
	}

	public String addDomain(Domain domain, Integer complianceId) {
		
		String sql="";
		String check = "select count(*) from domain where domain_name = ?";
		String result = jdbcTemplate.queryForObject(check,new Object[]{domain.getDomainName()},String.class);
		
		if(result==null || result.equals("0")){
			sql = "insert into domain (domain_name,domain_code,compliance_id) values (?,?,?)";
		}else{
			sql = "update domain set domain_name =?, domain_code = ? where domain_id = " + result;
		}
		
		executeQuery(domain, sql, complianceId);       
        
        String sqlForDomainId = "select max(domain_id) from domain where domain_code = ?";
       
		return jdbcTemplate.queryForObject(sqlForDomainId,new Object[]{domain.getDomainCode()},String.class);
	}

	private void executeQuery(final Domain domain,final String sql,final Integer complianceId) {
		 try {
	            synchronized(this) {
	                jdbcTemplate.update(new PreparedStatementCreator() {
	                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                        PreparedStatement statement = con.prepareStatement(sql);
	                        statement.setString(1, domain.getDomainName());
	                        statement.setString(2, domain.getDomainCode());
	                        statement.setString(3, complianceId.toString());
	                        return statement;
	                    }
	                });
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	}

	public String addSubdomain(final Subdomain subdomain, final String domainId) {

		final String sql = "insert into subdomain (subdomain_name,subdomain_code,domain_id) values (?,?,?)";
		
        try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, subdomain.getSubdomainValue());
                        statement.setString(2, subdomain.getSubdomainCode());
                        statement.setString(3, domainId);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        String sqlForDomainId = "select subdomain_id from subdomain where subdomain_code = ?";
		String subdomainId =  jdbcTemplate.queryForObject(sqlForDomainId,new Object[]{subdomain.getSubdomainCode()},String.class);
		
		updatePrincipleAndObjectives(subdomain.getPrinciple(),subdomain.getObjective(),subdomainId);
		
		return subdomainId;
	}

	private void updatePrincipleAndObjectives(final String principle, final String objective, final String subdomainId) {

		final String sql = "insert into principle_objective (principle,objective,subdomain_id) values (?,?,?)";
		
        try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, principle);
                        statement.setString(2, objective);
                        statement.setString(3, subdomainId);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	public void addControl(final Control control,final String subdomainId) {

		final String sql = "insert into controls (control_value,control_code,subdomain_id) values (?,?,?)";
		
        try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, control.getControlValue());
                        statement.setString(2, control.getControlCode());
                        statement.setString(3, subdomainId.toString());
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
        	LOGGER.info("Values in Controls have not been stored. There was some exception while saving data!!");
            ex.printStackTrace();
        }
	}

	public Map<String, String> getCompliances(String organizationId) {
		String sql = "";
		List<Map<String, Object>> rows = null;
		
		if(organizationId.equals("0")){
			sql = "select compliance_name,compliance_description from compliance_header order by creation_date desc";
			rows = jdbcTemplate.queryForList(sql);
		}else{
			sql = "select compliance_name,compliance_description from compliance_header where organization_id = ? order by creation_date desc";
			rows = jdbcTemplate.queryForList(sql,organizationId);
		}
		ComplianceHeader complianceHeader = null; 
		
		Map<String, String> compMap = new LinkedHashMap<String, String>();
		
		try{
			
			for(Map row : rows){
				complianceHeader = new ComplianceHeader();
				complianceHeader.setComplianceName((String)row.get("compliance_name"));
				complianceHeader.setComplianceDescription((String)row.get("compliance_description"));
				compMap.put(complianceHeader.getComplianceName(), complianceHeader.getComplianceDescription());
			}
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			
			return compMap;
	}

	public String getAssessmentId(String complianceDesc) {
		String sql = "SELECT ad.assessment_id FROM assessment_details ad join compliance_header ch on ch.compliance_id = ad.compliance_id "
					 + " where ch.compliance_name = ?";
		
		return jdbcTemplate.queryForObject(sql,new Object[]{complianceDesc},String.class);
	}

	public List<Entry<String, Domain>> getComlianceDefinitionDetails(String complianceName) {
		
		System.out.println("Inside getComlianceDefinitionDetails for Compliance Name ");
		
		String sql = "select c.control_code,c.control_value,sd.subdomain_code,sd.subdomain_name,ab.domain_name,ab.domain_code,po.principle,po.objective "
				+ " from 			compsecure_sama.subdomain sd, compsecure_sama.controls c,compsecure_sama.principle_objective po "
				+ " left join		(select domain_id,domain_code,domain_name from 	compsecure_sama.domain d "
				+ " inner join 		compsecure_sama.compliance_header ch "
				+ " on 				ch.compliance_id = d.compliance_id where ch.compliance_name = '" + complianceName +"') ab "
				+ " on 				ab.domain_id where ab.domain_id= sd.domain_id and c.subdomain_id = sd.subdomain_id and po.subdomain_id = sd.subdomain_id group by c.control_id";
		System.out.println(sql);
		
		return listDomainDetails(sql,null);
	}

	public List<Control> getControlsForQuestions(String complianceName) {
		
		System.out.println("Inside getControlsForQuestions for Compliance Name " + complianceName);
		
		Control control = null;
		List<Control> controlList = new ArrayList<Control>();
		
		String sql = "select c.control_value,c.control_code from controls c join subdomain s "
					+ " on c.subdomain_id = s.subdomain_id join domain d on s.domain_id = d.domain_id "
					+ " join compliance_header ch on d.compliance_id=ch.compliance_id where ch.compliance_name = ?";
		
		LOGGER.info("SQL Query for getControlsForQuestions : " +sql);
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,complianceName);
		try{
			
			for(Map row : rows){
				control = new Control();
				control.setControlCode((String)row.get("control_code"));
				control.setControlValue((String)row.get("control_value"));
				controlList.add(control);
			}
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		return controlList;
	}

	public void saveQuestions(List<Questions> questionsList) {
		
	}

	public void saveQuestions(String controlLabel,final String questionCode,final String question) {
		
		final String controlId = getControlId(controlLabel);
		
		String insertSql = "";
	
		String checkForExistingQuestions = "select count(*) from questionnaire_master where question_code = '" + questionCode+"'";
	
		String count = jdbcTemplate.queryForObject(checkForExistingQuestions,String.class);
		
		if(count.equals("0")){
			insertSql = "insert into questionnaire_master (question,control_id,question_code) values (?,?,?)";
		}else{
			updateQuestions(controlId,questionCode,question);
			return;
		}
		
		final String sql = insertSql;
		
        try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, question);
                        statement.setString(2, controlId);
                        statement.setString(3, questionCode);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	private void updateQuestions(final String controlId, final String questionCode, final String question) {
		final String insertSql = "update questionnaire_master set question=?, question_code=? where control_id = ?";
		
		try {
            synchronized(this) {
                jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(insertSql);
                        statement.setString(1, question);
                        statement.setString(2, questionCode);
                        statement.setString(3, controlId);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
	}

	private String getControlId(String controlLabel) {
		String sql = "select max(control_id) from controls where control_code=?";
		LOGGER.info("inside the getControlId method " + sql + controlLabel);
		return jdbcTemplate.queryForObject(sql,new Object[]{controlLabel},String.class);
	}

	public String saveComplianceDefinitionData(String complianceName,List<Domain> domains) {
		
		String result = "success";
		
		System.out.println(this.getClass().getEnclosingMethod());
		LOGGER.info("METHOD : inside the saveComplianceDefinitionData method");
		
		try{
		String sqlForComplianceId = "select compliance_id from compliance_header where compliance_name=?";
		final String complianceId = jdbcTemplate.queryForObject(sqlForComplianceId,new Object[]{complianceName},String.class);
		
		for (Iterator iterator = domains.iterator(); iterator.hasNext();) {
			final Domain domain = (Domain) iterator.next();
			String domainId = saveDomainDetails(complianceId, domain);		
			List<Subdomain> subdomains = domain.getSubdomain();
			for (Iterator iterator2 = subdomains.iterator(); iterator2.hasNext();) {
				Subdomain subdomain = (Subdomain) iterator2.next();
				String subdomainId = saveSubdomainDetails(domainId,subdomain);
				savePrincipleObjectiveDetails(subdomainId,subdomain);
				List<Control> controls = subdomain.getControl();
				for (Iterator iterator3 = controls.iterator(); iterator3.hasNext();) {
					Control control = (Control) iterator3.next();
					saveControlDetails(subdomainId,control);
				}
			}
		}
		}catch(Exception ex){
			result = "failed";
			LOGGER.info(result);
			
		}
		return result;
	}
	

	private String saveDomainDetails(final String compliance_id, final Domain domain) {
		
		final String sql = "insert into domain (domain_name,domain_code,compliance_id) values (?,?,?)";
		
		int noOfRecUpdated = 0;
		String domainId = "";
		
		try {
		    synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setString(1, domain.getDomainName());
		                statement.setString(2, domain.getDomainCode());
		                statement.setString(3, compliance_id);
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in saveDomainDetails have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		
		if(noOfRecUpdated!=0){
			String sqlForComplianceId = "select max(domain_id) from domain where domain_name=? and compliance_id=?";
			try{
				domainId = jdbcTemplate.queryForObject(sqlForComplianceId,new Object[]{domain.getDomainName(),compliance_id},String.class);
			}catch(Exception ex){
				LOGGER.info("in the saveDomainDetails method - query for sqlForComplianceId failed " + sqlForComplianceId);
				ex.printStackTrace();
			}
		}
		
		return domainId;
	}
	
	private String saveSubdomainDetails(final String domainId, final Subdomain subdomain) {
		String subdomainId = "";
		
		final String sql = "insert into subdomain (subdomain_name,subdomain_code,domain_id) values (?,?,?)";
		int noOfRecUpdated = 0;
		try {
		    synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setString(1, subdomain.getSubdomainValue());
		                statement.setString(2, subdomain.getSubdomainCode());
		                statement.setString(3, domainId);
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in Subdomain -"+ subdomain.getSubdomainCode() +"- have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		
		if(noOfRecUpdated!=0){
			String sqlForComplianceId = "select subdomain_id from subdomain where subdomain_name=? and domain_id = ?";
			try{
				subdomainId = jdbcTemplate.queryForObject(sqlForComplianceId,new Object[]{subdomain.getSubdomainValue(), domainId},String.class);
			}catch(Exception ex){
				LOGGER.info("in the saveSubdomainDetails method - query for subdomainId failed " + sqlForComplianceId);
				ex.printStackTrace();
			}
		}
		
		return subdomainId;
	}
	
	
	private void savePrincipleObjectiveDetails(final String subdomainId, final Subdomain subdomain) {
		final String sql = "insert into principle_objective (principle,objective,subdomain_id) values (?,?,?)";
		int noOfRecUpdated = 0;
		try {
		    synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setString(1, subdomain.getPrinciple());
		                statement.setString(2, subdomain.getObjective());
		                statement.setString(3, subdomainId);
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in Subdomain -"+ subdomain.getSubdomainCode() +"- have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
	}
	
	
	private void saveControlDetails(final String subdomainId, final Control control) {
		
		final String sql = "insert into controls (control_value,control_code,subdomain_id) values (?,?,?)";
		int noOfRecUpdated = 0;
		
		try {
		    synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setString(1, control.getControlValue());
		                statement.setString(2, control.getControlCode());
		                statement.setString(3, subdomainId);
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in Controls have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		LOGGER.info("METHOD : Number of control records updated : " + noOfRecUpdated);
	}

	public Boolean doesAssessmentIdExist(String assessmentId) {

		boolean returnValue = false;
		
		LOGGER.info(" Inside doesAssessmentIdExist. \t Assessment ID " + assessmentId);
		
		String countQuery = "select count(*) from control_effectiveness where assessment_id = " + assessmentId;
		
		String count = jdbcTemplate.queryForObject(countQuery,String.class);
		
		if(Integer.valueOf(count) > 0){
			returnValue = true;
		}else{
			returnValue =  false;
		}
		return returnValue;
	}

	//public List<ControlEffectiveness> geControlEffectivenessDataForControl(String controlCode, String assessmentId) {
	
	public ControlEffectiveness geControlEffectivenessDataForControl(String controlCode, String assessmentId) {
		LOGGER.info("Inside geControlEffectivenessDataForControl");
		
		ControlEffectiveness controlEffectiveness = null;
		List<ControlEffectiveness> controlEffList = new ArrayList<ControlEffectiveness>();
		
		String sql = "select * from control_effectiveness where control_code = ? and assessment_id = ?";
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,controlCode,assessmentId);
		
		try{
		
			for(Map row : rows){
				controlEffectiveness = new ControlEffectiveness();
				controlEffectiveness.setControlCode((String)row.get("control_code"));
				controlEffectiveness.setDocEffectiveness((String)row.get("doc_effectiveness"));
				controlEffectiveness.setDocEffEvidenceId((String)row.get("doc_eff_evidence_id"));
				controlEffectiveness.setDocEffRemarks((String)row.get("doc_eff_remarks"));
				controlEffectiveness.setImplEffectiveness((String)row.get("impl_effectiveness"));
				controlEffectiveness.setImplEffEvidenceId((String)row.get("impl_eff_evidence_id"));
				controlEffectiveness.setImplEffRemarks((String)row.get("impl_eff_remarks"));
				controlEffectiveness.setRecEffectiveness((String)row.get("rec_effectiveness"));
				controlEffectiveness.setRecEffEvidenceId((String)row.get("rec_eff_evidence_id"));
				controlEffectiveness.setRecEffRemarks((String)row.get("rec_eff_remarks"));
				
				controlEffList.add(controlEffectiveness);
			}
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		if(controlEffList.size()>0){
			ControlEffectiveness controlEffectiveness2 = controlEffList.get(0);
			addEvidences(controlEffectiveness2,controlCode,assessmentId);
			return controlEffectiveness2;
		}else{
			return null;
		}
	}

	private void addEvidences(ControlEffectiveness controlEffectiveness2,String controlCode, String assessmentId) {
		
		List<String> docEffEvidenceList = new ArrayList<String>();
		List<String> implEffEvidenceList = new ArrayList<String>();
		List<String> recEffEvidenceList = new ArrayList<String>();
		
		
		String sql = "select * from evidences where control_code = ? and assessment_id = ?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,controlCode,assessmentId);

		try{
			for(Map row : rows){
				String evidenceName = (String)row.get("file_name");
				String nameOfEvidenceTypeUploaded = (String)row.get("evidence_type_name");
				if(nameOfEvidenceTypeUploaded.equals(CompSecureConstants.DOC_EFFECTIVE)){
					docEffEvidenceList.add(evidenceName);
				}else if(nameOfEvidenceTypeUploaded.equals(CompSecureConstants.IMPL_EFFECTIVE)){
					implEffEvidenceList.add(evidenceName);
				}else{
					recEffEvidenceList.add(evidenceName);
				}
			}
			
			controlEffectiveness2.setDocEffEvidences(docEffEvidenceList);
			controlEffectiveness2.setImplEffEvidences(implEffEvidenceList);
			controlEffectiveness2.setRecEffEvidences(recEffEvidenceList);
			
		}catch(Exception ex){
			LOGGER.info(ex.getMessage());
		}
	}

	public List<Entry<String, Domain>> getExistingComplianceDetails(String complianceName) {
	System.out.println("Inside getExistingDomainDetailsForCompliance");
		
		String sql = "SELECT c.control_code, c.control_value, sd.subdomain_code, sd.subdomain_name, d.domain_name, d.domain_code, "
				+ "	po.principle, po.objective FROM controls c, subdomain sd LEFT JOIN principle_objective po ON "
				+ "	sd.subdomain_id = po.subdomain_id JOIN domain d ON d.domain_id = sd.domain_id JOIN compliance_header ch ON "
				+ "	ch.compliance_id = d.compliance_id WHERE ch.compliance_name = '"+ complianceName +"' AND c.subdomain_id = sd.subdomain_id "
				+ " order by c.control_code";
		System.out.println(sql);
		
		return listDomainDetails(sql,null);
	}

	public UploadFile getFile(String filename, String assessmentId) {
		
		
		List<UploadFile> fileList = new ArrayList<UploadFile>();
		
		String sql = "select * from evidences where file_name = ? and assessment_id = ?";
		
		try{
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,new Object[]{filename,assessmentId});
			for (Map row : rows) {
				UploadFile uploadedFile = new UploadFile();
				uploadedFile.setAssessmentId((String)row.get("assessment_id").toString());
				uploadedFile.setFileName((String)row.get("file_name"));
				uploadedFile.setContentType((String)row.get("content_type"));
				uploadedFile.setData((byte[])row.get("doc_eff_evidence"));
				fileList.add(uploadedFile);
			}
			
		}catch(Exception ex){
			System.out.println("Error : " + ex.getMessage());
		}
		
		return fileList.get(0);
	}

	@Override
	public String enterMaturityDefinitionValues(final String complianceId, final String rangeFrom, final String rangeTo) {
		
		String checkForExistingQuestions = "select count(*) from maturity_definition where compliance_id = '" + complianceId+"'";
		String count = jdbcTemplate.queryForObject(checkForExistingQuestions,String.class);
		
		if(!count.equals("0")){
			String noOfRecordsUpdated = updateMaturityDefinitionValues(complianceId,rangeFrom,rangeTo);
			return noOfRecordsUpdated;
		}
		
		final String sql = "insert into maturity_definition (compliance_id,mat_def_from,mat_def_to) values (?,?,?)";
		int noOfRecUpdated = 0;
		
		System.out.println(sql);
		
		try {
		    synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setString(1, complianceId);
		                statement.setString(2, rangeFrom);
		                statement.setString(3, rangeTo);
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in maturity_definition have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		LOGGER.info("METHOD : Number of control records updated : " + noOfRecUpdated);
		
		return String.valueOf(noOfRecUpdated);
	}

	private String updateMaturityDefinitionValues(final String complianceId, final String rangeFrom, final String rangeTo) {
		
		final String updateQuery = "update maturity_definition set mat_def_from = ?, mat_def_to=? where compliance_id=?";
		int noOfRecUpdated = 0;
		
		try {
            synchronized(this) {
            		noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement(updateQuery);
                        statement.setString(1, rangeFrom);
                        statement.setString(2, rangeTo);
                        statement.setString(3, complianceId);
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
        	LOGGER.info("Values in maturity_definition have not been updated. There was some exception while saving data!!");
		    ex.printStackTrace();
        }
		
		LOGGER.info("METHOD : Number of control records updated : " + noOfRecUpdated);
		return String.valueOf(noOfRecUpdated);
	}

	@Override
	public String[] getMaturityLevels(String complianceId) {
		String [] range = new String[2];
		
		String sql = "select mat_def_from,mat_def_to from maturity_definition where compliance_id = ?";
		try{
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,new Object[]{complianceId});
			for (Map row : rows) {
				range[0] = (String)row.get("mat_def_from");
				range[1] = (String)row.get("mat_def_to");
			}
		}catch(Exception ex){
			System.out.println("Error : " + ex.getMessage());
		}
		return range;
	}
	
	@Override
	public List<User> getUsersInOrg(String orgId,Integer userId) {
		
		List<User> users = new LinkedList<User>();
		Date date = Calendar.getInstance().getTime();
		
		String sql = "select u.username,o.organization_name,r.role_desc,u.email_id,u.creation_date,u.status from organization_details o join login_details u "
				+ "on o.organization_id = u.organization_id join roles r on r.role_id = u.role_id "
				+ "where u.organization_id = ? order by creation_date desc, u.status";
		try{
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,new Object[]{orgId});
			for (Map row : rows) {
				User user = new User();
				user.setUsername((String)row.get("username"));
				user.setOrganizationName((String)row.get("organization_name"));
				user.setEmailId((String)row.get("email_id"));
				user.setCreationDate(getDate((Date)row.get("creation_date")));
				user.setStatus((String)row.get("status"));
				UserRoles roles = new UserRoles();
				roles.setRoleDescription((String)row.get("role_desc"));
				user.setRole(roles);
				users.add(user);
			}
		}catch(Exception ex){
			System.out.println("Error : " + ex.getMessage());
		}
		
		return users;
	}
	
//	private String formatDate(String date) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//		LOGGER.info(dateFormat.format(date));
//		return dateFormat.format(date);
//	}

	private String getDate(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		return dateFormat.format(date);
	}

	@Override
	public Integer createNewUser(final User userDetails) {
		
		final String sql = "insert into login_details (user_id,username,password,organization_id,role_id,email_id,status) values (?,?,?,?,?,?,?)";
		int noOfRecUpdated = 0;
		
		try {
		
		String currentId = jdbcTemplate.queryForObject("select max(user_id) from login_details",String.class);
		userDetails.setUserId(Integer.valueOf(currentId)+1);
		
		String orgId = jdbcTemplate.queryForObject("select organization_id from organization_details where organization_name='"+userDetails.getOrganizationName()+"'",String.class);
		
		userDetails.setUserId(Integer.valueOf(currentId)+1);
		userDetails.setOrganizationId(orgId);
		
		System.out.println(sql);
		System.out.println(userDetails.getPassword());
		
		    synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {
 
		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setInt(1, Integer.valueOf(userDetails.getUserId()));
		                statement.setString(2, userDetails.getUsername());
		                statement.setString(3, userDetails.getPassword());
		                statement.setString(4, userDetails.getOrganizationId());
		                statement.setInt(5, Integer.valueOf(userDetails.getRole().getRoleId()));
		                statement.setString(6,userDetails.getEmailId());
		                statement.setString(7,userDetails.getStatus());
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in the login table have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		LOGGER.info("METHOD : Number of control records updated : " + noOfRecUpdated);
		return noOfRecUpdated;
	}
	
	@Override
	public Integer updateUserDetails(final User userDetails) {
		final String sql = "update login_details set username = ?,organization_id = ?, email_id=?,role_id=?, status=? where user_id = ?";
		int noOfRecUpdated = 0;
		
		String orgId = jdbcTemplate.queryForObject("select organization_id from organization_details where organization_name='"+userDetails.getOrganizationName()+"'",String.class);
		userDetails.setOrganizationId(orgId);
		
		String userId = jdbcTemplate.queryForObject("select user_id from login_details where username='"+userDetails.getUsername()+"'",String.class);
		userDetails.setUserId(Integer.valueOf(userId));
		
		System.out.println(sql);
		try{
		 synchronized(this) {
		    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {

		            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		                PreparedStatement statement = con.prepareStatement(sql);
		                statement.setString(1, userDetails.getUsername());
		                statement.setString(2, userDetails.getOrganizationId());
		                statement.setString(3, userDetails.getEmailId());
		                statement.setInt(4, Integer.valueOf(userDetails.getRole().getRoleId()));
		                statement.setString(5, userDetails.getStatus());
		                statement.setInt(6, Integer.valueOf(userDetails.getUserId()));
		                return statement;
		            }
		        });
		    }
		} catch (Exception ex) {
			LOGGER.info("Values in the login table have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		LOGGER.info("METHOD : Number of control records updated : " + noOfRecUpdated);
		return null;
	}
	
	@Override
	public Boolean checkAdminGenPassword(String username, String password) {
		LOGGER.info("inside the checkAdminGenPassword - DAOIMPL Class");
		try {
			String db_password = jdbcTemplate.queryForObject("select password from login_details where username='" + username + "'", String.class);
			if (db_password.equals(password)) {
				return true;
			}
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}
		return false;
	}
	
	@Override
	public String getUserId(String username) {
		LOGGER.info("inside the getUserId - DAOIMPL Class");
		try {
			String userId = jdbcTemplate.queryForObject("select user_id from login_details where username='" + username + "'", String.class);
			if (!StringUtils.isEmpty(userId)) {
				return userId;
			}
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}
		return null;
	}
	
	@Override
	public String saveChangedPasswordDetails(final ValidityObj validityObj) {
		LOGGER.info("inside the saveChangedPasswordDetails - DAOIMPL Class");
		
		final String passwordUpdateSql = "update login_details set password = ? where username = ?";
		final String insertSecurityQuestions = "insert into security_questions(security_question,security_answer,user_id) values (?,?,?)";
		
		int noOfRecUpdated = 0;
		
		final String userId = getUserId(validityObj.getUserName());
		
		try{
			 synchronized(this) {
			    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {

			            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			                PreparedStatement statement = con.prepareStatement(passwordUpdateSql);
			                statement.setString(1, validityObj.getChangedPassword());
			                statement.setString(2, validityObj.getUserName());
			                return statement;
			            }
			        });
			    }
			 
			 synchronized(this) {
			    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {

			            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			                PreparedStatement statement = con.prepareStatement(insertSecurityQuestions);
			                statement.setString(1, validityObj.getSecurityQuestion());
			                statement.setString(2, validityObj.getSecurityAnswer());
			                statement.setInt(3, Integer.valueOf(userId));
			                return statement;
			            }
			        });
			    }
			} catch (Exception ex) {
				LOGGER.info("Values in the login table have not been stored. There was some exception while saving data!!");
			    ex.printStackTrace();
			}
		
		return String.valueOf(noOfRecUpdated);
	}

	@Override
	public String getSecurityQuestion(String username) {
		LOGGER.info("inside the getSecurityQuestion - DAOIMPL Class");
		
		String sqlSecurityQues = "select security_question from security_questions sq join login_details ld "
								+"on ld.user_id=sq.user_id where ld.username= '" + username+"'";
		
		try {
			String securityQuestion = jdbcTemplate.queryForObject(sqlSecurityQues, String.class);
			if (!StringUtils.isEmpty(securityQuestion)) {
				return securityQuestion;
			}
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}
		return null;
	}
	
	@Override
	public Boolean verifyAnswer(String username, String answer) {
		LOGGER.info("inside the verifyAnswer - DAOIMPL Class");
		
		String sqlSecAns="select security_answer from security_questions sq join login_details ld "
								+"on ld.user_id=sq.user_id where ld.username= '" + username +"'";
		
		try {
			String dbAnswer = jdbcTemplate.queryForObject(sqlSecAns,String.class);
			if (dbAnswer.equals(answer)) {
				return true;
			}
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}
		return false;
	}
	
	@Override
	public String savePassword(final String pwd, final String username) {
		
		LOGGER.info("inside the savePassword - DAOIMPL Class");
		
		int noOfRecUpdated = 0;
		final String passwordUpdateSql = "update login_details set password = ? where username = ?";
		
		try{
			 synchronized(this) {
			    	noOfRecUpdated = jdbcTemplate.update(new PreparedStatementCreator() {

			            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			                PreparedStatement statement = con.prepareStatement(passwordUpdateSql);
			                statement.setString(1, pwd);
			                statement.setString(2, username);
			                return statement;
			            }
			        });
			    }
		}catch(Exception ex){
			LOGGER.info(ex.getMessage());
		}
		if(noOfRecUpdated>0){
			return "done";
		}
		return null;
	}
	
}

