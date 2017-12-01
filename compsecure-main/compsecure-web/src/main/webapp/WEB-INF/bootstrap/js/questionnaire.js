/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var complianceId = localStorage.complianceId;
    var assessmentId = localStorage.assessmentId;
    var complianceName = localStorage.complianceName;
    var selfAssessmentOption = localStorage.selfAssessmentOption;
    var saved = false;
    
    $("#complianceNameLabelValue").text(localStorage.getItem("complianceName"));
    $("#assessmentNameLabelValue").text(localStorage.getItem("assessmentName"));
    $("#orgNameLabelValue").text(localStorage.getItem("organizationName"));
    
    
//    alert(complianceId);
//    alert(assessmentId);
    if(assessmentId === undefined){
    	console.log("In Questionnaire - no assessmentId");
    	assessmentId = "";
    }
    if(complianceId === undefined){
    	console.log("In Questionnaire - no assessmentId");
    	complianceId = "";
    }
    var questionHtmlTR = "";
    var count = 1;
   $.ajax({
        url: "/compsecure-web/getQuestionnaireDetails",
        data : {
        		 complianceId: complianceName,
        		 assessmentId : assessmentId
        	   }
    }).then(function (data) {
    	$("#loading").hide();
        console.log("Inside questionnaire page");
        console.log(data);
        var questionnaireData = $.parseJSON(data);
        if(questionnaireData.length<1 || questionnaireData ==""){
        	alert("here");
        	$("#dialog-empty").dialog({
				 modal: true,
	             title :"Status",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                    window.location="home";
	                }
	            },
			});
        }else{
        	
        	$.each(questionnaireData,function(key,value){
//            console.log(value["questionCode"] - value["question"]);
            questionHtmlTR = questionHtmlTR + "<tr><td class='text-center' style='padding:2px;'>"+count+"</td>\n\
                                                   <td class='text-center' style='padding:2px;'><input type='text' readonly='readonly' class='qCode' name='questionCode' id='questionCode' style='border:none' value='"+value["questionCode"]+"'></input></td><td class='text-left' style='padding:2px;'>"+value["question"]+"</td>\n\
                                                   <td class='text-center' style='padding:2px;'>" + setSelect(value["questionResponse"]) +
                                                   	"</td>\n\
                                                    <td class='text-center' style='padding:2px;'>" +
                                                    "<textarea class='text-center' rows='2' id='questionRemarks' class='qRemarks' name='questionRemarks'>"+ setRemarks(value["questionRemarks"])+"</textarea></td>";
            count++;
        });
        $("#questionTable").after(questionHtmlTR);
        }
    });
   
   
   function setSelect(selectedValue){
	   
//	   alert(selectedValue);
	   var selectedOption = '';
	   
	   var yesSelected ='';
	   var noSelected = '';
	   var naSelected ='';
	   
	   if(selectedValue === "Yes"){
		   yesSelected = 'selected';
		   selectedOption = "<option selected='selected'>Yes</option>" +
			"<option>No</option>" +
			"<option>NA</option>"; 
	   }
	   else if(selectedValue === "No"){
		   noSelected = 'selected';
		   selectedOption = "<option>Yes</option>" +
			"<option selected='selected'>No</option>" +
			"<option>NA</option>"; 
	   }
	   else if(selectedValue === "NA"){
		   naSelected = 'selected';
		   selectedOption = "<option>Yes</option>" +
			"<option>No</option>" +
			"<option selected='selected'>NA</option>"; 
	   }
	   else{
		   selectedOption = "<option>Yes</option>" +
			"<option>No</option>" +
			"<option>NA</option>"; 
	   }
	   
	   var selectionHtml = "<select class='text-center' id='questionSelect' class='qSelect'  name='questionResponse'>" +
		"<options>" +
		"<option>Select</option>" +
		selectedOption +
		"</options>" +
		"</select>" ;
	   
	   console.log(selectionHtml);
	   
	   return selectionHtml;
   }
   
   function setRemarks(remarks){
//	   alert(remarks);
	   var qRemarks = '';
	   if(remarks===undefined){
		   qRemarks = "";
	   }else{
		   qRemarks = remarks;
	   }
	   return qRemarks;
   }
   
    // to check if the assessment selected is an existing one or a new one
   //  alert(selfAssessmentOption);
    
    $("#qn-button-save").click(function () {
    	$("#loading").show();
    	var selfAssessmentOption = localStorage.getItem("selfAssessmentOption");
    	console.log("SelfAssessmentOption : " + selfAssessmentOption);
    	
    	// alert("next clicked!!");
    	
    	var url = "/compsecure-web/handleJson";
    	if(selfAssessmentOption==="existing"){
    		url = "/compsecure-web/handleAlterJson";
    	}
    	
    	localStorage.setItem("complianceId",complianceId);
        var questionResponse = [];
        console.log($("#questionForm").serialize());
        var trDetails = [];
        
       $('#questionForm tr').each(function(i,v) {
           //alert($(this).find("#questionCode").val());
           var a = $(this).find("#questionCode").val();
           var b = $(this).find("#questionSelect").val();
           var c = $(this).find("#questionRemarks").val();
           var obj = new QObj(a,b,c);
           trDetails.push(obj);
        })
        
        console.log(JSON.stringify(trDetails));
        $.ajax({
                url: url,
                type: "POST",
                contentType:"application/json",
                dataType: "JSON",
                data:JSON.stringify(trDetails),
        }).done(function(data){
//        	alert(data);
        	$("#loading").hide();
        	saved = true;
            console.log(data);
            $("#dialog").dialog({
				 modal: true,
	             title :"Status",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                    window.location="control-effectiveness";
	                }
	            },
			});
        });
    });
    
    function QObj(a,b,c){
        if(a!= 'undefined'){
            this.questionCode=a;
            this.questionResponse = b;
            this.questionRemarks = c;
        }
    }
    
    function QuestionObject(name,value) {
        if(name=="questionCode"){
            this.questionCode = value;
        }
        if(name=="questionResponse"){
            this.questionResponse = value;
        }
        if(name=="questionRemarks"){
            this.questionRemarks = value;
        }
    }
    
    $("#qn-button-next").click(function () {
//    	alert(saved);
    	if(saved===true){
    	 window.location="control-effectiveness";
    	}else{
    		alert("Please save the form");
    		return false;
    	}
    });
    
    $("#qn-button-cancel").click(function(){
    	window.location="home";
    });
});

