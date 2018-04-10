
// functions for redirection
function redirectToConfigure()
{
    window.location = "../templates/configure.html"
}

function redirectToUpload()
{
    window.location = "../templates/upload.html"
}

function redirectToCodeStats()
{
    window.location = "../templates/codeStats.html"
}


function redirectToCodeMatch()
{

    console.log(" logged redirection ");
    window.location = "../templates/codeMatch.html"
}


function redirectToWelcome(){
        window.location = "../templates/welcome.html"
}

function redirectToLogin(){
    window.location = "../templates/userLogin.html"
}


function redirectToRegister(){
    window.location = "../templates/userRegister.html"
}

function logOut(){
    localStorage.clear();
    window.location = "../templates/welcome.html"
}