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
	

     $(document).on('click', '#button-add-control', function () {
        //alert("Add Control "+ controlCount+" clicked!!");
        
        var clicked = parseInt($(this).attr('name'));
        //alert(clicked);
        
//         $("#cntrl"+clicked).after("<tr><td style='width: 5%'><input id='control_chkBox_id' type='checkbox' class='checkbox' name='control_no'></td>\n\
//                            <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
//        					<td><input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td></tr>");
         $("tr:last").after("<tr><td style='width: 5%'><input id='control_chkBox_id' type='checkbox' class='checkbox' name='control_no'></td>\n\
                            <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
        					<td><input id='control_value' type='text' class='form-control gap' name='control_value' placeholder='Control Details'></td></tr>");

         //         clicked = clicked+1;
         alert(clicked);  
       //controlCount++;
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
        doSave();
    });
    
    
    $(document).on('click','#button-add-subdomain',function(){
    
       //alert("#button-add-subdomain-add subdomain clicked");
       
       //alert("current subdomain button clicked --  " + $(this).attr('name'));
       var subdomainButtonClicked = $(this).attr('name');
       
       //alert("in add subdomain. subdomainButtonClicked - " + subdomainButtonClicked);
       
        var strSubdomain = getSubdomainTableHTML(subdomainButtonClicked);
       
       //alert("inside #button-add-subdomain"+ controlCount);
       
       $(this).parent().after(strSubdomain + "<br>"+ getControlTableHTML(controlCount));
       controlCount++;
    });
    
    function getSubdomainTableHTML(subdomainButtonClicked){
        //alert("in subdomain "  + subdomainButtonClicked);
        
        //alert("controlDomainId " + $("#controlDomainId").attr("name"));
        
        var sudomainCountNo = subdomainButtonClicked+1;
        return "<div id='subdomain-group-"+sudomainCountNo+"' class='col-sm-12' style='padding: 0'>\n\
                                <br><div class='input-group'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                                    <input id='sub_code' type='text' style='width:100%;' class='form-control' name='sub_code' placeholder='Subdomain Code'>\n\
                                    <input id='subdomain_value' type='text' style='width:100%;' class='form-control gap' name='subdomain_value' placeholder='Subdomain Value'>\n\
                                    <textarea id='principle' style='width:100%;' class='form-control gap' name='principle' placeholder='Principle' rows='2'></textarea>\n\
                                    <textarea id='objective' style='width:100%;' class='form-control gap' name='objective' placeholder='Objective' rows='2'></textarea></div><br>";
       
    }

    $(document).on('click', '#button-add-control-domain', function () {
        addDomainClicked = true;
        //alert("add domain clicked");
        i++;
        var strCompliance = "<div class='panel panel-default'> <div class='panel-heading'> <h4 class='panel-title'> \n\
                    <a data-toggle='collapse' class='input-group' href='#collapse1"+i+"' name="+i+" id='controlDomainId'>Control Domain -"+i+"</a></h4></div> \n\
                    <div id='collapse1"+i+"' class='panel-collapse collapse'> <div class='panel-body'> \n\
                    <div class='input-group'> <span class='input-group-addon'>Domain Name</span> \n\
                    <input id='domain_code' type='text' class='form-control ' name='domain_code' placeholder='Control Domain Code'> \n\
                    <input id='domain_value' type='text' class='form-control gap' name='domain_value' placeholder='Control Domain Value'> </div> <br>"
                    + getSubdomainTableHTML(i)+ "<br>"+ getControlTableHTML(i)+
                    "<div class='row'><div class='col-sm-3'>\n\
                     <button type='button' id='button-add-subdomain' class='btn btn-success gap' name='"+i+"'>Add Subdomain</button>\n\
                    </div>\n\
                    </div>\n\
                    \n\
                    </tr> </tbody> </table> </div> </div> </div> </div>";
        console.log(strCompliance);
        $(this).before(strCompliance);
    });
    
    function doSave(){
    	alert("in save");
    	console.log("In the doSave method");
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
    		   console.log(JSON.stringify($("#complianceDefForm").serialize()));
    	   });
    	   
    	   $.ajax({
    		   url	:"/compsecure-web/saveComplianceDefData",
    		   type :"POST",
    		   data : {
    			   complianceName: complianceName,
    			   formData:JSON.stringify($("#complianceDefForm").serialize())
    		   }
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


function ComplianceObject(compliance_text,domain_code ,domain_value ,subdomainList){
	this.complianceDescription = compliance_text,
	this.domainCode  = domain_code;
	this.domainValue = domain_value;
	this.subdomain = subdomainList;
}

function getControlTableHTML(controlCount){
    var controlNo = controlCount +1;
	return "<div> <label class='input-group'>Controls</label></div><br>" +
			   "<div class='row'> <div class='col-lg-12'><table class='table' id='subdomainTable'> <thead> <tr> <th>Select</th> <th>Control Code</th> <th>Control Detail</th> </tr> </thead>" +
			   "<tbody> <tr id='cntrl"+controlNo+"'> <td style='width:3%;align-content: center'><input type='checkbox' name='control_no' id='control_"+controlNo+"'></td> <td style='width: 20%'>" +
			   "<input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td> <td style='width: 77%'>" +
			   "<input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td>" +
			   "<!--<td style='width: 77%'><button id='button-save-control' type='button' class='btn btn-success gap'>Add </button></td>--> </tr> </tbody> </table></div> </div>" +
			   "<div class='col-sm-6' align='right'> <button type='button' id='button-add-control' class='btn btn-success gap' name='"+controlNo+"'>Add Control</button> " +
			   "<button type='button' id='button-delete-control' class='btn btn-success gap'>Delete Control</button> <br> </div>";
}

function doNext(){
	console.log("In the doNext method");
	doSave();
}

function test(compDefFormData){
    console.log(compDefFormData);
    var data = JSON.stringify(compDefFormData);
    console.log(data);
}