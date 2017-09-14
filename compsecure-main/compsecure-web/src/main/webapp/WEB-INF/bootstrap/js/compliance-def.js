/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var i = 1;
    $("#button-add-control").click(function () {
        alert("Add Control");
        $("#cntrl").after("<tr><td style='width: 5%'><input id='control_chkBox_id' type='checkbox' class='checkbox' name='control_no'></td>\n\
                            <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
<td><input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td></tr>");
        //$('#subdomainTable').append('<tr id="cntrl' + (i + 1) + '"></tr>');
    });

    $("#button-save-control").click(function () {
        alert("save");
        var control_code = $("#control_code").val();
        var control_details = $("#control").val();
        $("#cntrl").after("<tr><td><input type='checkbox' name='control_no'></td><td style='width: 25%'><td style='width: 25%'>" + control_code + "</td>\n\
                               <td style='width:35%'>" + control_details + "</td><td style='width:35%'></td></tr>");
        $("#control_code").val("");
        $("#control").val("");
    });

    $("#button-delete-control").click(function () {
        $("table tbody").find('input[name="control_no"]').each(function () {
            if ($(this).is(":checked")) {
                $(this).parents("tr").remove();
            }
        });
    });

    $("#button-save").click(function () {
        alert("Inside the buttonsave");
        var x = $("#complianceForm").serializeArray();
        console.log(x);
        $.ajax({
            method: "post",
            url: "http://localhost:8080/CompSecureApplication/saveComplianceDetails",
            data: {"details": JSON.stringify(x)},
            dataType: "json"
        }).then(function (data) {
            console.log(data);
        });
    });
    
    $("#button-next").click(function(){
      window.location.href="questions_add.html";
    });
    
    $(document).on('click','#button-add-subdomain',function(){
        alert("clicked");
        var strSubdomain ="<br><div class='input-group col-sm-12'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                                    <input id='subdomain_code' type='text' style='width:100%;' class='form-control' name='subdomain_code' placeholder='Subdomain Code'>\n\
                                    <input id='subdomain' type='text' style='width:100%;' class='form-control gap' name='subdomain' placeholder='Subdomain Value'>\n\
                                    <textarea id='principle' style='width:100%;' class='form-control gap' name='principle' placeholder='Principle' rows='2'></textarea>\n\
                                    <textarea id='objective' style='width:100%;' class='form-control gap' name='objective' placeholder='Objective' rows='2'></textarea></div><br>";
    $(this).before(strSubdomain);
    });
    
    

    $(document).on('click', '#button-add-control-domain', function () {
        alert("clicked");
        i++;
        var strCompliance = "<div class='panel panel-default'><div class='panel-heading'><h4 class='panel-title'>\n\
                     <a data-toggle='collapse' class='input-group' href='#collapse1"+i+"'>Control Domain</a></h4></div>\n\
                     <div id='collapse1"+i+"' class='panel-collapse collapse'><div class='panel-body'><div class='input-group'>\n\
                     <span class='input-group-addon'>Domain Name</span><input id='domain_code' type='text' class='form-control ' name='domain_code' placeholder='Control Domain Code'>\n\
                     <input id='domain' type='text' class='form-control gap' name='domain' placeholder='Control Domain Value'></div>\n\
                     <br>\n\
                     <div class='input-group'><span class='input-group-addon' id='subdomainId'>Subdomain</span>\n\
                     <input id='subdomain_code' type='text' class='form-control ' name='subdomain_code' placeholder='Subdomain Code'><input id='subdomain' type='text' class='form-control gap' name='subdomain' placeholder='Subdomain Value'>                                     \n\
                     <textarea id='principle' class='form-control gap' name='principle' placeholder='Principle' rows='2'></textarea>                                     \n\
                     <textarea id='objective' class='form-control gap' name='objective' placeholder='Objective' rows='2'></textarea></div>\n\
                     <br>\n\
                     <button type='button' id='button-add-subdomain' class='btn btn-success gap'>Add Subdomain</button><br><div class='row'>\n\
                     <div class='col-sm-6'><button type='button' id='button-add-control' class='btn btn-success gap'>Add Control</button><button type='button' id='button-delete-control' class='btn btn-success gap'>Delete Control</button></div></div>                                 \n\
                     <br><div><label class='input-group'>Controls</label></div><br>\n\
                     <div class='row'><table class='table' id='subdomainTable'>\n\
                        <thead><tr><th>Select</th><th>Control Code</th><th>Control Detail</th><th></th>\n\
                        </tr></thead>\n\
                     <tbody><tr id='cntrl'><td style='align-content: center'><input type='checkbox' name='control_no'></td>\n\
                     <td style='width: 25%'><input id='control_code' type='text' class='form-control gap' name='control_code' placeholder='Control Code'></td>\n\
                     <td style='width: 35%'><input id='control' type='text' class='form-control gap' name='control' placeholder='Control Details'></td>\n\
                     <!--<td style='width: 35%'><button id='button-save-control' type='button' class='btn btn-success gap'>Add </button></td>--></tr></tbody></table></div>\n\
                     </div></div></div>";
        $(this).before(strCompliance);
    });
});

