package com.se.compsecure.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;
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

		String sql = "select * from assessment_details where organization_id=" + organizationId;

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

		String sql = "select * from compliance_header ch join assessment_details ad on ch.compliance_id = ad.compliance_id and ad.assessment_id = '" + assessmentId +"'";
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
		String sql = "select * from compliance_header where organization_id=" + organizationId;

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
				question.setQuestionCode(String.valueOf((Integer)row.get("questionnaire_id")));
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
				+ "	WHERE ch.compliance_name = 'NCT-29Oct8' and ad.assessment_id='37' AND c.subdomain_id = sd.subdomain_id "
				+ "	ORDER By c.control_code";
		
		System.out.println(sql);
		
		return listDomainDetails(sql,assessmentId);
	}
	
	public List<Entry<String, Domain>> getDomainDetailsForCompliance(String complianceName) {
		System.out.println("Inside getDomainDetailsForCompliance");
		
		List<Domain> domainList = new ArrayList<Domain>();		
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
		String sql = "select c.control_code,c.control_value,sd.subdomain_code,sd.subdomain_name,ab.domain_name,ab.domain_code,po.principle,po.objective "
				+ " from 			compsecure_sama.subdomain sd, compsecure_sama.controls c,compsecure_sama.principle_objective po "
				+ " left join		(select domain_id,domain_code,domain_name from 	compsecure_sama.domain d "
				+ " inner join 		compsecure_sama.compliance_header ch "
				+ " on 				ch.compliance_id = d.compliance_id join assessment_details ad on ad.compliance_id = ch.compliance_id where ad.assessment_id='"+assessmentId+"' and ch.compliance_description = '" + complianceDesc +"') ab "
				+ " on 				ab.domain_id where ab.domain_id= sd.domain_id and c.subdomain_id = sd.subdomain_id and po.subdomain_id = sd.subdomain_id group by c.control_id";
		System.out.println(sql);
		
		return listDomainDetails(sql,assessmentId);
	}

	private List<Entry<String, Domain>> listDomainDetails(String sql, String assessmentId) {
		Map<String, Domain> domainMap = new HashMap<String, Domain>();
		
		Map<String, Subdomain> subdomainMap = new HashMap<String, Subdomain>();
		Map<Subdomain, Control> subdomainControlMap = new HashMap<Subdomain, Control>();
		
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
		List<Entry<String , Domain>> domainListFinal = new ArrayList<Entry<String ,Domain>>(setDomain);
	    
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
	public User authenticateUser(User user) {
		
		User authenticatedUser = null;
		try{
		String sql = "select password from login_details where username = ?";
		
		String password = jdbcTemplate.queryForObject(sql,new Object[]{user.getUsername()},String.class);
		
		if(password.equals(user.getPassword())){
			String sqlForUser = "select * from login_details where username = ? and password = ?";
			authenticatedUser = (User)jdbcTemplate.queryForObject(sqlForUser, new Object[]{user.getUsername(),user.getPassword()},new BeanPropertyRowMapper(User.class));
		}
		else{
			return null;
		}
		}catch(Exception ex){
			LOGGER.info(ex.getMessage());
		}
		return authenticatedUser;
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
	
	
	public Boolean checkIfControlExists(String controlCode){
		String countQuery = "select count(*) from control_effectiveness where control_code = '" + controlCode + "'";
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

	public String saveAssessmentDetails(final AssessmentDetails assessmentDetails) {
		
		
	final String complianceId = getComplianceId(assessmentDetails.getComplianceId());	
	LOGGER.info(" Inside saveAssessmentDetails. \t Compliance ID " + complianceId);
	
	String countQuery = "select count(assessment_id) from assessment_details where organization_id= " + assessmentDetails.getOrganizationId() + 
			 " and compliance_id = " + complianceId;
	
	String assessmentIdSQL = "select assessment_id from assessment_details where organization_id= " + assessmentDetails.getOrganizationId() + 
			 " and compliance_id = " + complianceId;
	
	String count = jdbcTemplate.queryForObject(countQuery,String.class);
	
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

	public List<ControlEffectiveness> getControlEffectivenessDetails(String assessmentId, String complianceDesc) {
		
		List<ControlEffectiveness> controlEffectivenessesList = new ArrayList<ControlEffectiveness>();
		
		String sql = "select * from control_effectiveness where assessment_id =" + assessmentId;
		
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
			controlEffectivenessesList.add(controlEffectiveness);
		}
		LOGGER.info(" Control Effectiveness Details " + controlEffectivenessesList.toString());
		return controlEffectivenessesList;
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
			sql = "select compliance_name,compliance_description from compliance_header";
			rows = jdbcTemplate.queryForList(sql);
		}else{
			sql = "select compliance_name,compliance_description from compliance_header where organization_id = ?";
			rows = jdbcTemplate.queryForList(sql,organizationId);
		}
		ComplianceHeader complianceHeader = null; 
		
		Map<String, String> compMap = new HashMap<String, String>();
		
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
		
		System.out.println("Inside getComlianceDefinitionDetails for Compliance Name ");
		
		Control control = null;
		List<Control> controlList = new ArrayList<Control>();
		
		String sql = "select c.control_value,c.control_code from controls c join subdomain s "
					+ " on c.subdomain_id = s.subdomain_id join domain d on s.domain_id = d.domain_id "
					+ " join compliance_header ch on d.compliance_id=ch.compliance_id where ch.compliance_name = ?";
		
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
		
	final String sql = "insert into questionnaire_master (question,control_id,question_code) values (?,?,?)";
		
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

	private String getControlId(String controlLabel) {
		String sql = "select control_id from controls where control_code=?";
		return jdbcTemplate.queryForObject(sql,new Object[]{controlLabel},String.class);
	}

	public void saveComplianceDefinitionData(String complianceName,List<Domain> domains) {
		
		System.out.println(this.getClass().getEnclosingMethod());
		LOGGER.info("METHOD : inside the saveComplianceDefinitionData method");
		
		String sqlForComplianceId = "select compliance_id from compliance_header where compliance_name=?";
		final String complianceId = jdbcTemplate.queryForObject(sqlForComplianceId,new Object[]{complianceName},String.class);
		
		
		for (Iterator iterator = domains.iterator(); iterator.hasNext();) {
			final Domain domain = (Domain) iterator.next();
			String domainId = saveDomainDetails(complianceId, domain);		
			List<Subdomain> subdomains = domain.getSubdomain();
			for (Iterator iterator2 = subdomains.iterator(); iterator2.hasNext();) {
				Subdomain subdomain = (Subdomain) iterator2.next();
				String subdomainId = saveSubdomainDetails(domainId,subdomain);
				List<Control> controls = subdomain.getControl();
				for (Iterator iterator3 = controls.iterator(); iterator3.hasNext();) {
					Control control = (Control) iterator3.next();
					saveControlDetails(subdomainId,control);
				}
			}
		}
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
			LOGGER.info("Values in Controls have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		
		if(noOfRecUpdated!=0){
			String sqlForComplianceId = "select domain_id from domain where domain_name=?";
			domainId = jdbcTemplate.queryForObject(sqlForComplianceId,new Object[]{domain.getDomainName()},String.class);
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
			LOGGER.info("Values in Controls have not been stored. There was some exception while saving data!!");
		    ex.printStackTrace();
		}
		
		if(noOfRecUpdated!=0){
			String sqlForComplianceId = "select subdomain_id from subdomain where subdomain_name=?";
			subdomainId = jdbcTemplate.queryForObject(sqlForComplianceId,new Object[]{subdomain.getSubdomainValue()},String.class);
		}
		
		return subdomainId;
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

}

