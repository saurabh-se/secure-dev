$(document).ready(function(){
	$("#loading").show();
	$.ajax({
		url :"/compsecure-web/getUserRole",
		type:"GET",
		headers: {"Authorization": "Bearer "+localStorage.getItem('token')}
	}).done(function(data){
		if(data==="1"){
			$("#loading").hide();
			var userListHtml = "";
			$.ajax({
				url:"ad/getUsers",
				type:"GET",
				headers: {"Authorization": "Bearer "+localStorage.getItem('token')}
			}).done(function(data){
				console.log(data);
				
				var users = $.parseJSON(data);
				var count = 1;
				$.each(users,function(index,value){
					userListHtml = userListHtml + "<div class='col-lg-1 tb-text' id='countId'>"+count+"</div>"+
                    "<div class='col-lg-1 tb-text' id='username'>"+value["username"]+"</div>"+
                    "<div class='col-lg-1 tb-text' id='roleId'>"+value["role"]["roleDescription"]+"</div>"+
                    "<div class='col-lg-3 tb-text' id='emailId'>"+value["emailId"]+"</div>"+
                    "<div class='col-lg-2 tb-text' id='orgId'>"+value["organizationName"]+"</div>"+
                    "<div class='col-lg-1 tb-text' >"+value["creationDate"]+"</div>"+
                    "<div class='col-lg-1 tb-text' id='statusId'>"+value["status"]+"</div>"+
                    "<div class='col-lg-1 tb-text'><a href='#' id='editUser' name="+count+">Edit</a></div>"+
					"<div class='col-lg-1 tb-text'><a href='#' id='reactivate' name="+count+">Reactivate</a></div>";
					count++;
					localStorage.setItem("orgName",value["organizationName"]);
				});
				$("#tableBody").append(userListHtml);
			});
		}else{
			$("#loading").hide();
			$("#dialog-fail").dialog({
				 modal: true,
	             title :"Not Authorised",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                    window.location.href="logout.html";
	                }
	            },
			});
		}
	});	
	
	$("#button-addUser").click(function () {
		displayUserForm("new");
    });
	
	  $(document).on("click", "#editUser", function (event){
		  event.preventDefault();
		  var target = event.target || event.srcElement;
		  var i = target.name;
//			alert(i);
		displayUserForm("edit",i);
	});
	  
	  $(document).on("click", "#reactivate", function (event){
		event.preventDefault();
		var target = event.target || event.srcElement;
		var i = target.name;
		
		var username = $("div#username").eq(i-1).text();
		var emailId = $("div#emailId").eq(i-1).text();
		$("#loading").show();
		$.ajax({
			url : "ad/reactivateUsers",
			data: {"username": username, "email":emailId},
			type:"POST",
			dataType: "json",
			headers: {"Authorization": "Bearer "+localStorage.getItem('token')}
		}).done(function(data){
			$("#loading").hide();
			$("#reactivation").dialog({
				 modal: true,
	             title :"Reactivated",
	             dialogClass :"dialogStyle",
	             width: 400,
	            buttons : {
	                Ok: function() {
	                    $(this).dialog("close"); //closing on Ok click
	                }
	            },
			});
			console.log(data);
		});
	  });
});



function displayUserForm(variant,i){
	$("#newUserOrg").html("<options><option>"+localStorage.getItem("orgName")+"<option></options>");
	if(variant==="edit"){
		$("#newUserPassword").prop("disabled",true);
		$("#newUserName").val($("div#username").eq(i-1).text());
		$("#newUserEmailId").val($("div#emailId").eq(i-1).text());
//		$("#newUserRole :selected").text($("div#roleId").eq(i-1).text());
//		$("#userStatus").val($("div#statusId").eq(i-1).val());
	}
    $("#user-creation-form").dialog({
        modal: true,
        title: "Create User",
        dialogClass: "dialogStyleUA",
        width: 400,
        buttons: {
            Submit: function () {
            	var username=$("#newUserName").val();
            	var password =$("#newUserPassword").val();
            	$("#newUserPassword").val=sha512(password);
            	var emailId = $("#newUserEmailId").val();
            	var urole = $("#newUserRole :selected").val();
            	var org = $("#newUserOrg :selected").val();
            	var status = $("#userStatus :selected").text();

            	var userObj = new UserObj(username,sha512(password),emailId,urole,org,status);
            	
            	addUser(userObj,variant);
            	console.log(JSON.stringify(userObj));
                $(this).dialog("close"); //closing on Ok click
            },
            Cancel: function(){
            	$(this).dialog("close");
            }
        },
    });
}

function UserObj(username,password,emailId,urole,org,status){
	
	this.username=username;
	this.password=password;
	this.emailId=emailId;
	
	var roleObj = new RoleObj(urole);
	this.role=roleObj;
	this.organizationName = org;
	this.status = status;
}


function RoleObj(role){
	this.roleId = role;
}

function addUser(userObj,variant){
	var url = "/compsecure-web/ad/saveNewUserDetails";
//	alert("add/edit - " + variant);
	$("#loading").show();
	if(variant==="edit"){
		url="/compsecure-web/ad/updateUserDetails"
	}
	$.ajax({
		url: url,
		data : JSON.stringify(userObj),
		type:"POST",
		dataType: "json",
		 contentType:"application/json",
		 headers: {"Authorization": "Bearer "+localStorage.getItem('token')}
	}).done(function(data){
		$("#loading").hide();
		console.log(data);
		$("#confirmation").dialog({
            modal: true,
            title: "User Created",
            dialogClass: "dialogStyleUA",
            width: 400,
            buttons: {
                    Ok: function(){
                	$(this).dialog("close");
                	window.location.href="user-administration";
                }
            },
        });
	});
}