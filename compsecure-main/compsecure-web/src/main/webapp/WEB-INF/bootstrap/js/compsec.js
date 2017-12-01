/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	$.ajax({
		url:"/compsecure-web/checkForLoggedInUser"
	}).done(function(data){
		var res = $.parseJSON(data);
		console.log("RESULT : " + res);
		if(res==="unauthenticated"){
			alert("Please Login");
			window.location.href="login.html";
		}
	});
	
	$("#loading").hide();
    var assessmentSelectedObj = "";
    var complianceSelected="";
    var complianceFromDB = "";
    var complianceId = "";   
    var selfAssessmentOption = localStorage.getItem("option");
    var i=1;
   
    if(selfAssessmentOption === "new"){
        $("#assessmentName").html("<input id='assessment_name' type='text' class='form-control'>");
        console.log(selfAssessmentOption);
    	localStorage.setItem("selfAssessmentOption",selfAssessmentOption);
    }else{
    	console.log("in  the else part");
    	$("#button-start").text("Next");
    	console.log(selfAssessmentOption);
    	localStorage.setItem("selfAssessmentOption",selfAssessmentOption);
    }

    var roleId = localStorage.getItem("roleId");
    var userId = localStorage.getItem("userId");
    
    //alert(userId);
    //alert(roleId);
    
    $.ajax({
        url: "/compsecure-web/getOrgDetails/"+userId+"/"+roleId,
        data:{"self_assessment_option":selfAssessmentOption}
    }).then(function (data) {
        console.log("Inside compsec js");
        $("#organization").empty();
        var obj = $.parseJSON(data);
        console.log(obj);
        $.each(obj, function (val, text) {
            $('#organization').append($('<option></option>').val(val).html(text));
        });
    });

//    $("#button-start").click(function () {
//        alert("in the start click event!");
//        window.location.href = "questionnaire.html";
//    });

    $('#questionnaire-list').DataTable();//

    $("#qn-button-next").click(function () {
        window.location.href = "control-effectiveness.html";
    });

    $("#ce-button-next").click(function () {
        window.location.href = "maturity-effectiveness.html";
    });
});

$("#organization").change(function () {
	$("#loading").show();
    var selectedText = $("#organization :selected").text();
    var selectedVal = $("#organization :selected").val();
    var selfAssessmentOption = localStorage.getItem("selfAssessmentOption");
    
    //for display on the page header
    $("#orgNameLabelValue").text(selectedText);
    localStorage.setItem("organizationName",selectedText);
    
    console.log(selectedText);
    console.log(selectedVal);
    console.log(selfAssessmentOption);
    if(selfAssessmentOption!="new"){
    $.ajax({
        url: "/compsecure-web/getAssessmentDetails",
        data: {selected: selectedVal}
    }).then(function (data) {
    	$("#loading").hide();
        console.log("Inside orgchange method");
        console.log(data);
        var ddata = $.parseJSON(data);
        assessmentSelectedObj = ddata;
        $.each(ddata, function (val, text) {
            $('#assessment-selector').append($('<option></option>').val(val).html(text["assessmentName"]))
        });
    });
    }else{
    	$.ajax({
            url: "/compsecure-web/getComplianceDetailsForOrg",
            data: {selected: selectedVal}
        }).then(function (data) {
        	$("#loading").hide();
            console.log("Inside get Compliance Details for an organization");
            console.log(data);
            var ddata = $.parseJSON(data);
            console.log(ddata);
           	$.each(ddata, function (val, text) {
            		$('#compliance-selector').append($('<option></option>').val(text).html(val));
            	});
        });
    }
});

$("#assessment-selector").change(function () {
	$("#loading").show();
    console.log("inside assessment selector");
    var selectedText = $("#assessment-selector :selected").text();
    var selectedVal = $("#assessment-selector :selected").val();
    console.log(" Selected Text :" + selectedText);
    console.log(" Selected Value :" + selectedVal);
    
    //for display on the page header
    $("#assessmentNameLabelValue").text(selectedText);
    localStorage.setItem("assessmentName",selectedText);
    
    // to be used for future computations
    localStorage.setItem("assessmentId",selectedVal);

    $.each(assessmentSelectedObj, function (index, value) {
        console.log(index);
        console.log(value["assessmentId"]);
        if (selectedVal === value["assessmentId"]) {
            console.log("true");
            $("#assessment-description").val(value["assessmentDesc"]);
            $("#assessment-from").val(value["assessmentStartDate"]);
            $("#assessment-to").val(value["assessmentToDate"]);
        }
    });
    
    console.log("Selected Assessment :" + selectedVal);

    $.ajax({
        url: "/compsecure-web/getComplianceDetails",
        data: {selected: selectedVal}
    }).then(function (data) {
    	$("#loading").hide();
        console.log("Inside assessment-change method");
        console.log(data);
        var ddata = $.parseJSON(data);
        console.log(ddata);
        var optionSelected = localStorage.getItem("option");
//        alert("optionSelected " + optionSelected);
        console.log("CompSec-optionSelected " + optionSelected);
        if(optionSelected==="new"){
        	$.each(ddata, function (val, text) {
        		$('#compliance-selector').append($('<option></option>').val(text).html(val))
        	});
        }else{
        	var key = "";
        	$.each(ddata, function (val, text) {
        		$('#compliance-selector').append($('<option></option>').val(text).html(val));
        		key = text;
        	});
        	$('#compliance-selector').val(key);
        	updateComplianceDesc();
        }
        
    });
});

$("#compliance-selector").change(function () {
	updateComplianceDesc();
});

function updateComplianceDesc(){
	 var selectedText = $("#compliance-selector :selected").text();
	    var selectedVal = $("#compliance-selector :selected").val();
	    console.log(selectedText);
	    console.log(selectedVal);
	    $("#comp-description").val(selectedVal);
	    complianceSelected  = selectedVal;
	    complianceId = selectedText;
	    localStorage.setItem("complianceId",selectedVal);
	    localStorage.setItem("complianceName",selectedText);
	    
	  //for display on the page header
	    $("#complianceNameLabelValue").text(selectedText);
	    localStorage.setItem("complianceName",selectedText);
//	    alert(selectedText);
}

$("#button-start").click(function(){
//   alert("Start Clicked");
   var optionSelected = localStorage.getItem("option");
//   alert(optionSelected);
   if(optionSelected==="new"){
	   doSave();
   }else{
	   window.location.href="questionnaire.html";
   }
});


$("#button-cancel").click(function(){
	window.location="home";
})

function doSave(){
   
//	alert("in dosave");
	$("#loading").show();
	
	var organizationId = $("#organization :selected").val();
	var assessmentName = $("#assessment_name").val();
	var assessmentDesc = $("#assessment-description").val();
	var assessmentFromDate = $("#assessment-from").val();
	var assessmentToDate = $("#assessment-to").val();
	var complianceId = $("#compliance-selector :selected").text();
	var compDesc = $("#comp-description").val();
	
	var assessmentObj = new AssessmentObject(organizationId,assessmentName,assessmentDesc,assessmentFromDate,assessmentToDate,complianceId,compDesc);
	console.log(assessmentObj);
	localStorage.setItem("complianceId",complianceId);
	
   $.ajax({
	    url: "/compsecure-web/saveAssessmentDetails",
	    type:"POST",
        contentType:"application/json",
        data: JSON.stringify(assessmentObj)
    }).then(function(data){
    	$("#loading").hide();
    	window.location="questionnaire.html";
    });
}

function AssessmentObject(organizationId,assessmentName,assessmentDesc,assessmentFromDate,assessmentToDate,complianceId,compDesc){
	this.organizationId = organizationId;
	this.assesssmentId = "5";
	this.assessmentName = assessmentName;
	this.assessmentDesc = assessmentDesc;
	this.assessmentStartDate = assessmentFromDate;
	this.assessmentToDate = assessmentToDate;
	this.complianceId = complianceId;
	this.complianceDesc = compDesc;
	this.assessmentStatus="Started";
}


