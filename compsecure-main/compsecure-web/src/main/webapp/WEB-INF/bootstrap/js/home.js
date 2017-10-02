/**
 * 
 */

$(document).ready(function(data){
	
	$("#homePageDiv").html("Please select..<br> A New Compliance or Open and Existing Compliance");

$("#existNewDialog").dialog({
  buttons: {
    'New': function() {
       selectedOption = "new";
       console.log(selectedOption);
       localStorage.setItem("selectedOption",selectedOption);
       $(this).dialog('close');
       window.location="compliance-header";
    },
    'Existing': function() {
    	selectedOption = "existing";
    	console.log(selectedOption);
        localStorage.setItem("selectedOption",selectedOption);
    	$(this).dialog('close');
    	window.location="compliance-header";
    }
  }
});

});