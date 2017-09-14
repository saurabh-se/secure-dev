package com.se.compsecure.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import javax.sql.DataSource;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.mysql.cj.core.util.StringUtils;
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

@Component
public class CompSecureDAOImpl implements CompSecureDAO {

	private JdbcTemplate jdbcTemplate;

	public CompSecureDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * TODO to be moved to a utility class
	 *
	 */
	private Object convertToString(Date date) {
		String converted = null;
		Date convertedDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			converted = sdf.format(date);
			convertedDate = sdf.parse(converted);
			System.out.println("Converted Date : " + convertedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(convertedDate);
		cal.add(Calendar.MONTH, 1);
		System.out.println(cal.toString());
		return cal;
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
			assessmentDetails.setRemarks((String) row.get("remarks"));
			assessmentDetails.setAssessmentStartDate((Calendar) row.get(convertToString((Date) row.get("assessment_start_date"))));
			assessmentDetails.setAssessmentToDate((Calendar) row.get(convertToString((Date) row.get("assessment_to_date"))));
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

		String sql = "select * from compliance_header where assessment_id=" + assessmentId;

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
	
	public List<Entry<String , Domain>> getDomainDetails(String assessmentId){
		
		System.out.println("Inside getDomainDetails");
		
		List<Domain> domainList = new ArrayList<Domain>();		
		String sql = "select c.control_code,c.control_value,sd.subdomain_code,sd.subdomain_name,ab.domain_name,ab.domain_code,po.principle,po.objective "
				+ " from 			compsecure_sama.subdomain sd, compsecure_sama.controls c,compsecure_sama.principle_objective po "
				+ " left join		(select domain_id,domain_code,domain_name from 	compsecure_sama.domain d "
				+ " inner join 		compsecure_sama.compliance_header ch "
				+ " on 				ch.compliance_id = d.compliance_id " + " where 			ch.assessment_id='"+assessmentId+"') ab "
				+ " on 				ab.domain_id where ab.domain_id= sd.domain_id and c.subdomain_id = sd.domain_id and po.subdomain_id = sd.subdomain_id group by c.control_id";
		
//	    SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
		
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

	public List<Questions> getComplianceQuestions(String complianceDescription,String assessmentId) {
		
		List<Questions> complianceQuestionsList = new ArrayList<Questions>();
		String sql = "";
		
		if (StringUtils.isNullOrEmpty(assessmentId)) {
			sql = " select distinct question_code,question from compsecure_sama.questionnaire_master qm join "
					+ " (select control_code,control_id from compsecure_sama.controls c join "
					+ " compsecure_sama.subdomain sd on c.subdomain_id join compsecure_sama.domain d on sd.domain_id "
					+ " join compsecure_sama.compliance_header ch on ch.compliance_id join compsecure_sama.assessment_details ad on ad.assessment_id"
					+ " where ad.assessment_id = ch.assessment_id " + " and ch.compliance_id = d.domain_id "
					+ " and sd.subdomain_id = c.subdomain_id and ch.compliance_description='"
					+ complianceDescription.trim() + "') "
					+ "abc on abc.control_id = qm.control_id ";
		} else {

			sql = " select distinct qm.question_code,qm.question,qr.question_response,qr.question_remarks from compsecure_sama.questionnaire_master qm join "
					+ " (select control_code,control_id from compsecure_sama.controls c join "
					+ " compsecure_sama.subdomain sd on c.subdomain_id join compsecure_sama.domain d on sd.domain_id "
					+ " join compsecure_sama.compliance_header ch on ch.compliance_id join compsecure_sama.assessment_details ad on ad.assessment_id"
					+ " where ad.assessment_id = ch.assessment_id " + " and ch.compliance_id = d.domain_id "
					+ " and sd.subdomain_id = c.subdomain_id and ch.compliance_description='"
					+ complianceDescription.trim() + "' and ad.assessment_id='" + assessmentId + "') "
					+ "abc on abc.control_id = qm.control_id join compsecure_sama.question_response qr on qm.question_code = qr.question_code";
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
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

	public Integer saveComplianceQuestionsResponse(List<QuestionsResponse> questRes) {
		
		Integer count =0;
		
		for (Iterator iterator = questRes.iterator(); iterator.hasNext();) {
			QuestionsResponse questionsResponse = (QuestionsResponse) iterator.next();
			if(questionsResponse.getQuestionCode()!=null && !questionsResponse.getQuestionResponse().equals("Select")){
				String sql = "insert into question_response (question_code,question_response,assessment_id,question_remarks) values ('" 	+ questionsResponse.getQuestionCode() + "','"
																  	+ questionsResponse.getQuestionResponse()+"','"
																  	+ 1  + "','"	
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
		String sql = "select password from login_details where username = ?";
		
		String password = jdbcTemplate.queryForObject(sql,new Object[]{user.getUsername()},String.class);
		
		if(password.equals(user.getPassword())){
			String sqlForUser = "select * from login_details where username = ? and password = ?";
			authenticatedUser = (User)jdbcTemplate.queryForObject(sqlForUser, new Object[]{user.getUsername(),user.getPassword()},new BeanPropertyRowMapper(User.class));
		}
		
		else{
			return null;
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
		
		 if(docToUpload.equals("docEff")){
         	columnName = "doc_eff_evidence";
         }
         else if(docToUpload.equals("implEff")){
        	 columnName = "impl_eff_evidence";
         }else{
        	 columnName = "rec_eff_evidence";
         }
		final String sql = "insert into evidences (assessment_id,control_code,"+ columnName + ",file_name,content_type) values (?,?,?,?,?)";
		
	        try {
	            synchronized(this) {
	                jdbcTemplate.update(new PreparedStatementCreator() {
	 
	                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                        PreparedStatement statement = con.prepareStatement(sql);
	                        statement.setInt(1, uploadFile.getAssessmentId());
	                        statement.setString(2, uploadFile.getControlCode());
	                        statement.setBytes(3, uploadFile.getData());
	                        statement.setString(4, uploadFile.getFileName());
	                        statement.setString(5, uploadFile.getContentType());
//	                        statement.setBytes(4, null);
//	                        statement.setBytes(5, null);
	                        return statement;
	                    }
	                });
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		testUpload(3);
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

	public void saveControlEffectivenessDetails(final ControlEffectiveness controlEffectiveness) {
//		throw new NotImplementedException("This method is yet to be implemented!!");
		final StringBuffer sb = new StringBuffer();
		sb.append(" insert into control_effectiveness ( ");
		sb.append("control_code,doc_effectiveness,doc_eff_evidence_id,doc_eff_remarks,");
		sb.append("impl_effectiveness,impl_eff_evidence_id,impl_eff_remarks,");
		sb.append("rec_effectiveness,rec_eff_evidence_id,rec_eff_remarks ) values (?,?,?,?,?,?,?,?,?,?)");
		
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
                        return statement;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}


//select distinct question_code,question from compsecure_sama.questionnaire_master qm join
//(select control_code,control_id from compsecure_sama.controls c join 
//			  compsecure_sama.subdomain sd on c.subdomain_id join compsecure_sama.domain d on sd.domain_id
//              join compsecure_sama.compliance_header ch on ch.compliance_id join compsecure_sama.assessment_details ad on ad.assessment_id
//              where ad.assessment_id = ch.assessment_id
//              and ch.compliance_id = d.domain_id 
//              and sd.subdomain_id = c.subdomain_id and ch.compliance_name='Cyber Security Framework' and ad.assessment_id='1') abc on abc.control_id = qm.control_id
