// update :

const DECIMAL_NUM_LIMIT =  2;
//  changed Comments match -> Comment Match (api produces different data now )

var getMetaDataURL = "/getResults3";
var ROLE = localStorage.getItem("role");

$(document).ready(function (){

    redirectIfNotLoggedIn();


    if (ROLE != "PROFESSOR")
    {
        console.log("No access to configure window for TA");
        document.getElementById("configure").style.display ="none";
        // hide config option
        // $('#configure').hide;
    }

    $("#configure").click(function(){redirectToConfigure()});

    $("#upload").click(function(){redirectToUpload()});

    $("#codeMatch").click(function(){redirectToCodeMatch()});

    $("#codeStats").click(function(){redirectToCodeStats()});

    $("#logOut").click(function(){logOut()});

    var userId = localStorage.getItem("user");


    var RESULT =    getMatches(userId);


    // the following will be combined with the json result of code
    // in local storage

    localStorage.setItem("metaData",JSON.stringify(RESULT)) // storing metadata in local storage
    var metaData = localStorage.getItem("metaData") // storing metadata in local storage
    metaData = JSON.parse(metaData);

    createMatchTable(metaData);

});

function getMatches(userId) {
    console.log("ajax request for get Meta Data sent");
    return newAjaxGet(getMetaDataURL,userId);
}


function newAjaxGet(urlTo,userId)
{
    var paramData = 'userid='+encodeURI(userId);
    console.log("param" +paramData);

    var resultTemp;
    var ajaxObx =  $.ajax({
        url: urlTo,
        data: paramData,
        type: "GET",
       async: false,

        success: function (response) {
            console.log(response);
            resultTemp = response;
            return response;
        },
        error: function (xhr, status, error) {
            alert(xhr.responseText);
        }
    });

   console.log("ajax res");
    return resultTemp;
}

/**
 * Adds a new row in the given table id
 * with 3 td tags.
 * 1. Project 1 name
 * 2. Project 2 Name
 * 3. Percentage
 * @param tableId
 * @param type
 * @param count
 */
function addPercentageRow(tableId,project1Name,project2Name,matchPercentage)
{
    var tableRef = document.getElementById(tableId).getElementsByTagName('tbody')[0];

// Insert a row in the table at row index 0
    var newRow   = tableRef.insertRow(tableRef.rows.length);

// Insert a cell in the row at index 0
    var newCell1  = newRow.insertCell(0);
    var newCell2  = newRow.insertCell(1);
    var newCell3  = newRow.insertCell(2);


// Append a text node to the cell
    var project1            = document.createTextNode(project1Name)
    var project2            = document.createTextNode(project2Name);
    var matchPercentage     = document.createTextNode(matchPercentage + "%");

    newCell1.appendChild(project1);
    newCell2.appendChild(project2);
    newCell3.appendChild(matchPercentage);
}

/**
 * Appends row to the existing table based on the results
 * @param filePairMatchArray
 */
function createMatchTable(filePairMatchArray)
{
    const sortedFilePairArray = sortMatchTable(filePairMatchArray);

    for (let i in sortedFilePairArray)
    {
        addPercentageRow(
            "fileTable",
            filePairMatchArray[i].file1,
            filePairMatchArray[i].file2,
            parseFloat(filePairMatchArray[i].percentage).toFixed(DECIMAL_NUM_LIMIT)
    );
    }
}
