// Author : Athul
// Version : 3
// Date created : March 12, 2018
// Date edited :  March 22, 2018
// Please replace the ids if the ids are changed in html;
var back = "back"; // back button for match iteration
var next = "next"; // next button for match iteration
var leftStudentTableId  = "leftTable";
var rightStudentTableId = "rightTable";

var matchNumberStatus = "currentFileString";
var currentMatchIndex =0;
var filePairArray = [];

var TABLEHEADERLEFT = "leftFileName";   //  class name in HTML
var TABLEHEADERRIGHT = "rightFileName";// class  name in HTML


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
    "Comments Match"        : "commentsMatch"

};


var CodeMatchData = {
    metadata: "",
    codeData: ""
}



const RESULT = [
    {
        "file1": "simple1.py",
        "file2": "simple1.py",
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
        "file1": "simple1.py",
        "file2": "simple2.py",
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
        "file1": "simple1.py",
        "file2": "simple3.py",
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
        "file1": "simple2.py",
        "file2": "simple1.py",
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
        "file1": "simple2.py",
        "file2": "simple2.py",
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
        "file1": "simple2.py",
        "file2": "simple3.py",
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
window.onload= function()
{
    const matchMetedata = getMatches();
    console.log("printing metadata");
    console.log(matchMetedata);

    getCodeMatchData();


    // to be replaced with the ajax all instead of static values
    updateNavigation();
    updateBackButtonStatus();
    updateNextButtonStatus();
    updateMatchNumberStatus();
    console.log("Navigation update success");


    console.log("Metadata to process ;");
    console.log(CodeMatchData.metadata[0]);
    processMatch(CodeMatchData.metadata[0]);
    // FilePairMatchesTable(filePairArray);

}

function createTable(fileContent,tableId) {
    console.log("deleting existing rows");
    deleteTableRows(tableId);
    var arr = fileContent.split("\n");
    var table = document.getElementById(tableId).getElementsByTagName('tbody')[0];

    console.log("creating  new rows");

    for ( i =0; i < arr.length; i++)
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
    var newStatus = (currentMatchIndex + 1 ) + "/" +   ( getLastMatchIndex(CodeMatchData.metadata) +1);
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
        console.log("fileName does note exist in the content table");
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


    // .file to be replaced with function : fileNameToContent(filename);


    var file1Content = getFileContent(file1Name,CodeMatchData.codeData);
    console.log("file 1 Content  :");
    console.log(file1Content);

    var file2Content = getFileContent(file2Name,CodeMatchData.codeData);
    console.log("file 2 Content : " )
    console.log(file2Content);


    createTable(file1Content,leftStudentTableId);
    createTable(file2Content,rightStudentTableId);


    // modify table header  of left side table :

    modifyTableHeader(TABLEHEADERLEFT,file1Name);
    // modify table header  of right side table :
    modifyTableHeader(TABLEHEADERRIGHT,file2Name);

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

         lineNoToColor  = lineNoToColor;

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

    };

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

    const receivedFilePair = getMatches();

    filePairArray = removeEmptyMatchPair(receivedFilePair);
    // get metdata of match
    CodeMatchData.metadata =filePairArray;

    //get  code
    CodeMatchData.codeData = codeObj;

//
// // comment the above and uncomment the below to test for multiple matches
//     // filePairArray = removeEmptyMatchPair(RESULT);
//
//     filePairArray = removeEmptyMatchPair(RESULT)
//     // get metdata of match
//     CodeMatchData.metadata =filePairArray;
//
//     //get  code
//     CodeMatchData.codeData = codeObj;

}



/**
 * gets the matches using an ajax request
 * As of now only the metadata is received from the ajax response
 * once the endpoint is available for code , obj should be replaced with the corresponding data
 * @returns {*}
 */
function getMatches() {

    console.log("getMatches : ");
    const ajaxObj = getUrlJsonSync("/getResults3");
    console.log("/getResults3: received object");
    console.log(ajaxObj);
    console.log("Received data : Metadata for code match");
    console.log(ajaxObj.data);
    return ajaxObj.data;
}


/**
 * method containing ajax request
 * @param url
 * @returns {{valid: *|string, data: *}}
 */
function getUrlJsonSync(url){
    var jqxhr = $.ajax({
        type: "GET",
        url: url,
        dataType: 'json',
        cache: false,
        async: false
    });

    // 'async' has to be 'false' for this to work
    var response = {valid: jqxhr.statusText,  data: jqxhr.responseJSON};
    console.log(response);
    return response;
}



var codeObj = {};
codeObj["simple1.py"] = "def sum(a, b):\n" +
    "    mul(a,b,1)\n" +
    "    a+b\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c\n" +
    "\n" +
    "print(\"hello world\")";


codeObj["simple2.py"] = "def sum(a, b):\n" +
    "    mul(a,b,1)\n" +
    "    a+b\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c\n" +
    "\n" +
    "print(\"hello world\")";


codeObj["simple3.py"] = "print(\"hello world\")\n" +
    "\n" +
    "def sum(a, b):\n" +
    "    a+b\n" +
    "    mul(a,b,1)\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c";

codeObj["simple4.py"] = "# keep part of code compare to 1, 2, 3\n" +
    "\n" +
    "def sum(a, b):\n" +
    "    a+b\n" +
    "    mul(a,b,1)\n" +
    "\n" +
    "print(\"hello world\")\n";

codeObj["simple5.py"] = "# has part of code from simple 1, and part of extra code\n" +
    "\n" +
    "def mul(a,b,c):\n" +
    "    a*b*c\n" +
    "\n" +
    "print(\"The sum of %i and %i is %i\" % (5, 3, sum(5, 3)))\n";


codeObj["simple6.py"] = "# totally different from previous simples\n" +
    "temperature = 115\n" +
    "while temperature > 112:\n" +
    "    print(temperature)\n" +
    "    temperature = temperature - 1";



// later to use
//
// var filePathId = {};
//
// function addPathFileId(path){
//     var countMapsCreated =  Object.keys(filePathId).length;
//     filePathId[countMapsCreated] = path;
//     console.log(filePathId);
// }



// /**
//  * the following function  iterates over the array of file pair matches available
//  * @param filePairMatchArr
//  *
//  * Note : this require filePairMatch variable to be set in the current namespace
//  */
// function FilePairMatchesTable(filePairMatchArr){
//     console.log("Processing : multiple...filePairs ");
//     console.log("Iterating over each file pair match :");
//
//
//     for (i = 0; i < filePairMatchArr.length; i++) {
//         console.log(" Obj " + filePairMatchArr[i]);
//
//     }
//
// }
function redirectToCodeStats()
{
    window.location = "../templates/codeStats.html"
}