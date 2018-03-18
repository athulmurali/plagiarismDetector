$(document).ready(function () {

    $("#login").click(function () {
        // var password = $("#password").val();
        // var user = $("#uname").val();

        var cred = {

            "user":$("#uname").val(),
            "password": $("#password").val()
        }

        $.ajax({
            type: "POST",
            url: "/passcodeStringjson",
            data: JSON.stringify(cred),
            contentType: 'application/json',
            success: function () {

                console.log("Here");
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
        window.location = "../templates/uploadFiles.html";
    }

    function redirect2() {
        window.location = "../templates/userLogin.html";
    }
});