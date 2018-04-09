$(document).ready(function () {




    function getUrlVars() {
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for (var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');

            if ($.inArray(hash[0], vars) > -1) {
                vars[hash[0]] += "," + hash[1];
            }
            else {
                vars.push(hash[0]);
                vars[hash[0]] = hash[1];
            }
        }
        return vars;
    }

    // document.getElementById('userId').value = getUrlVars()["userId"];
    //alert(getUrlVars()["userId"]);

    $("#p1").text("Welcome " + getUrlVars()["userId"]);


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
    $("#go").click(function () {
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
                    window.location = "../templates/configPlagiarismPercentage.html";

                }
            });
        });

});



function redirectToCodeStats()
{
    window.location = "../templates/codeStats.html";
}
