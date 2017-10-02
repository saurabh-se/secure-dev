/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	
	var selectedOption 	= localStorage.getItem("selectedOption");
	var assessmentId 	= localStorage.getItem("assessmentId");
	var complianceName	= localStorage.getItem("complianceName");
	
	console.log("assessmentId in compliance-def page" + assessmentId);
	console.log("complianceId in compliance-def page" + complianceName);
	$("label[for='comp_name']").html(complianceName);
	
	$.ajax({
		url 	: "/compsecure-web/getComplianceDefinitionDetails",
		data 	: {
					"complianceName": complianceName
    			}
	}).then(function (data) {
        console.log("Inside get Compliance Details");
        console.log(data);
        var ddata = $.parseJSON(data);
        console.log(ddata);
    });
	
    var i = 1;
    var controlCount = 1;
    
    $("#button-add-control").click(function () {
        alert("Add Control");
        $("#cntrl").after("<tr><td style='width: 5%'><input id='control_chkBox_id' type='checkbox' class='checkbox' name='control_no'></td>\n\
                            <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
        					<td><input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td></tr>");
    });

    $("#button-delete-control").click(function () {
        $("table tbody").find('input[name="control_no"]').each(function () {
            if ($(this).is(":checked")) {
                $(this).parents("tr").remove();
            }
        });
    });

    $("#button-save").click(function () {
        alert("Inside the buttonsave");
//        doSave().done(function(){
//        	window.location =  "toAddQuestions";
//        });
        doSave();
    });
    
    
    $(document).on('click','#button-add-subdomain',function(){
    	alert("add subdomain clicked");
        var strSubdomain ="<br><br><div class='input-group col-sm-12'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                                    <input id='subdomain_code' type='text' style='width:100%;' class='form-control' name='subdomain_code' placeholder='Subdomain Code'>\n\
                                    <input id='subdomain' type='text' style='width:100%;' class='form-control gap' name='subdomain' placeholder='Subdomain Value'>\n\
                                    <textarea id='principle' style='width:100%;' class='form-control gap' name='principle' placeholder='Principle' rows='2'></textarea>\n\
                                    <textarea id='objective' style='width:100%;' class='form-control gap' name='objective' placeholder='Objective' rows='2'></textarea></div><br>";
        
        
       $("#subdomain-group").after(strSubdomain + "<br>"+ getControlTableHTML(controlCount) );
       controlCount++;
    });
    
    

    $(document).on('click', '#button-add-control-domain', function () {
        alert("clicked");
        i++;
        var strCompliance = "<div class='panel panel-default'><div class='panel-heading'><h4 class='panel-title'>\n\
                     <a data-toggle='collapse' class='input-group' href='#collapse1"+i+"'>Control Domain</a></h4></div>\n\
                     <div id='collapse1"+i+"' class='panel-collapse collapse'><div class='panel-body'><div class='input-group'>\n\
                     <span class='input-group-addon'>Domain Name</span><input id='domain_code' type='text' class='form-control ' name='domain_code' placeholder='Control Domain Code'>\n\
                     <input id='domain' type='text' class='form-control gap' name='domain' placeholder='Control Domain Value'></div>\n\
                     <br>\n\
                     <div class='input-group'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                     <input id='subdomain_code' type='text' class='form-control ' name='subdomain_code' placeholder='Subdomain Code'><input id='subdomain' type='text' class='form-control gap' name='subdomain' placeholder='Subdomain Value'>                                     \n\
                     <textarea id='principle' class='form-control gap' name='principle' placeholder='Principle' rows='2'></textarea>                                     \n\
                     <textarea id='objective' class='form-control gap' name='objective' placeholder='Objective' rows='2'></textarea></div>\n\
                     <br>\n\
                     <button type='button' id='button-add-subdomain' class='btn btn-success gap'>Add Subdomain</button><br><div class='row'>\n\
                     <div class='col-sm-6'><button type='button' id='button-add-control' class='btn btn-success gap'>Add Control</button><button type='button' id='button-delete-control' class='btn btn-success gap'>Delete Control</button></div></div>                                 \n\
                     <br><div><label class='input-group'>Controls</label></div><br>\n\
                     <div class='row'><table class='table' id='subdomainTable'>\n\
                        <thead><tr><th>Select</th><th>Control Code</th><th>Control Detail</th><th></th>\n\
                        </tr></thead>\n\
                     <tbody><tr id='cntrl'><td style='align-content: center'><input type='checkbox' name='control_no'></td>\n\
                     <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
                     <td style='width: 35%'><input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td><div class='col-sm-6' align='right'>\n\
                     <button type='button' id='button-add-control' class='btn btn-success gap'>Add Control</button>\n\
                     <button type='button' id='button-delete-control' class='btn btn-success gap'>Delete Control</button>\n\
                     <br>\n\
                     </div></div>\n\
                     <div class='row'><div class='col-sm-3'><button type='button' id='button-add-subdomain' class='btn btn-success gap'>Add Subdomain</button></div></div>\n\
                     </div></div>";
        $(this).before(strCompliance);
    });
    
    function doSave(){
    	console.log("In the doSave method");
    	console.log(JSON.stringify($("#complianceDefForm").serialize()));
    	console.log(JSON.stringify($("#complianceDefForm").serialize()));
    	var complianceName 	= $("#comp_name").text();
//    	var compliance_text 	= $("#comp_name :selected").text();
    	
    	var complianceObject = "";
    	
    	console.log(complianceName);
    	
    	   $("#complianceDefForm").each(function() {
    		   var domainList		= [];
    		   var subdomainList 	= [];
    		   
    		   var domain_code 		= $("#domain_code").val();
    		   var domain_value 	= $("#domain_value").val();
    		   
    		  
//    		   $("#subdomain-group").each(function(){
    			   var subdomain_code 	= $(this).find("input[name='sub_code']").val();
    			   var subdomain_value 	= $(this).find("input[name='subdomain_value']").val();
    			   var principle 		= $(this).find("textarea[name='principle']").val();
        		   var objective 		= $(this).find("textarea[name='objective']").val();
        		   var control_code 	= $(this).find("input[name='control_code']").val();
        		   var control_value	= $(this).find("input[name='control_value']").val();
        		   var subdomainObject 	= new SubdomainObj(subdomain_code ,subdomain_value ,principle ,objective ,control_code ,control_value);
        		   subdomainList.push(subdomainObject);
        		   
        		   var domainObj 		= new DomainObject(domain_code,domain_value,subdomainList);
        		   domainList.push(domainObj);
//    		   });
    		   
    		   complianceObject = new ComplianceObject(complianceName,domainList);
    		   console.log(JSON.stringify(complianceObject));
    	   });
    	   
    	   $.ajax({
    		   url	:"/compsecure-web/saveComplianceDefData",
    		   type :"POST",
    		   contentType:"application/json",
               dataType: "JSON",
    		   data : JSON.stringify(complianceObject)
    	   }).then(function(data){
    		   window.location="questions_add";
    	   });
    }
    
    function SubdomainObj(subdomain_code ,subdomain_value ,principle ,objective ,control_code ,control_value){
    	var controlList = [];
    	this.subdomainCode = subdomain_code;
    	this.subdomainValue = subdomain_value;
    	this.principle = principle;
    	this.objective = objective;
    	
    	var controlObj = new ControlObj(control_code,control_value);
    	controlList.push(controlObj);
    	this.control = controlList;
    }
    
    function DomainObject(domain_code,domain_value,subdomainList){
    	this.domainCode  = domain_code;
    	this.domainName = domain_value;
    	this.subdomain = subdomainList;
    }
});

function ControlObj(control_code,control_value){
	this.controlCode 	= control_code;
	this.controlValue 	= control_value;
}

function ComplianceObject(complianceName,domainList){
	this.complianceName = complianceName,
	this.domains = domainList;
}

function getControlTableHTML(controlCount){
	return "<div> <label class='input-group'>Controls</label></div><br>" +
			   "<div class='row'> <table class='table' id='subdomainTable'> <thead> <tr> <th>Select</th> <th>Control Code</th> <th>Control Detail</th> </tr> </thead>" +
			   "<tbody> <tr id='cntrl'> <td style='width:5%;align-content: center'><input type='checkbox' name='control_no' id='control_"+controlCount+"'></td> <td style='width: 25%'>" +
			   "<input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td> <td style='width: 35%'>" +
			   "<input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td>" +
			   "<!--<td style='width: 35%'><button id='button-save-control' type='button' class='btn btn-success gap'>Add </button></td>--> </tr> </tbody> </table> </div>" +
			   "<div class='col-sm-6' align='right'> <button type='button' id='button-add-control-'"+controlCount+"' class='btn btn-success gap'>Add Control</button> " +
			   "<button type='button' id='button-delete-control'"+controlCount+"' class='btn btn-success gap'>Delete Control</button> <br> </div>";
	
}

$("#button-next").click(function(){
	doNext();
});

function doNext(){
	alert("In the doNext method");
		window.location ="questions_add.html";
}