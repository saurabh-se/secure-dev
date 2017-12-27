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
    var complianceDesc = localStorage.getItem("complianceId");
    var complianceName = localStorage.getItem("complianceName");
    
    $("#complianceNameLabelValue").text(localStorage.getItem("complianceName"));
    $("#assessmentNameLabelValue").text(localStorage.getItem("assessmentName"));
    $("#orgNameLabelValue").text(localStorage.getItem("organizationName"));
    
    $.ajax({
        url: "/compsecure-web/getDomainDetails",
        data: {
        		"assessmentId": assessmentId,
        		"complianceName": complianceName
        },
        async:false
    }).done(function (data) {
        console.log("Completed....");
        $("#loading").hide();
        console.log(data);
        if(data==="/login"){
        	window.location.href="logout.html";
        }
        var obj = $.parseJSON(data);
        
//        console.log(obj);
        var i = 0;
//        var strTable = "<table id='control_effectiveness-list3' class='table table-sm table-bordered'> <thead> <tr> <th>#</th> <th>Control Code</th> <th>Control Name</th> <th>Doc Effectiveness(C/NC/PC)</th> <th>Doc Effectiveness Evidence</th> <th>Remarks</th> <th>Implementation Effectiveness (C/NC/PC)</th> <th>Implementation Effectiveness Evidence </th> <th>Record Effectiveness (C/NC/PC)</th> <th>Record Effectiveness Evidence</th> <th>Record Effectiveness Remarks</th> </tr> </thead> <tbody> \n\
//                        <tr id='ce-controlTableTR'>  </tbody> </table>";
        var qBtnCollapse = "<button type='button' class='btn btn-info' id='qBtn'>Questions</button>\n\
        <div id='demo' class='collapse'>Questions</div>";
        
        var maturityLevelHtml = "";
        
        $.ajax({
        	url: "/compsecure-web/getMaturityLevels",
        	data: {"complianceName": complianceName },
        	async:false
        }).done(function(data){
        	console.log(data);
        	maturityLevelHtml = "<select class='form-control' id='maturityEffSelector'>"+data+"</select>";
        	console.log(maturityLevelHtml);
        });
        

        $.each(obj, function (key, value) {
            var count = 0;
            var domainName = value["domainName"];
            var domainCode = value["domainCode"];
            var subdomData = value["subdomain"];
            
            var domainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Domain Name</th> <th>Domain Code</th></tr> </thead> <tbody> \n\
                        <tr><td>" + domainName + "</td><td>" + domainCode + "</td></tbody> </table>";
            
            console.log("DOMAIN TABLE : " + domainDetTable);
            
            var htmlPHeading = "<div class='panel-heading' style='padding: 1px;border:0'>";
            var htmlPTitle = "<a data-toggle='collapse' href='#collapse" + i + "' id='ce-panel-heading' id='ce-domainDataToggleId'>\n\
                                  <label class='label label-default' style='background: none;color: #d9534f' id='ce-domainId'>" + domainName + "</label></a></div><br>";
           
            var htmlPSubdomainContent = "<div id='collapse"+i+"' class='panel-collapse collapse'>";
            

            $.each(subdomData, function (key, value) {
//                alert(value["subdomainValue"]);
                var subdomainValue = value["subdomainValue"];
                var subdomainCode = value["subdomainCode"];
                var controlData = value["control"];
                var principle = value["principle"];
                var objective = value["objective"];
                var cntrlHtml = "";
                
                var subdomainDetTable = "";
                var htmlPCEContent = "";
                
//                var subdomainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Subdomain Code</th> <th>Principle</th> <th>Objective</th>  </tr> </thead> <tbody> \n\
//                        <tr><td>" + subdomainCode + "</td><td>" + principle + "</td><td>" + objective + "</td></tbody> </table>";
                
                subdomainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Subdomain Code</th> <th>Principle</th> <th>Objective</th>  </tr> </thead> <tbody> \n\
                <tr><td>" + subdomainCode + "</td><td>" + principle + "</td><td>" + objective + "</td></tbody> </table>";
        
        
                htmlPSubdomainContent = htmlPSubdomainContent + "<div>\n\
                                   <div class='panel-body' id='ce-domain-panel'>"+subdomainDetTable+"\n\
                                      <div class='panel-heading' style='padding: 1px;'>\n\
                                        <a data-toggle='collapse'  href='#subdomaincollapse" + i + "'>\n\
                                        <label class='label label-default' style='background: none;color: #1b6d85' id='ce-domainId'>" 
                                            + subdomainValue + "</label></a></div><br>";

                                $.each(controlData, function (controlKey, cntrlValue) {
                                    var controlValue = cntrlValue["controlValue"];
                                    var controlCode = cntrlValue["controlCode"];
                                    
                                    var docEffectiveness = "";
                                    var docEffRemarks	 = "";
                                    var implEffectiveness = "";
                                    var implEffRemarks = "";
                                    var recEffectiveness = "";
                                    var recEffRemarks = "";
                                    var docEffEvidences = "";
                                    var implEffEvidences = "";
                                    var recEffEvidences = "";
                                    
                                    if(cntrlValue["controlEffectiveness"]!=undefined){
                                    	 docEffectiveness = cntrlValue["controlEffectiveness"]["docEffectiveness"];
//                                    	 alert(docEffectiveness);
                                    	 docEffRemarks = cntrlValue["controlEffectiveness"]["docEffRemarks"];
//                                    	 alert(docEffRemarks);
                                    	 implEffectiveness = cntrlValue["controlEffectiveness"]["implEffectiveness"];
//                                    	 alert(implEffectiveness);
                                    	 implEffRemarks = cntrlValue["controlEffectiveness"]["implEffRemarks"];
//                                    	 alert(implEffRemarks);
                                    	 recEffectiveness = cntrlValue["controlEffectiveness"]["recEffectiveness"];
//                                    	 alert(recEffectiveness);
                                    	 recEffRemarks = cntrlValue["controlEffectiveness"]["recEffRemarks"];
//                                    	 alert(recEffRemarks);
                                    	 docEffEvidences = cntrlValue["controlEffectiveness"]["docEffEvidences"];
//                                    	 alert(docEffEvidences);
                                    	 implEffEvidences = cntrlValue["controlEffectiveness"]["implEffEvidences"];
//                                    	 alert(implEffEvidences);
                                    	 recEffEvidences = cntrlValue["controlEffectiveness"]["recEffEvidences"];
//                                    	 alert(recEffEvidences);
                                    }
                                    
//                                    $("#ceSelectDocEffectiveness").val(docEffectiveness);
                                    
                                    count++;
                               
                                    cntrlHtml = cntrlHtml + "\n\
                                                                <tr><td>" + count + "</td><td><div style='display:inline-table;width:150px;'><input type='text' readonly='readonly' style='width:50px;border:none' id='controlId' name='controlCode' value='" + controlCode + "'></input>\n\
                                                                    <button type='button' title='Click for Questions' class='btn btn-info ce-qBtn' name='" + count + "' id='qBtn" + count + "' value='" + controlCode + "'>Display Questions</button></div></td> \n\
                                                                <td><div id='controlValue' class='text-left' style='width:300px;'>" + controlValue + "</div></td>\n\
                                                                <td><div id='docEffectivenessSel'>"+setSel("ceSelectDocEffectiveness",docEffectiveness)+"</div></td>"+
                                                                "<td>"+getUploadedFiles(docEffEvidences)+"<input type='file' id='upload-file-"+controlCode+"'>" +
                                                                "<button class='btn btn-info btnUpload' id='docEff' name='"+controlCode+"'>Upload</button></td> \n\
                                                                <td><textarea name='ce-remarksTA' id='ce-remarksTA' rows='2'>"+check(docEffRemarks)+"</textarea></td>\n\
                                                                <td><div id='recEffectivenessSel'>"+setSel("ceSelectRecEffectiveness",recEffectiveness)+"</div>  \n\
                                                                <td>"+getUploadedFiles(recEffEvidences)+"<input name='upload-implEffectiveness' type='file' id='upload-implEffectiveness-'"+controlCode+">" +
                                                                "<button class='btn btn-info btnUpload' id='implEff' name="+controlCode+">Upload</button></td>\n\
                                                                <td><textarea name='ce-remarksImplEff' id='ce-remarksImplEff' rows='2'>"+check(implEffRemarks)+"</textarea></td>\n\
                                                                <td><div id='implEffectivenessSel'>"+setSel("ceSelectimplEffectiveness",implEffectiveness)+"</div> \n\
                                                                <td>"+getUploadedFiles(recEffEvidences)+"<input name='upload-recEffectiveness' type='file' id='upload-recEffectiveness-'"+controlCode+">" +
                                                                "<button class='btn btn-info btnUpload' id='recEff' name="+controlCode+">Upload</button></td>\n\
                                                                <td><textarea name='ce-remarksRE' id='ce-remarksRE' rows='2'>"+check(recEffRemarks)+"</textarea></td> " +
                                                                "<td>"+maturityLevelHtml+"</td></tr>\n\
                                                                <tr id='qDisplay'><td colspan='12' style='display:none;' id='tdQuestions"+count+"'>" +
																"<div id='demo" + count + "' class='collapse'>Questions</div></td></tr>";
                                });
                                
                                htmlPCEContent = htmlPCEContent+ "<div id='subdomaincollapse" + i + "'><div><div><br>\n\
                                <table id='control_effectiveness-list3' class='table table-sm table-bordered'> <thead> <tr> <th>#</th><th style='width:35%;'>Control Code</th><th>Control</th><th>Doc Effectiveness(C/NC/PC)</th> <th>Doc Effectiveness Evidence</th> <th>Remarks</th> <th>Implementation Effectiveness (C/NC/PC)</th> <th>Implementation Effectiveness Evidence </th><th>Remarks</th> <th>Record Effectiveness (C/NC/PC)</th> <th>Record Effectiveness Evidence</th> <th>Record Effectiveness Remarks</th><th>Maturity Effectiveness</th> </tr> </thead> <tbody> \n\
                                <tr id='ce-controlTableTR'>" + cntrlHtml + "</tbody> </table>" +
                                "</div></div></div>";
                                
                                htmlPSubdomainContent = htmlPSubdomainContent + htmlPCEContent;
                i++;
//                });
            });
//            $("#ce-mainPanelId").append(domainDetTable+htmlPHeading+htmlPTitle+htmlPSubdomainContent);
            $("#ce-mainPanelId").append(htmlPTitle +htmlPHeading+ htmlPSubdomainContent + "</div><br>");
        });
    });
    
    
    function setSel(selValue,value){
    	var compliantOptionHtml="";
    	var nonCompliantOptionHtml="";
    	var partiallyCompliantOptionHtml="";
    	
    	switch(value){
    	case "Compliant":
    		compliantOptionHtml = "<option value='Compliant' selected='selected'>Compliant</option>";
    		nonCompliantOptionHtml ="<option value='Non Compliant'>Non Compliant</option>";
    		partiallyCompliantOptionHtml = "<option value='Partially Compliant'>Partially Compliant</option>" ;
    		break;
    	case "Non Compliant":
    		compliantOptionHtml = "<option value='Compliant'>Compliant</option>";
    		nonCompliantOptionHtml ="<option value='Non Compliant' selected='selected'>Non Compliant</option>";
    		partiallyCompliantOptionHtml ="<option value='Partially Compliant'>Partially Compliant</option>" ;
    		break;
    	case "Partially Compliant":
    		compliantOptionHtml = "<option value='Compliant'>Compliant</option>";
    		nonCompliantOptionHtml ="<option value='Non Compliant'>Non Compliant</option>";
    		partiallyCompliantOptionHtml ="<option value='Partially Compliant'  selected='selected'>Partially Compliant</option>" ;
    		break;
    	}
    	
    	var html = "<select name='"+selValue+"' id='"+selValue+"'>" +
    			"<options>"+compliantOptionHtml+nonCompliantOptionHtml+partiallyCompliantOptionHtml+"</options></select>";
    	    	
    	return html;
    }
    
    
    function check(str){
//    	alert("in check");
//        alert(" str " + str);
    	if(str===undefined){
    		return " ";
    	}else if(!str){
            return "";
        }
         else{
             return str;
    	}
    }
    
    
    $(document).on("click", ".btnUpload", function (event) {
    	
    	event.preventDefault();
    	$("#loading").show();
//    	alert("AssessmentId - " + assessmentId);
//    	alert("ControlCode - " + $(this).attr('name'));
    	
    	var controlCode = $(this).attr('name');
    	
    	var file_data = "";
    	
//    	alert("Upload Clicked");	
//    	alert($(this).attr('id'));
    	if($(this).attr('id')==='docEff'){
//    		fileSelect = $("#upload-file").val();
//    		alert($(jq(controlCode)).prop("files"));
    		file_data = $(jq(controlCode)).prop("files")[0];
    	}else if($(this).attr('id')==='implEff'){
    		file_data = $("#upload-implEffectiveness").prop("files")[0];
    	}else{
    		file_data = $("#upload-recEffectiveness").prop("files")[0];
    	}
    		var form_data = new FormData();
    		form_data.append("file", file_data) 
    	    		
    		$.ajax({
                    url: "/compsecure-web/doUpload/"+$(this).attr('id')+"/"+controlCode+"/",
                    dataType: 'script',
                    cache: false,
                    contentType: false,
                    processData: false,
                    data: form_data,                         // Setting the data attribute of ajax with file_data
                    type: 'post'
    		}).done(function(data){
    			$("#loading").hide();
    			$("#dialog").dialog({
					 modal: true,
		             title :"Upload Status",
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
    
    function jq( myid ) {
    	 
        var changedString = "#upload-file-" + myid.replace( /(:|\.|\[|\]|,|=|@)/g, "\\$1" );
//        alert("Escaped String  : " + changedString);
        return changedString;
    }
    
    
    function getUploadedFiles(filesList){
    	var filesHtml="";
    	if(!filesList===undefined || !filesList==""){
	    	console.log(filesList.size);
	    	var no = 1;
	    	$.each(filesList,function(index,value){
	    		filesHtml = filesHtml + "<a id='filesList' href='getFile?filename=" + value + "'>"+no+". "+value+"</a><br>";
	    		no = no+1;
	    	});
	    	console.log(filesHtml);
    	}
    	return filesHtml;
    }
    
    $("#ce-button-next").on("click", function (event) {
    	$("#loading").show();
    	var count =0;
        event.preventDefault();
        var t = $("#controlEffectiveForm").serializeArray();
        console.log($("#controlEffectiveForm").serialize());
        console.log(t);
        console.log(JSON.stringify(t));
        
        var table = document.getElementById('control_effectiveness-list3');
        // count of the number of rows
        var rowLength = table.rows.length;
//        alert(rowLength);
        
        var controlEffectiveness= [];
        
        $("#controlEffectiveForm tr").each(function() {
        	
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
            url: "/compsecure-web/saveControlEffectiveness",
            type: "POST",
            contentType:"application/json",
            dataType: "JSON",
            data:JSON.stringify(controlEffectiveness)
        }).then(function(data){
        	$("#loading").hide();
        	console.log(data);
        	window.location="maturity-effectiveness";
        });
//        
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
    }).done(function (data) {
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
    });
});

$("#ce-button-cancel").click(function(){
	window.location="home";
});

