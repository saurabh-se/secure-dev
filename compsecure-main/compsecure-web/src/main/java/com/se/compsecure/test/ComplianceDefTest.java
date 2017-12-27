package com.se.compsecure.test;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.Subdomain;

public class ComplianceDefTest {

	public static void main(String[] args) {
		String str = "domain_code=CDC1&domain_value=CDV1&sub_code=SDC-CDC1&subdomain_value=SDV-CDC1&principle=P-CDC1&objective=O-CDC1&control_code=C1.1&control_value=CDC1&control_code=C1.2&control_value=CDC2&sub_code=SDC2-CDC1&subdomain_value=SDV2-CDC1&principle=P2-CDC1&objective=O2-CDC1&control_code=C2.1&control_value=Subd2&control_code=C2.2&control_value=Subd2&domain_code=CDC2&domain_value=CDV2&sub_code=SDC1-CDC2&subdomain_value=SDV1-CDC2&principle=P2&objective=O2&control_code=C3.1&control_value=Control1-CDC2&control_code=C3.2&control_value=Control2-CDC2";

		ComplianceDefTest complianceDefTest = new ComplianceDefTest();
		complianceDefTest.generateComplianceDefKeyValPair(str);
	}

	private void generateComplianceDefKeyValPair(String serArrayStr) {

		Domain domain = null;
		Subdomain subdomain = null;
		Control control = null;

		String subdomainCode = "";
		serArrayStr = serArrayStr + "&eol";

		System.out.println(serArrayStr);

		Map<String, List<Domain>> domainMap = new HashMap<String, List<Domain>>();
		Map<String, List<Subdomain>> subdomainMap = new HashMap<String, List<Subdomain>>();

		List<Domain> domainList = new ArrayList<Domain>();
		List<Control> controlList = new ArrayList<Control>();
		List<Subdomain> subdomainList = null;

		String urlDecoded = URLDecoder.decode(serArrayStr);
		String[] splitString = urlDecoded.split("&");
		for (int i = 0; i < splitString.length; i++) {
			// System.out.println(splitString[i]);

			String[] keyValue = splitString[i].split("=");
			System.out.println(i + "\t " + keyValue[0] + "\t");

			if (keyValue[0].equals("eol")) {
				subdomain.setControl(controlList);
				subdomainList.add(subdomain);
				domain.setSubdomain(subdomainList);
				domainList.add(domain);
				break;
			}

			if (keyValue[0].equals("domain_code")) {
				if (domain != null) {
					subdomain.setControl(controlList);
					subdomainList.add(subdomain);
					domain.setSubdomain(subdomainList);
					domainList.add(domain);
				}
				domain = new Domain();
				domain.setDomainCode(keyValue[1]);
				subdomain = null;
				subdomainList = new ArrayList<Subdomain>();
				control = null;
			}
			if (keyValue[0].equals("domain_value")) {
				domain.setDomainName(keyValue[1]);
			}
			if (keyValue[0].equals("sub_code")) {
				if (subdomain != null) {
					System.out.println("** Adding to the subdomainlist **");
					subdomain.setControl(controlList);
					subdomainList.add(subdomain);
				}
				subdomain = new Subdomain();
				subdomain.setSubdomainCode(keyValue[1]);
				control = null;
				controlList = new ArrayList<Control>();
			}
			if (keyValue[0].equals("subdomain_value")) {
				subdomain.setSubdomainValue(keyValue[1]);
			}
			if (keyValue[0].equals("principle")) {
				subdomain.setPrinciple(keyValue[1]);
			}
			if (keyValue[0].equals("objective")) {
				subdomain.setObjective(keyValue[1]);
			}
			if (keyValue[0].equals("control_code")) {
				control = new Control();
				control.setControlCode(keyValue[1]);
			}
			if (keyValue[0].equals("control_value")) {
				control.setControlValue(keyValue[1]);
			}
			if (subdomain != null) {
				if (subdomain.getSubdomainCode() != null && !subdomain.getSubdomainCode().isEmpty()) {
					if (control != null) {
						if (control.getControlCode() != null && !control.getControlCode().isEmpty()
								&& control.getControlValue() != null && !control.getControlValue().isEmpty()) {
							System.out.println("** Adding to the controlList **");
							controlList.add(control);
						}
					}
				}
			}
		}

		Gson gson = new Gson();
		String json = gson.toJson(domainList);
		
		List<Domain> domains = gson.fromJson(json, new TypeToken<List<Domain>>(){}.getType());
		
		System.out.println(json);
		
//		for (Iterator iterator = domainList.iterator(); iterator.hasNext();) {
//			Domain domain1 = (Domain) iterator.next();
//			System.out.println(domain1.getDomainName());
//			List<Subdomain> subdomains = domain1.getSubdomain();
//			for (Iterator iterator2 = subdomains.iterator(); iterator2.hasNext();) {
//				Subdomain subdomain2 = (Subdomain) iterator2.next();
//				System.out.println("subdomain-code : " + subdomain2.getSubdomainCode());
//				System.out.println("subdomain-value :" + subdomain2.getSubdomainValue());
//				List<Control> cList = subdomain2.getControl();
//				for (Iterator iterator3 = cList.iterator(); iterator3.hasNext();) {
//					Control control2 = (Control) iterator3.next();
//					System.out.println(control2.getControlCode());
//				}
//			}
//		}
	}
}
