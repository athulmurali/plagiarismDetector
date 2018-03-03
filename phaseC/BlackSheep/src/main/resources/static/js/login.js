$(document).ready(function () {

    $("#login").click(function () {
        var password = $("#password").val();

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/passcodeString/",
            data: password,
            contentType: "text/plain",

            success: function () {
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
        window.location = "../templates/upload.html";
    }

    function redirect2() {
        window.location = "../templates/Error.html";
    }
}