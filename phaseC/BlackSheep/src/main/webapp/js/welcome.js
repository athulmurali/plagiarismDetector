$(document).ready(function () {

    var count =-1;
    $.ajax({
        type: "POST",
        url: "/usageStats",
        // data: JSON.stringify(cred),
        contentType: 'application/json',

        success: function (response) {
            console.log("Success");
            count = response;
            document.getElementById("count").innerHTML=count;
            document.getElementById("statusText").innerText="Active";

        },
        error: function (e) {
            console.log('page not found' + e);
            redirect2();
        }
    });


});



// new code
function redirectToLogin()
{
    window.location = "../templates/userLogin.html";
}


function redirectToUserRegister()
{
    window.location = "../templates/userRegister.html";
}