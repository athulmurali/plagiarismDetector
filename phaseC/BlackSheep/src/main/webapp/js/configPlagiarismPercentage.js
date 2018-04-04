window.onload  = function(){
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
}



function redirectToCodeStats()
{
    window.location = "../templates/codeStats.html";
}
