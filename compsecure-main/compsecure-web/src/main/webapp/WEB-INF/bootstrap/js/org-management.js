$(document).ready(function(){
	$("#loading").hide();
	$.ajax({
		url :"ad/getOrgList",
		type:"GET",
		headers: {"Authorization": "Bearer "+localStorage.getItem('token')}
	}).done(function(data){
		console.log(data);
	});
});