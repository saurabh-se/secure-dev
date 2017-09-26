/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
   
});

$("#button-cancel").click(function(){
	window.location.href="welcome";
});

$("#button-save").click(function(){
	doSave();
});

$("#button-next").click(function(){
	doNext();
});

function doSave(){
	console.log("In the doSave method");
	var complianceName 	= $("#compliance_name").val();
	var complianceDescription 	= $("#compliance_description").val();
	var regulatorId		= $("#regulator_name :selected").val();
	
	console.log(complianceName,complianceDescription,regulatorId);
	var compliance = new ComplianceObj(complianceName,complianceDescription,regulatorId);
	
	console.log(JSON.stringify(compliance));
	
	 $.ajax({
	        type: "POST",	
		 	url	: "/compsecure-web/enterComplianceDetails",
		 	contentType:"application/json",
	        data: JSON.stringify(compliance),
	    }).then(function (data) {
	        console.log("Inside the doSave method");
	        console.log(data);
	        var ddata = $.parseJSON(data);
	    });
}

function ComplianceObj(complianceName,complianceDescription,regulatorId){
	this.complianceName = complianceName;
	this.complianceDescription = complianceDescription;
	this.regulatorId		= regulatorId;
}

function doNext(){
	console.log("In the doNext method");
	doSave();
}
