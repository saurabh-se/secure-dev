$(document).ready(function(){
	 
	var complianceName = localStorage.getItem("complianceName");
	
	 $("#selFromRangeVal").text($("#from-range-value").val()); 
	 $("#selToRangeVal").text($("#to-range-value").val()); 
	 $("#loading").hide();
	 $("#from-range-value").change(function(){
	       $("#selFromRangeVal").text($("#from-range-value").val()); 
	    });
	    
	     $("#to-range-value").change(function(){
	       $("#selToRangeVal").text($("#to-range-value").val()); 
	    });
	     
	$("#button-submit").click(function(){
		$("#loading").show();
		var fromRange = $("#from-range-value").val();
		var toRange = $("#to-range-value").val();
		
		alert(fromRange+"-"+toRange);
		
		$.ajax({
			
			url:"/compsecure-web/enterMaturityDefinitionValues",
			data:{
				"complianceName":complianceName,
				"rangeFrom":fromRange,
				"rangeTo":toRange
			},
			type:"POST"
		}).done(function(data){
			$("#loading").hide();
			console.log(data);
			if(data){
	            $("#confirmation-dialogue").dialog({
					 modal: true,
		             title :"Confirmation",
		             dialogClass :"dialogStyle",
		             width: 400,
		            buttons : {
		                Ok: function() {
		                    $(this).dialog("close"); //closing on Ok click
		                    window.location.href="home";
		                }
		            },
				});
			}else{
				$("#loading").hide();
				alert("Something went wrong. Please try again");
			}
		});
	});
});