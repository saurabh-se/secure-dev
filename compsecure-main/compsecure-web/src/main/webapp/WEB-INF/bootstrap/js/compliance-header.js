/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
   
	var selectedOption = localStorage.getItem("selectedOption");
	console.log("selectedOption",selectedOption);
	var ddata="";
	
	if(selectedOption==="existing"){
		$.ajax({
			url : "/compsecure-web/getCompliances"
		}).then(function(data){
			console.log(data);
			ddata = $.parseJSON(data);
			$("#compliance-name-div").html("<select class='form-control' id='compliance_name'><option>Please Select</option></select>");
			$.each(ddata, function (val, text) {
        		$('#compliance_name').append($('<option></option>').val(text).html(val))
        	});
		});
	}
});

$(document).on("change", "#compliance_name", function (event) {
	var selectedOption = localStorage.getItem("selectedOption");
	console.log("In the compliance header page - value changed!!");
	if(selectedOption === "existing"){
		var selectedComplianceText = $("#compliance_name :selected").text();
		console.log("in the compliance header page : " + selectedComplianceText);
		$("#compliance_description").val($("#compliance_name :selected").val());
		localStorage.setItem("complianceName",$("#compliance_name :selected").text());
		alert($("#compliance_name :selected").text());
		$.ajax({
			url		:"/compsecure-web/getComplianceDetails",
			data 	: {"selectedCompliance" : selectedComplianceText}
		}).then(function(data){
			localStorage.setItem("assessmentId",data);
		});
	}else{
		
	}
});

$("#button-cancel").click(function(){
	window.location.href="welcome";
});

$("#button-save").click(function(){
	doSave().done(function(){
		console.log("The Data has been saved");
	});
});

$("#button-next").click(function(){
	doNext();
});

function doSave(){
	console.log("In the doSave method");
	var complianceName 	= $("#compliance_name").val();
	console.log("in the doSave method .. complianceName " + complianceName);
	var complianceDescription 	= $("#compliance_description").val();
	var regulatorId		= $("#regulator_name :selected").val();
	
	console.log(complianceName,complianceDescription,regulatorId);
	var compliance = new ComplianceObj(complianceName,complianceDescription,regulatorId);
	
	console.log(JSON.stringify(compliance));
	
	localStorage.setItem("complianceName",complianceName);
	
	 return $.ajax({
	        type: "POST",	
		 	url	: "/compsecure-web/enterComplianceDetails",
		 	contentType:"application/json",
	        data: JSON.stringify(compliance),
	    });
}

function ComplianceObj(complianceName,complianceDescription,regulatorId){
	this.complianceName = complianceName;
	this.complianceDescription = complianceDescription;
	this.regulatorId		= regulatorId;
}

function doNext(){
	console.log("In the doNext method");
	var selectedOption = localStorage.getItem("selectedOption");
	if(selectedOption==="existing"){
		console.log("moving to the next page without saving");
		window.location="compliance_definition_add";
	}else{
		doSave().done(function(){
			console.log("moving to the next page after saving");
			window.location="compliance_definition_add";
		});
	}
}
