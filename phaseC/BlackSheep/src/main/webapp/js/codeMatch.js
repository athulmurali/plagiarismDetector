// Author : Athul
// Version : 4
// Date created : March 12, 2018
// Date edited :  March 22, 2018

const metaDataURL = "/getResults3";
const userId      =  localStorage.getItem("user");
const ROLE       = localStorage.getItem("role");


const SIMILARITY_ID = "similarity";
var back = "back"; // back button for match iteration
var next = "next"; // next button for match iteration
var leftStudentTableId  = "leftTable";
var rightStudentTableId = "rightTable";

var matchNumberStatus = "currentFileString";
var currentMatchIndex =0;
var filePairArray = [];

var TABLEHEADERLEFT = "leftFileName";   //  class name in HTML
var TABLEHEADERRIGHT = "rightFileName";// class  name in HTML


// Other Constants:

const DECIMAL_NUM_LIMIT =  2;

// types
// 1. exactMatch
// 2. movedCode
// 3. renamedVariables
// 4. similarComments

var plagiarismCSSClasses = {
    "exactMatch"            : "exactMatch",
    "movedCode"             : "movedMatch",
    "renamedVariable"       : "renamedMatch",
    "commentsMatch"         : "commentsMatch",


    "CRC Match"             : "exactMatch",
    "CodeMovement Match"    : "movedMatch",
    "Structure Match"       : "renamedMatch",
    "Comments Match"        : "commentsMatch",


    //Added after backend data format change


    "Comment Match"        : "commentsMatch"

};

//variable that stores metadata and Code

var CodeMatchData = {
    metadata: "",
    codeData: ""
}

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


    console.log("Logged in as :"+ userId);
    var CodeMatchData;

    CodeMatchData = getCodeMatchData();

    // uncomment the below line and comment the above line to test the UI of codeMatch
    // CodeMatchData = getTestCodeMatchData();

    console.log("Code match data : Metadata & Code Data(below)");
    console.log(CodeMatchData);


    // to be replaced with the ajax all instead of static values
    updateNavigation();
    updateBackButtonStatus();
    updateNextButtonStatus();
    updateMatchNumberStatus();
    console.log("Navigation update success");

    processMatch(CodeMatchData.metadata[0]);
    // FilePairMatchesTable(filePairArray);

});

function createTable(fileContent,tableId) {
    console.log("deleting existing rows");
    deleteTableRows(tableId);
    var arr = fileContent.split("\n");
    var table = document.getElementById(tableId).getElementsByTagName('tbody')[0];

    console.log("creating  new rows");

    for ( let i =0; i < arr.length; i++)
    {
        var row = table.insertRow(i); // +1 to preserve the header
        var cell0 = row.insertCell(0);
        var cell1 = row.insertCell(1);
        console.log(i);
        cell0.innerHTML = String(i);
        cell1.innerHTML = arr[i];
    }
}

/**
 * nextMatch - moves the currentMatchIndex by 1
 * if the end is reached, the button must be disabled
 */
function nextMatch(){
    console.log("next button pressed");
    incrementMatchIndexIfPossible();
    // build Next table of matches :
    updateNavigation();
}

function prevMatch(){
    console.log("previous button pressed");
    decrementMatchIndexIfPossible();
    updateNavigation();
}

function getLastMatchIndex() {
    return filePairArray.length - 1;
}

function getFirstMatchIndex() {
    return 0;
}

function incrementMatchIndexIfPossible(){
    if (currentMatchIndex >= getLastMatchIndex())
        console.log("cannot increment. Last page reached");
    else{
        currentMatchIndex += 1;
        console.log(" Iterate next")
        processMatch(CodeMatchData.metadata[currentMatchIndex]);
    }
    return;
}

function decrementMatchIndexIfPossible(){
    if (currentMatchIndex == getFirstMatchIndex())
        console.log("cannot increment. First page reached");
    else
    {
        currentMatchIndex -= 1;
        processMatch(CodeMatchData.metadata[currentMatchIndex]);
    }
    return;
}

function updateNavigation() {
    updateNextButtonStatus();
    updateBackButtonStatus();
    updateMatchNumberStatus();
}

function updateNextButtonStatus(){

    if (currentMatchIndex == getLastMatchIndex()){
        document.getElementById(next).disabled = true;
        console.log("disabled");
    }

    else{
        document.getElementById(next).disabled = false;
        console.log("moved To Next Page");
    }
}

function updateBackButtonStatus(){
    if (currentMatchIndex == getFirstMatchIndex())
    {
        document.getElementById(back).disabled = true;
        console.log("first page reached.. back disabled ! ");
    }
    else
    {
        document.getElementById(back).disabled = false;
        console.log("back button Enabled ");
    }
}

function updateMatchNumberStatus(){
    var newStatus = (currentMatchIndex + 1 ) + "/" +   ( getLastMatchIndex() +1);
    console.log("new status : " +  newStatus);
    document.getElementById(matchNumberStatus).innerHTML = newStatus;

}


function deleteTableRows(tableId)
{
    console.log("deleting : "+ tableId);

    var tableHeaderRowCount = 1;
    var table = document.getElementById(tableId);
    var rowCount = table.rows.length;
    for (var i = tableHeaderRowCount; i < rowCount; i++)
    {
        table.deleteRow(tableHeaderRowCount);
    }
}

function getFileContent(fileName,fileContentTable) {

    if (fileName in fileContentTable)
        return fileContentTable[fileName];
    else{
        console.log("Code not found in code dict for  : " + fileName);
        return null;
    }

}

function removeEmptyMatchPair(filePairMatchArray)
{
    var tempMatchArray =[];
    for (i in filePairMatchArray)
    {
        if (! filePairMatchArray[i].matches.length == 0)
        {
            tempMatchArray.push(filePairMatchArray[i]);
            console.log("non empty file pair match");
        }
        else console.log("Empty match received");

    }
    return tempMatchArray;
}

function processMatch(objMatch)
{

    if (! objMatch.matches.length)
    {
        console.log("matches empty");
        return;

    }

    var file1Name = objMatch.file1;
    console.log("file 1 Name : " + file1Name );
    var file2Name = objMatch.file2;
    console.log("file 2 Name : " + file2Name );

    // modify table header  of left side table :

    modifyTableHeader(TABLEHEADERLEFT,file1Name);
    // modify table header  of right side table :
    modifyTableHeader(TABLEHEADERRIGHT,file2Name);

    console.log("updating similarity");
    updateSimilarity(SIMILARITY_ID,objMatch.percentage);

    // .file to be replaced with function : fileNameToContent(filename);
    var file1Content = getFileContent(file1Name,CodeMatchData.codeData);
    console.log("file 1 Content  :");
    console.log(file1Content);

    var file2Content = getFileContent(file2Name,CodeMatchData.codeData);
    console.log("file 2 Content : " )
    console.log(file2Content);

    // if the file content is not found it is returned
    if (file1Content == null || file2Content == null) return;


    createTable(file1Content,leftStudentTableId);
    createTable(file2Content,rightStudentTableId);




    for( var match in objMatch.matches)
    {
        console.log(match);
        var matchType = objMatch.matches[match].type;
        console.log(matchType);

        var file1lineNoArray =  objMatch.matches[match].file1MatchLines;
        var file2lineNoArray =  objMatch.matches[match].file2MatchLines;


        //  modify border of lineNumbers-table1
        modifyTableBorderOnType(leftStudentTableId, file1lineNoArray,matchType);

        //  modify border of lineNumbers - table2
        modifyTableBorderOnType(rightStudentTableId, file2lineNoArray,matchType);

    }

}


/**
 *
 * @param tableId represents the html tableId of the table to be modified
 * @param startLineNo represents the start Line number of the match type
 * @param endLineNo represents the end Line number of the match type.
 * @param matchType represents the type of match.
 *
 * Note:  this function does not check for invalid line numbers.
 * Line numbers are indexed from 0 to n.
 * the function callee is expected to give a valid line number.
 */
function modifyTableBorderOnType(tableId, lineNoArray, matchType)
{
    console.log("inside: modifyTable(*)");
    console.log("plagiarism css class id : " + plagiarismCSSClasses[matchType]);

    var rows = document.getElementById(tableId).rows;
    for( i in lineNoArray){
        console.log("changing type :  " + matchType + " line no : " + lineNoArray[i]);

        // line number to change the border color (without + 1 for table header)
        var lineNoToColor = lineNoArray[i];

        //header is the first Row no
        //hence i +1 in row number for contents


        //number of rows present in the table
        const rowCount = document.getElementById(tableId).rows.length;
        console.log("rows count : "     + rowCount);
        console.log("row to color  : "  +  lineNoToColor);

        if (lineNoToColor >= rowCount)
        {

            console.log("Cannot border a row not present in table ");
            continue;
        }
        var tr = rows.item(lineNoToColor);
        console.log("row before modification : ");
        console.log(document.getElementById(tableId).rows.item(lineNoToColor).innerHTML);
        tr.setAttribute("id", plagiarismCSSClasses[matchType]);
        console.log("row after modification : ");
        console.log(document.getElementById(tableId).rows.item(lineNoToColor).innerHTML);

    }

}

function modifyTableHeader(headerID, fileName ){
    var currentText = document.getElementsByClassName(headerID).item(0).innerHTML;
    console.log("header name - html  inner text : " + currentText);
    document.getElementsByClassName(headerID).item(0).innerHTML = fileName;
    console.log("header changed  name - html  inner text:  ");
    console.log( document.getElementsByClassName(headerID).item(0).innerHTML);
}

// function to get the match metadata and code
function getCodeMatchData()
{

    // get metdata of match
    const receivedFilePair = newAjaxGet(metaDataURL,userId);
    filePairArray          = removeEmptyMatchPair(receivedFilePair);
    CodeMatchData.metadata =filePairArray;

    //get  code
    // processing the input to fit ui input requirements
    const codeArray = newAjaxGet("/getCode",userId);
    const codeDictObj = codeArrayToCodeDict(codeArray);

    console.log("obtained code :")
    CodeMatchData.codeData = codeDictObj;
    return CodeMatchData;

}

/**
 * Purpose : Converts the code obtained from the api for getting code to a dict
 * of file names with code  to dict with string filename as key and code as value
 *
 * example :
 * input :
 * [
 *  0 : {fileName: "proj16zMJE", code: "↵ # @@TOPAATM },
 *  1 : {fileName: "proj16zMJE", code: "↵ # @@TOPAATM }
 * ]
 */
function codeArrayToCodeDict( codeArray)
{
    // the output dict
    var dict = {}; // create an empty array

    for ( let i =0; i < codeArray.length; i++)
    {
        // file Name is the name of the param
        const fileName = codeArray[i]["fileName"];
        const code = codeArray[i]["code"];

        console.log("iter       : " + i);
        console.log("fileName   : " + fileName);
        console.log("code       : " + code);

        dict[fileName]=code;
    }

    console.log("output dict below this ");
    console.log(dict);

    return dict
}

function newAjaxGet(urlTo,userId)
{
    var paramData = 'userid='+encodeURI(userId);

    var resultTemp = null;
    $.ajax({
        url: urlTo,
        data: paramData,
        type: "GET",
        async: false,

        success: function (response) {
            resultTemp = response;
            return response;
        },
        error: function (xhr, status, error) {
            alert(xhr.responseText);
        }
    });

    return resultTemp;
}

function updateSimilarity(similaritySpanId, percentage){
    console.log("updating similarity - in function");
    percentage = parseFloat(percentage).toFixed(DECIMAL_NUM_LIMIT);

    document.getElementById(similaritySpanId).innerHTML=percentage;
}


// test data for Code match | use the following for testing

var testCodeDict = {};
testCodeDict["simple1ProjectName"] = "def sum(a, b):\n" +
    "    mul(a,b,1)\n" +
    "    a+b\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c\n" +
    "\n" +
    "print(\"hello world\")";


testCodeDict["simple2ProjectName"] = "def sum(a, b):\n" +
    "    mul(a,b,1)\n" +
    "    a+b\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c\n" +
    "\n" +
    "print(\"hello world\")";


testCodeDict["simple3ProjectName"] = "print(\"hello world\")\n" +
    "\n" +
    "def sum(a, b):\n" +
    "    a+b\n" +
    "    mul(a,b,1)\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c";

testCodeDict["simple4ProjectName"] = "# keep part of code compare to 1, 2, 3\n" +
    "\n" +
    "def sum(a, b):\n" +
    "    a+b\n" +
    "    mul(a,b,1)\n" +
    "\n" +
    "print(\"hello world\")\n";

testCodeDict["simple5ProjectName"] = "# has part of code from simple 1, and part of extra code\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c\n" +
    "\n" +
    "print(\"The sum of %i and %i is %i\" % (5, 3, sum(5, 3)))\n";


testCodeDict["simple6ProjectName"] = "# totally different from previous simples\n" +
    "temperature = 115\n" +
    "while temperature > 112:\n" +
    "    print(temperature)\netC" +
    "    temperature = temperature - 1";



// function to  test the code  match metadata and code
function getTestCodeMatchData()
{

    // const receivedFilePair = newAjaxGet(metaDataURL,userId);

    filePairArray = removeEmptyMatchPair(testMetaData);
    // get metdata of match
    CodeMatchData.metadata =testMetaData;

    //get  code
    // const codeDictObj =  newAjaxGet("/getCode",userId);
    console.log("obtained code :")
    // console.log(codeDictObj);

    CodeMatchData.codeData = testCodeDict;

    return CodeMatchData;

}



const testMetaData = [
    {
        "file1": "simple1ProjectName",
        "file2": "simple1ProjectName",
        "percentage": 100,
        "matches": [
            {
                "type": "CRC Match",
                "file1MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ],
                "file2MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ]
            }
        ]
    },
    {
        "file1": "simple1ProjectName",
        "file2": "simple2ProjectName",
        "percentage": 100,
        "matches": [
            {
                "type": "CRC Match",
                "file1MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ],
                "file2MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ]
            }
        ]
    },
    {
        "file1": "simple1ProjectName",
        "file2": "simple3ProjectName",
        "percentage": 60,
        "matches": [
            {
                "type": "CodeMovement Match",
                "file1MatchLines": [
                    1,
                    2,
                    3,
                    5,
                    6,
                    8
                ],
                "file2MatchLines": [
                    1,
                    3,
                    4,
                    5,
                    7,
                    8
                ]
            },
            {
                "type": "Structure Match",
                "file1MatchLines": [
                    1,
                    3
                ],
                "file2MatchLines": [
                    1,
                    5
                ]
            },
            {
                "type": "Comments Match",
                "file1MatchLines": [],
                "file2MatchLines": []
            }
        ]
    },
    {
        "file1": "simple2ProjectName",
        "file2": "simple1ProjectName",
        "percentage": 100,
        "matches": [
            {
                "type": "CRC Match",
                "file1MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ],
                "file2MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ]
            }
        ]
    },
    {
        "file1": "simple2ProjectName",
        "file2": "simple2ProjectName",
        "percentage": 100,
        "matches": [
            {
                "type": "CRC Match",
                "file1MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ],
                "file2MatchLines": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8
                ]
            }
        ]
    },
    {
        "file1": "simple2ProjectName",
        "file2": "simple3ProjectName",
        "percentage": 60,
        "matches": [
            {
                "type": "CodeMovement Match",
                "file1MatchLines": [
                    1,
                    2,
                    3,
                    5,
                    6,
                    8
                ],
                "file2MatchLines": [
                    1,
                    3,
                    4,
                    5,
                    7,
                    8
                ]
            },
            {
                "type": "Structure Match",
                "file1MatchLines": [
                    1,
                    3
                ],
                "file2MatchLines": [
                    1,
                    5
                ]
            },
            {
                "type": "Comments Match",
                "file1MatchLines": [],
                "file2MatchLines": []
            }
        ]
    }
]; // use this to test