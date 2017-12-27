/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var questionsList = [];
    var i=0;
    
$(document).ready(function () {
    var complianceName = localStorage.getItem("complianceName");
    var selectedOption = localStorage.getItem("selectedOption");
    
//    alert(selectedOption);
    
     
    $("label[for='q_comp_name']").html(complianceName);
    $.ajax({
       url: "/compsecure-web/getControls",
       data : {"complianceName" : complianceName,
    	   		"selectedOption": selectedOption
       		}
    }).then(function(data){
        console.log(data);
        $("#loading").hide();
        var obj = $.parseJSON(data);
        console.log(obj);
         for(var i in obj){
              console.log(obj[i]['controlCode']);
              //var strDiv ="<label class='label-general input-group' style='padding-left:55px;padding-bottom:5px;' id='qControlLabel' name='qControlLabel'>Control " + obj[i]['controlCode']+"</label>";
              var strDiv = "<label class='label-general input-group' style='padding-left:55px;'>Control : <input type='text' readonly='readonly' id='qControlLabel' name='qControlLabel' value='"+obj[i]['controlCode']+"'></label> " + 
              		"<label class='label-general input-group' id='qControlValueLabel' style='padding-left:55px;padding-bottom:5px;'> Control-Value : " +obj[i]['controlValue']+"</label>";
              var quesSpan = "";
              var questionsList = obj[i]['controlQuestions'];
              if(!$.isEmptyObject(questionsList)){
	              $.each(questionsList,function(key,value){
	              quesSpan = quesSpan + "<div class='input-group'><span class='input-group-addon'>Question Code</span>\n\
	                                <input id='controlQuestionCode' style='width: inherit;' type='text' class='form-control' name='controlQuestionCode' value='"+ value['questionCode'] +"' placeholder='Question Code'/></div><br>\n\
	                                \n\<div class='input-group'><span class='input-group-addon'>Question</span>\n\
	                                <input id='controlQuestion' style='width: 80%;' type='text' class='form-control' name='controlQuestion' value='"+ value['question'] +"' placeholder='Question'/></div>\n\
	                              </div><br>";
	              });
	              quesSpan = quesSpan + "<br><button type='button' id='qAddControlQuestion"+i+"' class='btn btn-success qb'>Add Question</button><br>";
              }else{
            	  quesSpan = "<div class='input-group'><span class='input-group-addon'>Question Code</span>\n\
                  <input id='controlQuestionCode' style='width: inherit;' type='text' class='form-control' name='controlQuestionCode' placeholder='Question Code'/></div><br>\n\
                  \n\<div class='input-group'><span class='input-group-addon'>Question</span>\n\
                  <input id='controlQuestion' style='width: 80%;' type='text' class='form-control' name='controlQuestion' placeholder='Question'/></div>\n\
                  <br><button type='button' id='qAddControlQuestion"+i+"' class='btn btn-success qb'>Add Question</button><br>\n\
                </div>";
              }
              $("#questionsContainer").append(strDiv).append(quesSpan);
         }
    });
    
//    $("#button-delete-control").click(function () {
//        $("table tbody").find('input[name="control_no"]').each(function () {
//            if ($(this).is(":checked")) {
//                $(this).parents("tr").remove();
//            }
//        });
//    });
});
$(document).on('click','.qb',function(){
//        alert("clicked");
//        alert($(this).attr("id"));
//        alert($(this).closest("label[name='qControlLabel']").text());
         var quesSpan = "<div class='input-group' div='questionDiv'><span class='input-group-addon'>Question Code</span>\n\
                                <input id='controlQuestionCode' style='width: inherit;' type='text' class='form-control' name='controlQuestionCode' placeholder='Question Code'/></div><br>\n\
                                \n\<div class='input-group'><span class='input-group-addon'>Question</span>\n\
                                <input id='controlQuestion' style='width: 50%;' type='text' class='form-control' name='controlQuestion' placeholder='Question'/></div>";
        $(this).before("<br> "+quesSpan+"<br>");
    });
    
$("#button-save").click(function () {
	$("#loading").show();
//        alert("Inside the buttonsave");
        var x = $("#questionForm").serialize();
//        alert($("#qControlLabel").text());
        console.log(JSON.stringify(x));
        $.ajax({
            type: "POST",
            url: "/compsecure-web/saveQuestions",
            data: {details: JSON.stringify(x)},
            dataType: "json"
        }).done(function (data) {
        	$("#loading").hide();
            console.log(data);
            $("#dialog").dialog({
				 modal: true,
	             title :"Questions-Add",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                    window.location.href="maturity_definition_add.html";
	                }
	            },
			});
           
        });
    });

function QObj(code,question){
	this.questionCode = code;
	this.question = question;
}

function quesObj(name,count){
	return questionsList.push(newObject(questionCode,question));
}

function newObject(questionCode,question){
	this.questionCode = questionCode;
	this.question = question;
}

