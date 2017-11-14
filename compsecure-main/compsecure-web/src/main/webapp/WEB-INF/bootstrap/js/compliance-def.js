/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	
	var selectedOption 	= localStorage.getItem("selectedOption");
	var assessmentId 	= localStorage.getItem("assessmentId");
	var complianceName	= localStorage.getItem("complianceName");
	var saved = "false";
	
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
        
       
//        $.each(ddata, function (key, value) {
//            var count = 0;
//            var domainName = value["domainName"];
//            var domainCode = value["domainCode"];
//            var subdomData = value["subdomain"];
//            
//            var domainDetTable = "<table id='cd-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Domain Name</th> <th>Domain Code</th></tr> </thead> <tbody> \n\
//                        <tr><td>" + domainName + "</td><td>" + domainCode + "</td></tbody> </table>";
//
//            $.each(subdomData, function (key, value) {
//                //alert(value["subdomainValue"]);
//                var subdomainValue = value["subdomainValue"];
//                var subdomainCode = value["subdomainCode"];
//                var controlData = value["control"];
//                var principle = value["principle"];
//                var objective = value["objective"];
//                var cntrlHtml = "";
//                var subdomainDetTable = "<table id='cd-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Subdomain Code</th> <th>Principle</th> <th>Objective</th>  </tr> </thead> <tbody> \n\
//                        <tr><td>" + subdomainCode + "</td><td>" + principle + "</td><td>" + objective + "</td></tbody> </table>";
//
//                $("#collapse1")
//                        .append("<div class='panel-heading' style='padding: 1px;border:0'>\n\
//                                      <a data-toggle='collapse' href='#collapse" + i + "' id='ce-panel-heading' id='ce-domainDataToggleId'>\n\
//                                      <label class='label label-default' style='background: none;color: #d9534f' id='ce-domainId'>" + domainName + "</label></a></div><br>")
//                        .append("<div id='collapse" + i + "' class='panel-collapse collapse'><div class='panel-body' id='ce-domain-panel'>"+domainDetTable+"\n\
//                                      <div class='panel-heading' style='padding: 1px;'>\n\
//                                      <a data-toggle='collapse'  href='#subdomaincollapse" + i + "'>\n\
//                                      <label class='label label-default' style='background: none;color: #1b6d85' id='ce-domainId'>" + subdomainValue + "</label></a></div><br>\n\
//                                     \n\
//                                      \n\
//                                       <script>" +
//                                $.each(controlData, function (controlKey, cntrlValue) {
//                                    var controlValue = cntrlValue["controlValue"];
//                                    var controlCode = cntrlValue["controlCode"];
//                                  
//                                    count++;
//                                    //alert(controlValue);
//
//                                    cntrlHtml = cntrlHtml + "\n\
//                                                                <tr><td>" + count + "</td><td><div style='display:inline-table;width:150px;'><input type='text' readonly='readonly' style='width:50px;border:none' id='controlId' name='controlCode' value='" + controlCode + "'></input>\n\
//                                                                    <button type='button' title='Click for Questions' class='btn btn-info ce-qBtn' name='" + count + "' id='qBtn" + count + "' value='" + controlCode + "'>Display Questions</button></div></td> \n\
//                                                                <td><div id='controlValue' class='text-left' style='width:300px;'>" + controlValue + "</div></td>\n\
//                                                                <td><select name='ceSelectDocEffectiveness' id='ceSelectDocEffectiveness'><options><option>Select</option><option>Compliant</option><option>Non Compliant</option><option>Partially Compliant</option></options></select></td> \n\
//                                                                <td ><input type='file' id='upload-file'>" +
//                                                                	"<button class='btn btn-info btnUpload' id='docEff' name="+controlCode+">Upload</button></td> \n\
//                                                                <td><textarea name='ce-remarksTA' id='ce-remarksTA' rows='2'></textarea></td>\n\
//                                                                <td><select name='ceSelectImplEffectiveness' id='ceSelectImplEffectiveness'><options><option>Select</option><option>Compliant</option><option>Non Compliant</option><option>Partially Compliant</option></options></select></td>\n\
//                                                                <td><input name='upload-implEffectiveness' type='file' id='upload-implEffectiveness'>" +
//                                                                	"<button class='btn btn-info btnUpload' id='implEff'>Upload</button></td>\n\
//                                                                <td><textarea name='ce-remarksImplEff' id='ce-remarksImplEff' rows='2'></textarea></td>\n\
//                                                                <td><select name='ceSelectRecEffectiveness' id='ceSelectRecEffectiveness'><options><option>Select</option><option>Compliant</option><option>Non Compliant</option><option>Partially Compliant</option></options></select></td>\n\
//                                                                <td><input name='upload-recEffectiveness' type='file' id='upload-recEffectiveness'>" +
//                                                                "<button class='btn btn-info btnUpload' id='recEff'>Upload</button></td>\n\
//                                                                <td><textarea name='ce-remarksRE' id='ce-remarksRE' rows='2'></textarea></td> " +
//                                                                "<td><select class='form-control' id='maturityEffSelector'><option>1</option><option>2</option><option>3</option></select></td></tr>\n\
//\n\<tr id='qDisplay'><td colspan='12' style='display:none;' id='tdQuestions"+count+"'><div id='demo" + count + "' class='collapse'>Questions</div></td></tr>\
//                                                              "
//                                }) + "</script>"
//                                ).append("<div id='subdomaincollapse" + i + "' class='panel-collapse collapse'><div class='panel-body'><div>" + subdomainDetTable + "<br>\n\
//                                        <table id='control_effectiveness-list3' class='table table-sm table-bordered'> <thead> <tr> <th>#</th><th style='width:35%;'>Control Code</th><th>Control</th><th>Doc Effectiveness(C/NC/PC)</th> <th>Doc Effectiveness Evidence</th> <th>Remarks</th> <th>Implementation Effectiveness (C/NC/PC)</th> <th>Implementation Effectiveness Evidence </th><th>Implementation Effectiveness Remarks </th> <th>Record Effectiveness (C/NC/PC)</th> <th>Record Effectiveness Evidence</th> <th>Record Effectiveness Remarks</th><th>Maturity Level</th></tr> </thead> <tbody> \n\
//                                        <tr id='ce-controlTableTR'>" + cntrlHtml + "</tbody> </table>" +
//                        "</div></div></div>");
//                //                              alert(cntrlHtml);
//
//                i++;
//
//            });
//        });
    });
	
    var i = 1;
    var controlCount = 1;
    
    $("#button-add-control").click(function () {
        //alert("Add Control");
        $("#cntrl").after("<tr><td style='width: 5%'><input id='control_chkBox_id' type='checkbox' class='checkbox' name='control_no'></td>\n\
                            <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
        					<td><input id='control_value' type='text' class='form-control gap' name='control_value' placeholder='Control Details'></td></tr>");
    });

    $("#button-delete-control").click(function () {
        $("table tbody").find('input[name="control_no"]').each(function () {
            if ($(this).is(":checked")) {
                $(this).parents("tr").remove();
            }
        });
    });

    $("#button-save").click(function () {
        //alert("Inside the buttonsave");
//        doSave().done(function(){
//        	window.location =  "toAddQuestions";
//        });
        doSave();
    });
    
    
    $(document).on('click','#button-add-subdomain',function(){
    	//alert("add subdomain clicked");
        var strSubdomain ="<br><br><div class='input-group col-sm-12'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                                    <input id='sub_code' type='text' style='width:100%;' class='form-control' name='sub_code' placeholder='Subdomain Code'>\n\
                                    <input id='subdomain_value' type='text' style='width:100%;' class='form-control gap' name='subdomain_value' placeholder='Subdomain Value'>\n\
                                    <textarea id='principle' style='width:100%;' class='form-control gap' name='principle' placeholder='Principle' rows='2'></textarea>\n\
                                    <textarea id='objective' style='width:100%;' class='form-control gap' name='objective' placeholder='Objective' rows='2'></textarea></div><br>";
        
        
       $("#subdomain-group").after(strSubdomain + "<br>"+ getControlTableHTML(controlCount) );
       controlCount++;
    });
    
    
    $(document).on('click', '#button-add-control-domain', function () {
        //alert("clicked");
        i++;
        var strCompliance = "<div class='panel panel-default'><div class='panel-heading'><h4 class='panel-title'>\n\
                     <a data-toggle='collapse' class='input-group' href='#collapse1"+i+"'>Control Domain</a></h4></div>\n\
                     <div id='collapse1"+i+"' class='panel-collapse collapse'><div class='panel-body'><div class='input-group'>\n\
                     <span class='input-group-addon'>Domain Name</span><input id='domain_code' type='text' class='form-control ' name='domain_code' placeholder='Control Domain Code'>\n\
                     <input id='domain_value' type='text' class='form-control gap' name='domain_value' placeholder='Control Domain Value'></div>\n\
                     <br>\n\
                     <div class='input-group'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                     <input id='sub_code' type='text' class='form-control ' name='sub_code' placeholder='Subdomain Code'>\n\
                     <input id='subdomain_value' type='text' class='form-control gap' name='subdomain_value' placeholder='Subdomain Value'>                                     \n\
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
                     <td style='width: 35%'><input id='control_value' type='text' class='form-control gap' name='control_value' placeholder='Control Details'></td><div class='col-sm-6' align='right'>\n\
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
    	alert(saved);
    	console.log(JSON.stringify($("#complianceDefForm").serialize()));
    	var complianceDefFormData = JSON.stringify($("#complianceDefForm").serialize());
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
    		   //data : JSON.stringify(complianceObject)
               data : {
            	   		"complianceName" : complianceName,
            	   		"formData" : complianceDefFormData
            	   }
    	   }).done(function(data){
    		   saved = "true";
    		   localStorage.setItem("saved",saved);
    		   //window.location="questions_add";
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
			   "<input id='control_value' type='text' class='form-control gap' name='control_value' placeholder='Control Details'></td>" +
			   "<!--<td style='width: 35%'><button id='button-save-control' type='button' class='btn btn-success gap'>Add </button></td>--> </tr> </tbody> </table> </div>" +
			   "<div class='col-sm-6' align='right'> <button type='button' id='button-add-control-'"+controlCount+"' class='btn btn-success gap'>Add Control</button> " +
			   "<button type='button' id='button-delete-control'"+controlCount+"' class='btn btn-success gap'>Delete Control</button> <br> </div>";
	
}

$("#button-next").click(function(){
	doNext();
});

function doNext(){
	var saved = localStorage.getItem("saved");
	alert(saved);
	if(saved==='true'){
	window.location ="questions_add.html";
	}else{
		alert("Please save the data before proceeding");
		return false;
	}
}

$("#button-cancel").click(function(){
	window.location="welcome";
});