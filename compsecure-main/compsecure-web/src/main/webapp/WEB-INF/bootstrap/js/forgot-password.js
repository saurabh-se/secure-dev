$(document).ready(function() {
	$("#loading").hide();
	$("#username").focusout(function(){
//		alert("username entered");
		$.ajax({
			url : "getSecurityQuestions",
			data: {"username":$("#username").val()}
			
		}).done(function(data){
			if(data){
				$("#ok").show();
				$("#securityAnswer").removeAttr( "disabled" );
				$("#securityAnswer").focus();
				$("#btnSubmit").removeAttr("disabled");
				$("#securityQuestion").val(data);
			}else{
				$("#invalid-user").dialog({
					 modal: true,
		             title :"Status",
		             dialogClass :"dialogStyle",
		             width: 400,
		            buttons : {
		                Ok: function() {
		                    $(this).dialog("close"); //closing on Ok click
		                }
		            },
				});
			}
		});
	});
});
	
$("#securityAnswer").focusout(function(){
	$.ajax({
		url : "verifySecurityAnswer",
		data: {"emailId":$("#emailId").val(),"answer":sha512($("#securityAnswer").val())}
	}).done(function(data){
		if(data===true){
			$("#answer-ok").css("color","green");
			$("#changePassword").removeAttr( "disabled" );
			$("#changePassword").focus();
			$("#confirmChangePassword").removeAttr( "disabled" );
		}else{
			$("#incorrect-answer").dialog({
				 modal: true,
	             title :"Status",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                }
	            },
			});
		}
	});
});

$("#btnSubmit").click(function(){
	$("#loading").show();
	var changedPassword = $("#changePassword").val();
	var confirmChangePassword = $("#confirmChangePassword").val();
	if(changedPassword===confirmChangePassword){
		$.ajax({
			url:"savePassword",
			data:{"pwd":sha512($("#confirmChangePassword").val())},
			type:"POST",
			async:false
		}).done(function(data){
			if(data){
				 $("#confirmation").dialog({
					 modal: true,
		             title :"Status",
		             dialogClass :"dialogStyle",
		             width: 400,
		            buttons : {
		                Ok: function() {
		                    $(this).dialog("close"); //closing on Ok click
		                }
		            },
				});
			}
		});
	}else{
		$("#password-mismatch").dialog({
			 modal: true,
            title :"Status",
            dialogClass :"dialogStyle",
            width: 400,
           buttons : {
               Ok: function() {
                   $(this).dialog("close"); //closing on Ok click
               }
           },
		});
		$("#confirmChangePassword").css("border-color","red");
		return false;
	}
});

function ValidationObject(username,password,question,answer){
	this.validity			= true; 
	this.userName 			= username;
	this.changedPassword 	= sha512(password);
	this.securityQuestion 	= question;
	this.securityAnswer 	= answer;
}
