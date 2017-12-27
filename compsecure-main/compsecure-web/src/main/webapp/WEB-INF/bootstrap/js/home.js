/**
 * 
 */

$(document).ready(function(data){
	$("#existing").click(function(){
    	selectedOption = "existing";
    	console.log(selectedOption);
        localStorage.setItem("selectedOption",selectedOption);
    	window.location="compliance-header";
	});
	$("#new").click(function(){
	       selectedOption = "new";
	       console.log(selectedOption);
	       localStorage.setItem("selectedOption",selectedOption);
	       window.location="compliance-header";
	});
	$("#user-admin").click(function(){
		 window.location="user-administration";
	});
//	$("#homePageDiv").html("Please select..<br> A New Compliance or Open and Existing Compliance");
//	
//	$("#existNewDialog").dialog({
//	  buttons: {
//	    'New': function() {
//	       selectedOption = "new";
//	       console.log(selectedOption);
//	       localStorage.setItem("selectedOption",selectedOption);
//	       $(this).dialog('close');
//	       window.location="compliance-header";
//	    },
//	    'Existing': function() {

//	    },
//	    'Logout': function() {
//	    	selectedOption = "logout";
//	    	console.log(selectedOption);
//	    	$(this).dialog('close');
//	    	window.location="logout.html";
//	    }
//	  }
//	});
});