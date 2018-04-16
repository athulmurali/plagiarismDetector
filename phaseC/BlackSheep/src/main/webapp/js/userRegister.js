var emailValidated = false;
function registerOnFormChange()
{
    const formValidated = ifFormValid();
    console.log(formValidated);

    if (formValidated){
        $("#register").prop("disabled",false);
    }
    else{
        $("#register").prop("disabled",true);
    }
    return;

}

$(document).ready(function () {

    $("#register").prop("disabled",true);

    $('input').change(registerOnFormChange).keyup(registerOnFormChange);

    $("#home").click(function(){redirectToWelcome();});

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

    $("#email").keyup(emailValidate).change(emailValidate);

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

    ifFormValid();

});

function ifFormValid() {
    return  emailValidated             &&
        isPasswordLengthValid()         &&
        isPasswordConfirmed();
}

function emailValidate() {
    var $emailAvailable = $("#emailAvailable");
    var email = $("#email").val();

    if (validateEmail(email))
    {
        emailValidated = true;
        console.log("existing email check");
        checkIfEmailNotTaken(email);
        registerOnFormChange()
        return emailValidated;
    }
    else {

        $emailAvailable.text("invalid");
        $emailAvailable.css("color", "red");
        registerOnFormChange()

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
    // return success if not taken (send request to server)
    $.ajax({
        url : "/isEmailTaken",
        data: {email : email},
        type : "GET",

        success : function(response) {
            console.log("email -available");
            emailValidated = true;
            $("#emailAvailable").text("");
            $("#emailAvailable").prepend('<img id="theImg" src="../templates/img/circleCheck.png" />');
        },
        error : function(xhr, status, error) {
            console.log("in error of email");
            $("#emailAvailable").text("email Taken");
            $("#emailAvailable").css("color", "red");
            emailValidated  = false;
        }

    });

};