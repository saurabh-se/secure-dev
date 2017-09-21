$(document).ready(function () {
	  $("#dialog").dialog({
	        title: "Logout",
	        modal: true,
	        open: function() { var foo = $(this);
	            setTimeout(function() {
	               foo.dialog('close');
	               localStorage.clear();
	           	window.location.href="./";
	            }, 5000);
	        }
	    });
});