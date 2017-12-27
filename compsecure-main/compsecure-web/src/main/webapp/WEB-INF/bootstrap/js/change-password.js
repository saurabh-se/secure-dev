$(document).ready(function() {
	var token = getUrlParameter("data");
	var userId="";
//	alert(token);
	$.ajax({
		url : "checkExpiry",
		data: {"data":token},
		async:false
	}).done(function(res) {
		console.log(res);
		var data = $.parseJSON(res);
		var validity = data["validity"];
		userId=data["userId"];
		$("#username").val(userId);
		$("#username").prop("disabled","disabled");
//		alert(validity +" "+ userId);
		
		if (validity===false) {
			alert("Sorry, the token has expired. Please check with admin!!");
			window.close();
		}
		return false;
	});

	$("#loading").hide();

	//TODO: get the salt and then do the comparison
	
	$("#adminGenPassword").focusout(function() {
		if(userId===undefined){
			userId="";
		}
		
		if(!$("#adminGenPassword").val()){
			alert('Username ' + $("#username").val());
			if(!$("#username").val()){
				alert("Username is not valid.\nPlease try again!!");
				
				return false;
			}
			alert("Please enter the admin provided password");
			$("#adminGenPassword").css("border-color","red");
			$("#adminGenPassword").focus();
			return false;
		}
		$.ajax({
			url : "checkAdminGenPassword",
			data:{"userId": userId, "pwd":sha512($("#adminGenPassword").val())},
			async:false
		}).done(function(data){
			if(!data==false){
				$("#adminGenPassword").css("border-color","");
				$("#ok").show();
				$("#changePassword").removeAttr( "disabled" );
				$("#changePassword").focus();
				$("#confirmChangePassword").removeAttr( "disabled" );
				$("#securityQuestion").removeAttr( "disabled" );
				$("#securityAnswer").removeAttr( "disabled" );
				$("#btnSubmit").removeAttr("disabled");
			}else{
				alert("The password was wrong. Please try again!");
				$("#adminGenPassword").css("border-color","red");
				$("#adminGenPassword").focus();
				return false;
			}
		});
		
	});
});

$("#confirmChangePassword").focusout(function(){

	$("#confirmChangePassword").css("border-color","");
	
	if(!($("#confirmChangePassword").val() == $("#changePassword").val())){
		alert("The passwords do not match. \n Please verify and try again!");
		$("#confirmChangePassword").css("border-color","red");
//		$("#confirmChangePassword").focus();
		return false;
	}
});

$("#btnSubmit").click(function(){
	var username = $("#username").val();
	var newPassword = $("#confirmChangePassword").val();
	var securityQuestion = $("#securityQuestion").val();
	var securityAnswer = $("#securityAnswer").val();
	
	var validityObj = new ValidationObject(username,newPassword,securityQuestion,securityAnswer);
	$("#loading").show();
//	alert(JSON.stringify(validityObj));
	$.ajax({
		url 	: "saveChangedPasswordDetails",
		data 	: JSON.stringify(validityObj),
		type	: "POST",
		dataType: "json",
		contentType:"application/json",
		async:false
	}).done(function(data){
		alert("Password has been changed");
		window.location.href="/";
	});
	
});

function ValidationObject(username,password,question,answer){
	this.validity			= true; 
	this.userName 			= username;
	this.changedPassword 	= sha512(password);
	this.securityQuestion 	= question;
	this.securityAnswer 	= sha512(answer);
}

function getUrlParameter(sParam) {
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) {
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) {
			return sParameterName[1];
		}
	}
}
