window.onload  = function(){


    var elem = document.getElementById('rangeSelector');

var rangeValue = function(){
    console.log("hi")
    var newValue = elem.value;
    var target = document.getElementById("rangeValueDisplay")
    target.innerHTML = newValue;
}

elem.addEventListener("input", rangeValue);
}
