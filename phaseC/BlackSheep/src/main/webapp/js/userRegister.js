$(document).ready(function () {
    // checks bootstrap import
    var bootstrap_enabled = (typeof $().emulateTransitionEnd == 'function');
    console.log("bootstrap loaded : " + bootstrap_enabled);

    $('#password_confirm,#password').keyup(
        function () {
        if (isPasswordConfirmed())
        {
            $('#message').html('<img id="passwordStatus" src="../templates/img/circleCheck.png" />')

        } else
            $('#message').html('<img id="passwordStatus" src="../templates/img/wrong.png" />')
        }
    );

    $("#email").keyup(emailValidate);

    $('#password').keyup(isPasswordLengthValid);

    // if password is valid and email is valid allow
    // else disable the register button

    $("#register").click(function () {

        var userDetails = {
            "userId"     :    $("#email").val(),
            "password"  :    $("#password").val(),
            "role"      :    $('input[name=role]:checked').val()
        }

        $.ajax({
            type: "POST",
            url: "/userRegister",
            data: JSON.stringify(userDetails),
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
});

function ifFormValid() {
    return  emailValidate()             &&
        isPasswordLengthValid()     &&
        isPasswordConfirmed();
}

function emailValidate() {
    var $emailAvailable = $("#emailAvailable");
    var email = $("#email").val();
    $emailAvailable.text("");

    if (validateEmail(email))
    {
        $emailAvailable.prepend('<img id="theImg" src="../templates/img/circleCheck.png" />')
        return true;
    }
    else {
        $emailAvailable.text("invalid");
        $emailAvailable.css("color", "white");
        return false;
    }
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

function isPasswordConfirmed(){

    return ($('#password').val() == $('#password_confirm').val() &&
        $('#password').val()!= ''); }

function isPasswordLengthValid()
{
    if ($('#password').val().length < 4)
    {
        $('#passwordHelp').css("display", "block");
        return false;
    }

    else
    {
        $('#passwordHelp').css("display", "none");
        return true;
    }
}

function redirect1() {

    console.log("in redirect");
    window.location = "../templates/userLogin.html";
}

function redirect2() {
    window.location = "../templates/userRegister.html";
}

function checkIfEmailNotTaken(email){
    // return false if not taken (send request to server)
    // $.ajax({
    //     url: '/emailAvaliable',
    //     data: {email : email},
    //     success: function(data) {
    //         //Do Something
    //         if (data.available == false)
    //         {
    //             return false;
    //         }
    //     }
    // });
    return true;
};
