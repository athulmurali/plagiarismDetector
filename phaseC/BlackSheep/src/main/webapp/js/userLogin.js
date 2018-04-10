// Get the modal


// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    var modal = document.getElementById('id01');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}


$(document).ready(function (){
    // document.getElementById("TAContinue").onclick = function () {
    //     console.log(window.location.pathname);
    //     location.href = "./uploadFiles.html";
    // };


    $("#loginScreen").click(function (){redirectToLogin()} );
    $("#register").click(function (){redirectToRegister()} );
    $("#welcome").click(function (){redirectToWelcome()} );


});