/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var assessmentSelectedObj = "";
    var complianceSelected="";
    var complianceId = "";   
    var selfAssessmentOption = localStorage.getItem("option");
    var i=1;
   
    if(selfAssessmentOption === "new"){
        $("#assessmentName").html("<input id='assessmentName' type='text' class='form-control'>");
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
        url: "http://localhost:8080/compsecure-web/getOrgDetails/"+userId+"/"+roleId
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
    var selectedText = $("#organization :selected").text();
    var selectedVal = $("#organization :selected").val();
    var selfAssessmentOption = localStorage.getItem("selfAssessmentOption");
    
    console.log(selectedText);
    console.log(selectedVal);
    console.log(selfAssessmentOption);
    if(selfAssessmentOption!="new"){
    $.ajax({
        url: "http://localhost:8080/compsecure-web/getAssessmentDetails",
        data: {selected: selectedVal}
    }).then(function (data) {
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
            url: "http://localhost:8080/compsecure-web/getComplianceDetailsForOrg",
            data: {selected: selectedVal}
        }).then(function (data) {
            console.log("Inside get Compliance Details for an organization");
            console.log(data);
            var ddata = $.parseJSON(data);
            console.log(ddata);
            $.each(ddata, function (val, text) {
                $('#compliance-selector').append($('<option></option>').val(text).html(val))
            });
        });
    }
});

$("#assessment-selector").change(function () {
    console.log("inside assessment selector");
    var selectedText = $("#assessment-selector :selected").text();
    var selectedVal = $("#assessment-selector :selected").val();
    console.log(" Selected Text :" + selectedText);
    console.log(" Selected Value :" + selectedVal);
    
    // to be used for future computations
    localStorage.setItem("assessmentId",selectedVal);

    $.each(assessmentSelectedObj, function (index, value) {
        console.log(index);
        console.log(value["assessmentId"]);
        if (selectedVal === value["assessmentId"]) {
            console.log("true");
            $("#assessment-description").val(value["remarks"]);
        }
    });

    $.ajax({
        url: "http://localhost:8080/compsecure-web/getComplianceDetails",
        data: {selected: selectedVal}
    }).then(function (data) {
        console.log("Inside assessment-change method");
        console.log(data);
        var ddata = $.parseJSON(data);
        console.log(ddata);
        $.each(ddata, function (val, text) {
            $('#compliance-selector').append($('<option></option>').val(text).html(val))
        });
    });
});

$("#compliance-selector").change(function () {
    var selectedText = $("#compliance-selector :selected").text();
    var selectedVal = $("#compliance-selector :selected").val();
    console.log(selectedText);
    console.log(selectedVal);
    $("#comp-description").val(selectedVal);
    complianceSelected  = selectedVal;
    complianceId = selectedText;
});

$("#button-start").click(function(){
//   alert("Start Clicked");
   console.log(complianceSelected);
   localStorage.setItem("complianceId",complianceSelected);
   window.location.href = "questionnaire";
//   $.ajax({
//        url: "http://localhost:8080/CompSecureApplication/toQuestionnaire",
//        data:{"selected":complianceSelected}
//    }).then(function(data){
//        console.log(data);
//        var obj = $.parseJSON(data);
//        console.log(obj.complianceId);
//    });
});

