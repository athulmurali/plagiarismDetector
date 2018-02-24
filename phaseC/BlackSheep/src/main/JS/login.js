$(document).ready(function() {;


    $("#login").click(function () {
        var password = $("#password").val();
        console.log(password);


        $.ajax({
            type: "POST",
            url: "http://localhost:8080/passcode/",
            data: password,
            contentType: "text/plain",
            success: function(){
                    redirect();
                },
            statusCode: {
                418: function () {
                    console.log('page not found');
                    redirect2();
                },

                400: function () {

                }
            }

         });
    });

    function redirect() {
        window.location="../HTML/check.html";
    }

    function redirect2() {
        window.location="../HTML/notChecked.html";
    }

});