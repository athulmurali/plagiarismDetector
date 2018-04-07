$(document).ready(function () {

    $("#login").click(function () {
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

                if(response == "TA")
                    location.href = "./multipleSubmissionUpload.html?userId="+user2;
                else
                    redirect1();
            },
            error: function (e) {
                console.log('page not found' + e);
                redirect2();

            }
        });

        function redirect1() {

            console.log("in redirect");
            window.location.href = "../templates/configPlagiarismPercentage.html?userId="+user2;
        }

        function redirect2() {
            window.location = "../templates/userLogin.html";
        }
    });


});