/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
//    $("#ce-button-next").click(function () {
//        window.location.href = "maturity-effectiveness.html";
//    })

    var assessmentId = localStorage.getItem("assessmentId");
    $.ajax({
        url: "http://localhost:8080/compsecure-web/getDomainDetails",
        data: {"assessmentId": assessmentId}
    }).then(function (data) {
        console.log("Completed....");
        console.log(data);
        var obj = $.parseJSON(data);

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
                                    //alert(controlValue);

//                                         cntrlHtml = cntrlHtml +"<label'>"+controlValue+"</label>"+"<br>"})+"</script>"
                                    cntrlHtml = cntrlHtml + "\n\
                                                                <tr><td>" + count + "</td><td><div style='display:inline-table;width:150px;'><input type='text' readonly='readonly' style='width:50px;border:none' id='controlId' name='controlCode' value='" + controlCode + "'></input>\n\
                                                                    <button type='button' title='Click for Questions' class='btn btn-info ce-qBtn' name='" + count + "' id='qBtn" + count + "' value='" + controlCode + "'>Display Questions</button></div></td> \n\
                                                                <td><div id='controlValue' class='text-left' style='width:300px;'>" + controlValue + "</div></td>\n\
                                                                <td><select name='ceSelectDocEffectiveness' id='ceSelectDocEffectiveness'><options><option>Select</option><option>Compliant</option><option>Non Compliant</option><option>Partially Compliant</option></options></select></td> \n\
                                                                <td ><input type='file' id='upload-file'>" +
                                                                	"<button class='btn btn-info btnUpload' id='docEff' name="+controlCode+">Upload</button></td> \n\
                                                                <td><textarea name='ce-remarksTA' id='ce-remarksTA' rows='2'></textarea></td>\n\
                                                                <td><select name='ceSelectImplEffectiveness' id='ceSelectImplEffectiveness'><options><option>Select</option><option>Compliant</option><option>Non Compliant</option><option>Partially Compliant</option></options></select></td>\n\
                                                                <td><input name='upload-implEffectiveness' type='file' id='upload-implEffectiveness'>" +
                                                                	"<button class='btn btn-info btnUpload' id='implEff'>Upload</button></td>\n\
                                                                <td><textarea name='ce-remarksImplEff' id='ce-remarksImplEff' rows='2'></textarea></td>\n\
                                                                <td><select name='ceSelectRecEffectiveness' id='ceSelectRecEffectiveness'><options><option>Select</option><option>Compliant</option><option>Non Compliant</option><option>Partially Compliant</option></options></select></td>\n\
                                                                <td><input name='upload-recEffectiveness' type='file' id='upload-recEffectiveness'>" +
                                                                "<button class='btn btn-info btnUpload' id='recEff'>Upload</button></td>\n\
                                                                <td><textarea name='ce-remarksRE' id='ce-remarksRE' rows='2'></textarea></td> " +
                                                                "<td><select class='form-control' id='maturityEffSelector'><option>1</option><option>2</option><option>3</option></select></td></tr>\n\
\n\<tr id='qDisplay'><td colspan='12' style='display:none;' id='tdQuestions"+count+"'><div id='demo" + count + "' class='collapse'>Questions</div></td></tr>\
                                                              "
                                }) + "</script>"
//                                         $("#ce-controlTableTR").after("<tr> <th scope='row'>1</th> <td><div id='controlId'>"+controlCode+"<div></td> <td><div id='controlValue'>"+controlValue+"</div></td> <td> </td> <td><input type='file' id='upload-file'></td> <td> </td> <td> </td> <td><input type='file' id='upload-file'></td> <td> </td> <td> </td> <td> </td> </tr>")}) +"</script>"    
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
    
    
    $(document).on("click", ".btnUpload", function (event) {
    	
    	event.preventDefault();
    	
    	alert("AssessmentId - " + assessmentId);
    	alert("ControlCode - " + $(this).attr('name'));
    	
    	var controlCode = $(this).attr('name');
    	
    	var file_data = "";
    	
    	alert("Upload Button Clicked");
    	alert($(this).attr('id'));
    	if($(this).attr('id')==='docEff'){
//    		fileSelect = $("#upload-file").val();
    		file_data = $("#upload-file").prop("files")[0];
    	}else if($(this).attr('id')==='implEff'){
    		file_data = $("#upload-implEffectiveness").prop("files")[0];
    	}else{
    		file_data = $("#upload-recEffectiveness").prop("files")[0];
    	}
    		alert(file_data);
    		var form_data = new FormData();
    		form_data.append("file", file_data) 
    		              // Adding extra parameters to form_data
    		$.ajax({
                    url: "http://localhost:8080/compsecure-web/doUpload/"+$(this).attr('id')+"/"+assessmentId+"/"+ controlCode,
                    dataType: 'script',
                    cache: false,
                    contentType: false,
                    processData: false,
                    data: form_data,                         // Setting the data attribute of ajax with file_data
                    type: 'post'
    		});
    	
    });
    
    $("#ce-button-next").on("click", function (event) {
    	var count =0;
        event.preventDefault();
        alert("clicked!!");
        var t = $("#controlEffectiveForm").serializeArray();
        console.log($("#controlEffectiveForm").serialize());
        console.log(JSON.stringify(t));
        
        var table = document.getElementById('control_effectiveness-list3');
        // count of the number of rows
        var rowLength = table.rows.length;
        alert(rowLength);
        
        var controlEffectiveness= [];
        
        $("#controlEffectiveForm tr").each(function() {
//        	console.log($(this).find("input[name='controlCode']").val());
//        	console.log($(this).find("select[name='ceSelectDocEffectiveness']").val());
//        	console.log($(this).find("textarea[name='ce-remarksTA']").val());
//        	console.log($(this).find("select[name='ceSelectImplEffectiveness']").val());
//        	console.log($(this).find("textarea[name='ce-remarksImplEff']").val());
//        	console.log($(this).find("select[name='ceSelectRecEffectiveness']").val());
//        	console.log($(this).find("textarea[name='ce-remarksRE']").val());
        	
        	var controlCode = $(this).find("input[name='controlCode']").val();
        	var selDocEff 	= $(this).find("select[name='ceSelectDocEffectiveness']").val();
        	var remDocEff 	= $(this).find("textarea[name='ce-remarksTA']").val();
        	var selImplEff 	= $(this).find("select[name='ceSelectImplEffectiveness']").val();
        	var remImplEff	= $(this).find("textarea[name='ce-remarksImplEff']").val()
        	var selRecEff 	= $(this).find("select[name='ceSelectRecEffectiveness']").val();
        	var remRecEff 	= $(this).find("textarea[name='ce-remarksRE']").val();
        	
        	 var ceObj = new CEObj(controlCode,selDocEff,remDocEff,selImplEff,remImplEff,selRecEff,remRecEff);
        	 controlEffectiveness.push(ceObj);
        	
         })
         console.log(JSON.stringify(controlEffectiveness));
        $.ajax({
            url: "http://localhost:8080/compsecure-web/saveControlEffectiveness",
            type: "POST",
            contentType:"application/json",
            dataType: "JSON",
            data:JSON.stringify(controlEffectiveness),
    }).then(function(data){
    	alert(data);
        console.log(data);
        window.location="control-effectiveness";
    });
//        window.location="maturity-effectiveness";
    });
});

	function CEObj(controlCode,selDocEff,remDocEff,selImplEff,remImplEff,selRecEff,remRecEff){
		if(controlCode !='undefined'){
			this.controlCode = controlCode; 
			this.docEffectiveness = selDocEff; 
			this.docEffRemarks = remDocEff;
			this.implEffectiveness = selImplEff;
			this.implEffRemarks = remImplEff;
			this.recEffectiveness = selRecEff;
			this.recEffRemarks = remRecEff;
		}
	}

/**
 * This method is used to display the questions with details 
 * when the user clicks on the "DisplayQuestions" button
 */
$(document).on("click", ".ce-qBtn", function (event) {
	var assessmentId = localStorage.getItem("assessmentId");
    var clicked = "";
    alert($(this).attr('name'));  
    clicked = $(this).attr('name');
    var controlCode = $(this).val();
    
    console.log(controlCode);
    console.log(assessmentId);
    
    var html = "";
    $.ajax({
        url: "http://localhost:8080/compsecure-web/getQuestionResponse",
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

