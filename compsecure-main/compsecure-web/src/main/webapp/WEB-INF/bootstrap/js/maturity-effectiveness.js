/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	
    var assessmentId = localStorage.getItem("assessmentId");
    var complianceId = localStorage.getItem("complianceId");
    
    $.ajax({
        url: "/compsecure-web/getCompleteDetails",
        data: {
        		"assessmentId": assessmentId,
        		"complianceId": complianceId
        }
    }).then(function (data) {
        console.log("Completed....");
        
        console.log(data);
        var obj = $.parseJSON(data);

        //get the control effectiveness data if available
        var test  = getDetails(assessmentId,complianceId).done(function(value) {
        	var controlEffDetails = JSON.parse(value);
        	console.log(controlEffDetails);
        	console.log(" Length : " + controlEffDetails.length);
        	
       
        var i = 0;
        var strTable = "<table id='control_effectiveness-list3' class='table table-sm table-bordered'> <thead> <tr> <th>#</th> <th>Control Code</th> <th>Control Name</th> <th>Doc Effectiveness(C/NC/PC)</th> <th>Doc Effectiveness Evidence</th> <th>Remarks</th> <th>Implementation Effectiveness (C/NC/PC)</th> <th>Implementation Effectiveness Evidence </th> <th>Record Effectiveness (C/NC/PC)</th> <th>Record Effectiveness Evidence</th> <th>Record Effectiveness Remarks</th> </tr> </thead> <tbody> \n\
                        <tr id='ce-controlTableTR'>  </tbody> </table>";
        var qBtnCollapse = "<button type='button' class='btn btn-info' id='qBtn'>Questions</button>\n\
        <div id='demo' class='collapse'>Questions</div>";

        $.each(obj, function (key, value) {
            var count = 0;
            var domainName = value["domainName"];
            var domainCode = value["domainCode"];
            var subdomData = value["subdomain"];
            
            var domainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Domain Name</th> <th>Domain Code</th></tr> </thead> <tbody> \n\
                        <tr><td>" + domainName + "</td><td>" + domainCode + "</td></tbody> </table>";

            $.each(subdomData, function (key, value) {
//                alert(value["subdomainValue"]);
                var subdomainValue = value["subdomainValue"];
                var subdomainCode = value["subdomainCode"];
                var controlData = value["control"];
                var principle = value["principle"];
                var objective = value["objective"];
                var cntrlHtml = "";
                console.log(" i : " + i);
                console.log(" count : " + count);
                var subdomainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Subdomain Code</th> <th>Principle</th> <th>Objective</th>  </tr> </thead> <tbody> \n\
                        <tr><td>" + subdomainCode + "</td><td>" + principle + "</td><td>" + objective + "</td></tbody> </table>";

                $("#ce-mainPanelId")
                        .append("<div class='panel-heading' style='padding: 1px;border:0'>\n\
                                      <a data-toggle='collapse' href='#collapse" + i + "' id='ce-panel-heading' id='ce-domainDataToggleId'>\n\
                                      <label class='label label-default' style='background: none;color: #d9534f' id='ce-domainId'>" + domainName + "</label></a></div><br>")
                        .append("<div id='collapse" + i + "' class='panel-collapse collapse'><div class='panel-body' id='ce-domain-panel'>"+domainDetTable+"\n\
                                      <div class='panel-heading' style='padding: 1px;'>\n\
                                      <a data-toggle='collapse'  href='#subdomaincollapse" + i + "'>\n\
                                      <label class='label label-default' style='background: none;color: #1b6d85' id='ce-domainId'>" + subdomainValue + "</label></a></div><br>\n\
                                     \n\
                                      \n\
                                       <script>" +
                                $.each(controlData, function (controlKey, cntrlValue) {
                                    var controlValue = cntrlValue["controlValue"];
                                    var controlCode = cntrlValue["controlCode"];
                                  
                                    count++;
                                    for(n=0;n<controlEffDetails.length;n++){
                                    cntrlHtml = cntrlHtml + "\n\
                                                                <tr><td>" + count + "</td><td><div style='display:inline-table;width:150px;'><input type='text' readonly='readonly' style='width:50px;border:none' id='controlId' name='controlCode' value='" + controlCode + "'></input>\n\
                                                                    <button type='button' title='Click for Questions' class='btn btn-info ce-qBtn' name='" + count + "' id='qBtn" + count + "' value='" + controlCode + "'>Display Questions</button></div></td> \n\
                                                                <td><div id='controlValue' class='text-left' style='width:300px;'>" + controlValue + "</div></td>\n\
                                                                <td>"+controlEffDetails[n]["docEffectiveness"]+"</td> \n\
                                                                <td ><input type='file' id='upload-file'>" +
                                                                	"<button class='btn btn-info btnUpload' id='docEff' name="+controlCode+">Upload</button></td> \n\
                                                                <td>"+controlEffDetails[n]["docEffRemarks"]+"</td>\n\
                                                                <td>"+controlEffDetails["implEffectiveness"]+"</td>\n\
                                                                <td><input name='upload-implEffectiveness' type='file' id='upload-implEffectiveness'>" +
                                                                	"<button class='btn btn-info btnUpload' id='implEff'>Upload</button></td>\n\
                                                                <td>"+controlEffDetails[n]["implEffRemarks"]+"</td>\n\
                                                                <td>"+controlEffDetails[n]["recEffectiveness"]+"</td>\n\
                                                                <td><input name='upload-recEffectiveness' type='file' id='upload-recEffectiveness'>" +
                                                                "<button class='btn btn-info btnUpload' id='recEff'>Upload</button></td>\n\
                                                                <td>"+ controlEffDetails[n]["recEffRemarks"]+"</td> " +
                                                                "<td><select class='form-control' id='maturityEffSelector'><option>1</option><option>2</option><option>3</option></select></td></tr>\n\
\n\<tr id='qDisplay'><td colspan='12' style='display:none;' id='tdQuestions"+count+"'><div id='demo" + count + "' class='collapse'>Questions</div></td></tr>\
                                                             "
                                    }}) + "</script>"
                                ).append("<div id='subdomaincollapse" + i + "' class='panel-collapse collapse'><div class='panel-body'><div>" + subdomainDetTable + "<br>\n\
                                        <table id='control_effectiveness-list3' class='table table-sm table-bordered'> <thead> <tr> <th>#</th><th style='width:35%;'>Control Code</th><th>Control</th><th>Doc Effectiveness(C/NC/PC)</th> <th>Doc Effectiveness Evidence</th> <th>Remarks</th> <th>Implementation Effectiveness (C/NC/PC)</th> <th>Implementation Effectiveness Evidence </th><th>Implementation Effectiveness Remarks </th> <th>Record Effectiveness (C/NC/PC)</th> <th>Record Effectiveness Evidence</th> <th>Record Effectiveness Remarks</th><th>Maturity Level</th></tr> </thead> <tbody> \n\
                                        <tr id='ce-controlTableTR'>" + cntrlHtml + "</tbody> </table>" +
                        "</div></div></div>");
                                
                //                              alert(cntrlHtml);

                i++;
//                });

            });
        });
    });
    });
});

function getDetails(assessmentId,complianceId){
	return $.ajax({
        url: "/compsecure-web/getControlEffectivenessDetails",
        data: {
        		"assessmentId": assessmentId,
        		"complianceId": complianceId
        }
    });
}

/**
 * This method is used to display the questions with details 
 * when the user clicks on the "DisplayQuestions" button
 */
$(document).on("click", ".ce-qBtn", function (event) {
	var assessmentId = localStorage.getItem("assessmentId");
	
	
    var clicked = "";
    clicked = $(this).attr('name');
    var controlCode = $(this).val();
    
    console.log(controlCode);
    console.log(assessmentId);
    
    var html = "";
    $.ajax({
        url: "/compsecure-web/getQuestionResponse",
        data: {
        		"controlCode": controlCode,
        		"assessmentId":assessmentId
        	  }
    }).then(function (data) {
        $("#tdQuestions"+clicked).toggle();
        $("#demo" + clicked).toggle();
        var questions = $.parseJSON(data);
        var divStart = "<div class='row' id='qDisplayDiv' style='margin-left:2px'><table id='displayQ'><tr><th>Question Code</th><th>Question</th><th>Question Response</th></tr>";
        var divEnd = "<div class='row' style='background-color: white'></div></table></div>"
        $.each(questions, function (key, value) {
            //html = html + "<ul class='list-unstyled'><li class='list-group-item list-group-item-success'>" + value["questionCode"] + " : "+ value["question"]+ ":" + value["questionResponse"]+ "</li></ul>";
        	html = html + "<tr><td>"+value["questionCode"]+"</td><td>"+ value["question"]+"</td><td>"+ value["questionResponse"]+"</td>";
//            $("#demo" + clicked).html(html);
        });
        $("#demo" + clicked).html(divStart + html + divEnd);
        console.log(value["questionCode"]);
        console.log(value["question"]);
        console.log(value["questionResponse"]);
    });
});

