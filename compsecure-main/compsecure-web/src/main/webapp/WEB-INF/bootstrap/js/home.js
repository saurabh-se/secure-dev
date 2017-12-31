// admin home controller
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
	$("#manage-organization").click(function(){
		 window.location="manage-organization";
	});
});