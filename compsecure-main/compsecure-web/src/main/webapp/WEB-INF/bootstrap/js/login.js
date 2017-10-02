/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	console.log("here");
	$("#btnSubmit").click(function(event){
            event.preventDefault();
                var username = $("#username").val();
                var password = $("#inputPassword").val();
                
                var loginObj = new LoginObj(username,password);
		
		var formObj = $("#loginForm").serialize();
		console.log(JSON.stringify(loginObj));
               
		$.ajax({
			 url: "/compsecure-web/login",
			 type:"POST",
			 contentType:"application/json",
             dataType: "JSON",
			 data: JSON.stringify(loginObj)			
		}).then(function(data){
			console.log(data);
			if(data==="invalid"){
				alert("Invalid Credentials");
				localStorage.clear();
				window.location.href ="./";
			}
			else{
				console.log(data);
				var role=data["role"]["roleDescription"];
				var roleId = data["role"]["roleId"];
				var userId=data["userId"];
				
//				alert(role  + " " + roleId + " " +userId);
				
				localStorage.setItem("userId",userId);
				localStorage.setItem("roleId",roleId);
				console.log(role);
				if (role === "admin") {
					window.location.href = "home";
				} else {
					window.location.href = "self-assessment_1";
				}
			}
		});
	});
        function LoginObj(u,p){
            this.username = u;
            this.password = p;
        }
	
});
