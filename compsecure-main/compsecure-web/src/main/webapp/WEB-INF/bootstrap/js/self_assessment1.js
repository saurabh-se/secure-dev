/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    
   $("#button-new-assessment").click(function(){
      localStorage.setItem("option","new");
      window.location.href="self-assessment"; 
   });  
   
    $("#button-existing-assessment").click(function(){
      localStorage.setItem("option","existing");
      window.location.href="self-assessment-existing"; 
   });  
   
 });
