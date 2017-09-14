/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    var products = {"productData": []};

//    $('li').each(function (index)
//    {
//        products.productData.push({
//            "code": $(this).data('code'),
//            "quantity": parseInt($(this).data('quantity')),
//            "price": parseInt($(this).data('price'))
//        });
//    });

    $("#submit").click(function (event) {
        event.preventDefault();
        alert("clicked!!");

        $(':input', '#expForm').each(function () {
//            alert(this.name + ': ' + this.value);
            products.productData.push({
                "testInput": this.name,
                "testValue": this.value
            });
        });

        var questionsList = [];

        $(':input', '#expForm').each(function () {
//            alert(this.name);
//            alert(this.value);
            
            var questionObj = new QuestionObject(this.name,this.value);
            questionsList.push(questionObj);
        });
        
        var qList = [];
        for(var i=0;i<questionsList.length/3;i++){
            console.log(questionsList[i]);
            
            qList.push(questionsList[i],questionsList[i+1],questionsList[i+2]);
        }
        console.log(JSON.stringify(qList));
        console.log(JSON.stringify(questionsList));

    });

    function QuestionObject(name,value) {
        if(name=="remarks"){
            this.remarks = value;
        }
        if(name=="quest1"){
            this.quest1 = value;
        }
        if(name=="answer1"){
            this.answer1 = value;
        }
        if(name=="selection"){
            this.selection = value;
        }
    }
});

