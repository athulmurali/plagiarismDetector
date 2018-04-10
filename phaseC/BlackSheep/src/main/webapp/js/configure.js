
var elem = document.getElementById('rangeSelector');

$(document).ready(function () {

    $("#configure").click(function(){redirectToConfigure()});

    $("#upload").click(function(){redirectToUpload()});

    $("#codeMatch").click(function(){redirectToCodeMatch()});

    $("#codeStats").click(function(){redirectToCodeStats()});
    $("#done").click(function(){redirectToCodeStats()});
    $("#logOut").click(function(){logOut()});



    $("#go").click(function () {
        console.log("button clicked");

        event.preventDefault();
        var newValue = elem.value;

        var StructureChange = $('#StructureChange:checked').val();
        if(StructureChange==undefined)
            StructureChange=null;

        var CodeChange = $('#CodeChange:checked').val();
        if(CodeChange==undefined)
            CodeChange=null;

        var CommentChange = $('#CommentChange:checked').val();
        if(CommentChange==undefined)
            CommentChange=null;

        var type = {

            "c1":StructureChange,
            "c2":CodeChange,
            "c3":CommentChange,
            "percentage":newValue
        }

        $.ajax({
            type: "POST",
            url: "/config",
            data: JSON.stringify(type),
            contentType: 'application/json',

            success: function (response) {
                console.log("Success");
                window.location = "../templates/upload.html";

            },
            error: function (e) {
                console.log('page not found' + e);
                window.location = "../templates/configure.html";
            }
        });
    });



    // document.getElementById('userId').value = getUrlVars()["userId"];
    //alert(getUrlVars()["userId"]);

    $("#p1").text("Welcome " + localStorage.getItem("user"));

    console.log(localStorage.getItem("saveData"));

    var elem = document.getElementById('rangeSelector');

    var rangeValue = function(){
        var newValue = elem.value;
        var target = document.getElementById("rangeValueDisplay")
        target.innerHTML = newValue;
    }

    elem.addEventListener("input", rangeValue);
    var elem = document.getElementById('rangeSelector');

    var rangeValue = function(){
        var newValue = elem.value;
        var target = document.getElementById("rangeValueDisplay")
        target.innerHTML = newValue + "%";
    }
    elem.addEventListener("input", rangeValue);
});




