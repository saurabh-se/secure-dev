/**
 * 
 */
$(document).ready(function () {
	
	   var assessmentId = localStorage.getItem("assessmentId");
	    var complianceId = localStorage.getItem("complianceId");
	    $.ajax({
	        url: "/compsecure-web/getDomainDetails",
	        data: {
	        		"assessmentId": assessmentId,
	        		"complianceId": complianceId
	        }
	    }).then(function (data) {
	    	
	    	var obj = $.parseJSON(data);
	        console.log(obj);
	        
	        var i = 0;
	        var strTable = "<table id='control_effectiveness-list3' class='table table-sm table-bordered'>" +
	        		"<thead> <tr> <th>#</th> <th>Control Code</th> <th>Control Name</th> <th>Doc Effectiveness(C/NC/PC)</th> " +
	        		"<th>Doc Effectiveness Evidence</th> <th>Remarks</th> <th>Implementation Effectiveness (C/NC/PC)</th> " +
	        		"<th>Implementation Effectiveness Evidence </th> <th>Record Effectiveness (C/NC/PC)</th> " +
	        		"<th>Record Effectiveness Evidence</th> <th>Record Effectiveness Remarks</th> </tr></thead><tbody>"+
	                 "<tr id='ce-controlTableTR'></tbody></table>";
	        var qBtnCollapse = "<button type='button' class='btn btn-info' id='qBtn'>Questions</button>\n\
	        <div id='demo' class='collapse'>Questions</div>";

	        $.each(obj, function (key, value) {
	            var count = 0;
	            var domainName = value["domainName"];
	            var domainCode = value["domainCode"];
	            var subdomData = value["subdomain"];
	            
	            var domainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr>" +
	            		"<th>Domain Name</th> <th>Domain Code</th></tr> </thead> <tbody>"+
	            		"<tr><td>" + domainName + "</td><td>" + domainCode + "</td></tbody></table>";
        
	            console.log("DOMAIN TABLE : " + domainDetTable);

		        $.each(subdomData, function (key, value) {
		//            alert(value["subdomainValue"]);
		            var subdomainValue = value["subdomainValue"];
		            var subdomainCode = value["subdomainCode"];
		            var controlData = value["control"];
		            var principle = value["principle"];
		            var objective = value["objective"];
		            var cntrlHtml = "";
		            var subdomainDetTable = "<table id='ce-subdomainDetTableId' class='table table-sm table-bordered'><thead><tr><th>Subdomain Code</th> <th>Principle</th> <th>Objective</th>  </tr> </thead> <tbody> \n\
		                    <tr><td>" + subdomainCode + "</td><td>" + principle + "</td><td>" + objective + "</td></tbody> </table>";
		            
		            $.each(controlData, function (controlKey, cntrlValue) {
                        var controlValue = cntrlValue["controlValue"];
                        var controlCode = cntrlValue["controlCode"];
                        
                        
		            });
		        });
	        });
	    });
});