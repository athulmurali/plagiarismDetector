
// functions for redirection
function redirectToConfigure()
{
    window.location = "../templates/configure.html"
}

function redirectToUpload()
{
    window.location = "../templates/upload.html";
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

function  redirectIfNotLoggedIn()
{
    var userId = localStorage.getItem("user");
    if (userId == null)
    {
        console.log("Please login and come back! ");
        alert("Not logged in ! Please login");
        redirectToLogin();

    }

}

function redirectTAToUpload(ROLE)
{

    if (ROLE != "PROFESSOR") {
        redirectToUpload();

    }

}


/**
 * Sorts the file Pair array given to be sorted.
 * The result will be reversed to desc order.
 * @param unsortedFilePairArray
 * @returns {Uint8Array | Uint16Array | Int16Array | Float32Array | Uint8ClampedArray | Int32Array | any}
 */
function sortMatchTable(unsortedFilePairArray){
    console.log("inside sortMatchTable():");
    //adding reverse, as the requirement demands s ort -descending
    return unsortedFilePairArray.sort(compare).reverse();
}

/**
 * The compare function to be given as input to the sorting function
 * @param filePair1 an obj containing 'percentage'
 * @param filePair2 an obj containing 'percentage'
 * @returns {number}
 */
function compare(filePair1,filePair2) {
    filePair1.percentage = parseFloat(filePair1.percentage);
    filePair2.percentage = parseFloat(filePair2.percentage);

    if (filePair1.percentage < filePair2.percentage)
        return -1;
    if (filePair1.percentage > filePair2.percentage)
        return 1;
}

