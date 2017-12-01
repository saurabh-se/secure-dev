$(document).ready(function () {
	  $("#dialog").dialog({
	        title: "Logout",
	        dialogClass:"dialogStyle",
	        modal: true,
	        open: function() { var foo = $(this);
	            setTimeout(function() {
	               foo.dialog('close');
	               localStorage.clear();
	           	window.location.href="./";
	            }, 2000);
	        }
	    });
});