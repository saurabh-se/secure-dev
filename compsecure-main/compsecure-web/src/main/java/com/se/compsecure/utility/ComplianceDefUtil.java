package com.se.compsecure.utility;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.se.compsecure.model.Control;
import com.se.compsecure.model.Domain;
import com.se.compsecure.model.Subdomain;

public class ComplianceDefUtil {

	public static String generateKeyValPair(String serArrayStr) {

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

		String urlDecoded = URLDecoder.decode(serArrayStr.replace("\"", ""));
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
		
		System.out.println(json);
		return json;
		
	}
}
