
// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    var modal = document.getElementById('modalLogin');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

$(document).ready(function () {

    $("#loginScreen").click(function (){redirectToLogin()} );
    $("#register").click(function (){redirectToRegister()} );
    $("#welcome").click(function (){redirectToWelcome()} );

    $("#taSelect").click(function () {
        console.log("ta role selected");
        console.log(window.location.pathname);
        localStorage.setItem("role","TA");
        console.log("TA role selected");
        document.getElementById('modalLogin').style.display='block';
    });


    $("#professorSelect").click(function () {
        console.log("professor role selected");
        localStorage.setItem("role","PROFESSOR");
        console.log("Professor role selected");
        document.getElementById('modalLogin').style.display='block';
    });


    $("#login").click(function () {
        // localStorage.clear();

        ///var password2 = $("#password").val();
        var user2 = $("#uname").val();

        var cred = {

            "user":$("#uname").val(),
            "password": $("#password").val()
        }
        console.log("login clicked");

        $.ajax({
            type: "POST",
            url: "/userLogin",
            data: JSON.stringify(cred),
            contentType: 'application/json',

            success: function (response) {

                localStorage.setItem("user",user2);

                if( response =="PROFESSOR" && localStorage.getItem("role") == "PROFESSOR")
                {
                    localStorage.setItem("role","PROFESSOR");
                    redirectToConfigure();
                }
                else if(response =="TA" && localStorage.getItem("role") == "TA") {
                    redirectToUpload();
                }

                else{
                    console.log(document.getElementById("passwordStatus"));
                }
            },

            error: function (e) {
                localStorage.clear();

                console.log(document.getElementById("passwordStatus").style.display="block");

                console.log('page not found' + e);
                // redirectToLogin();

            }
        });

    });

});