$(document).ready(function () {

    $("#login").click(function () {
        // var password = $("#password").val();n
        // var user = $("#uname").val();
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
                    console.log("Success");
                    redirect1();
            },
            error: function (e) {
                console.log('page not found' + e);
                redirect2();

            }
        });
    });

    function redirect1() {

        console.log("in redirect");
        window.location = "../templates/configPlagiarismPercentage.html";
    }

    function redirect2() {
        window.location = "../templates/userLogin.html";
    }
});