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
            success: function () {
                redirect1();
                console.log("here");
            },
            error: function (e) {
                console.log('page not found' + e);
                redirect2();

            }
        });
    });

    function redirect1() {
        window.location = "../templates/uploadFiles.html";
        console.log("reDirected to a new window");
    }

    function redirect2() {
        window.location = "../templates/userLogin.html";
    }
});