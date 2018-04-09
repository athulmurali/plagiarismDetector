$(document).ready(function () {

    $("#login").click(function () {
        localStorage.clear();

        ///var password2 = $("#password").val();
        var user2 = $("#uname").val();

        var cred = {

            "user":$("#uname").val(),
            "password": $("#password").val()
        }

        $.ajax({
            type: "POST",
            url: "/userLogin",
            data: JSON.stringify(cred),
            contentType: 'application/json',

            success: function (response) {

                localStorage.setItem("user",user2);

                if(response == "TA")
                {
                    localStorage.setItem("role","TA");
                    location.href = "./upload.html";
                }
                else{
                    localStorage.setItem("role","PROFESSOR");
                    redirect1();
                }
            },

            error: function (e) {
                localStorage.clear();
                console.log('page not found' + e);
                redirect2();

            }
        });

        function redirect1() {
            console.log("in redirect");
            window.location.href = "../templates/configure.html";
        }

        function redirect2() {
            alert("Invalid login credentials!");
            window.location = "../templates/userLogin.html";
        }
    });


});