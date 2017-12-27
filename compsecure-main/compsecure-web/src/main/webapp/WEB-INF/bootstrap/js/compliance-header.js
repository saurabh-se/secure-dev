/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	
//	$.ajax({
//		url:"/compsecure-web/authenticateRole"		
//	}).done(function(data){
//		$("#loading").hide();
//	});
   
	var selectedOption = localStorage.getItem("selectedOption");
	console.log("selectedOption",selectedOption);
	var saved = "false";
	var ddata="";

	if(selectedOption==="existing"){
		$.ajax({
			url : "/compsecure-web/getCompliances"
		}).done(function(data){
			$("#loading").hide();
			console.log(data);
			ddata = $.parseJSON(data);
			$("#compliance-name-div").html("<select class='form-control' id='compliance_name'><option>Please Select</option></select>");
			$.each(ddata, function (val, text) {
        		$('#compliance_name').append($('<option></option>').val(text).html(val))
        	});
		});
	}else{
		$("#loading").hide();
	}
});

$(document).on("change", "#compliance_name", function (event) {
//	$("#loading").show();
	var selectedOption = localStorage.getItem("selectedOption");
	console.log("In the compliance header page - value changed!!");
	if(selectedOption === "existing"){
		var selectedComplianceText = $("#compliance_name :selected").text();
		console.log("in the compliance header page : " + selectedComplianceText);
		$("#compliance_description").val($("#compliance_name :selected").val());
		localStorage.setItem("complianceName",$("#compliance_name :selected").text());
		//alert($("#compliance_name :selected").text());
		$.ajax({
			url		:"/compsecure-web/getComplianceDetails",
			data 	: {"selected" : selectedComplianceText}
		}).done(function(data){
			console.log(data);
			$("#loading").hide();
//			alert(" data :" + data);
			localStorage.setItem("assessmentId",data);
		});
	}else{
		
	}
});

$("#button-cancel").click(function(){
	window.location="home";
});

$("#button-save").click(function(){
	$("#loading").show();
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
	    }).done(function(data){
	    	$("#loading").hide();
	    	var selectedOption = localStorage.getItem("selectedOption");
	    	if(selectedOption==="new"){
	    	$("#dialog").dialog({
				 modal: true,
	             title :"Compliance-Header",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                    window.location="compliance_definition_add";
	                }
	            },
			});
	    	}else{
	    	$("#dialog-existing").dialog({
				 modal: true,
	            title :"Compliance-Header",
	            dialogClass :"dialogStyle",
	            width: 400,
	           buttons : {
	               Ok: function() {
	                   $(this).dialog("close"); //closing on Ok click
	               }
	           },
			});
	    	}
	    	saved = "true";
	    	localStorage.setItem("saved",saved);
	    });
}

function ComplianceObj(complianceName,complianceDescription,regulatorId){
	this.complianceName = complianceName;
	this.complianceDescription = complianceDescription;
	this.regulatorId		= regulatorId;
}

function doNext(){
	console.log("In the doNext method");
	var saved = localStorage.getItem("saved");
	var selectedOption = localStorage.getItem("selectedOption");
	if(selectedOption==="existing"){
		var assessmentId = localStorage.getItem("assessmentId");
		localStorage.setItem("assessmentId",assessmentId);
		console.log("moving to the next page without saving");
		window.location="compliance_definition_add";
	}else if(saved==="true"){
		window.location="compliance_definition_add";
	}
	else{
		doSave().done(function(){
			console.log("moving to the next page after saving");
		});
	}
}
