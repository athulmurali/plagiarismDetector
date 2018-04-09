// update :

//  changed Comments match -> Comment Match (api produces different data now )

var getMetaDataURL = "/getResults3";



window.onload= function() {


    //redirects  to code stats on click of div id codeStats
    $("#codeMatch").click(function(){redirectToCodeMatch()});

    $("#upload").click(function(){redirectToUpload()});

    $("#configure").click(function(){redirectToConfig()});



    var userId = localStorage.getItem("user");


    var RESULT =    getMatches(userId);

    localStorage.setItem("metData",RESULT) // storing metadata in local storage

    console.log(getFileDict(RESULT));

    var perList = getPercentageArray(RESULT);
    var avg = avgOfList(perList);
    console.log("avg" + avg);
    changeOverallPercentage(avg.toFixed(2));


   var typeCountDict = getTypeCount(RESULT);

   //building table 1
    var keys = Object.keys(typeCountDict);
    for(i in keys){
        addRow('myTable',keys[i],typeCountDict[keys[i]]);
        console.log(keys[i] + typeCountDict[keys[i]]);
    }


    // getMatches();s
    var fileDict = getFileDict(RESULT);

    //building table 2
    var keys = Object.keys(fileDict);
    for(i in keys){
        addRow('fileTable',keys[i],fileDict[keys[i]]);
        console.log(keys[i] + typeCountDict[keys[i]]);
    }
    var obj =getMatches();

}


function addRow(tableId,type,count)
{
    var tableRef = document.getElementById(tableId).getElementsByTagName('tbody')[0];

// Insert a row in the table at row index 0
    var newRow   = tableRef.insertRow(tableRef.rows.length);

// Insert a cell in the row at index 0
    var newCell1  = newRow.insertCell(0);
    var newCell2  = newRow.insertCell(1);


// Append a text node to the cell
    var newText  = document.createTextNode(type)
    var newText1  = document.createTextNode(count)

    newCell1.appendChild(newText);
    newCell2.appendChild(newText1);

}

function changeOverallPercentage(percentage){
    console.log(document.getElementById("percent").innerText);
    document.getElementById("percent").innerText = percentage;

}

function getPercentageArray(matchPairArray){

    console.log("getPercentageArray");
    var newObj = {};
    newObj["Structure Match"] = 0;
    newObj["Comment Match"] = 0; //  changed Comments match -> Comment Match
    newObj["CodeMovement Match"] = 0;
    newObj["CRC Match"] = 0;

    var percentArray =[];

    for(i in matchPairArray)
    {
        var currentPercentage = matchPairArray[i].percentage;
        console.log(currentPercentage);
        percentArray.push(currentPercentage);
        // matches =  matchPairArray[i].matches;
        // for(j in matches){
        //     console.log(matches[j]);
        //     percentArray
        // }
    }

    return percentArray;

}

function avgOfList(avgList)
{
    var total = 0;
    for(var i = 0; i < avgList.length; i++) {
        total += avgList[i];
    }
    return total / avgList.length;
}

function getTypeCount(matchPairArray){

    console.log("getTypeCount");
    var newObj = {};
    newObj["Structure Match"] = 0;
    newObj["Comment Match"] = 0;
    newObj["CodeMovement Match"] = 0;
    newObj["CRC Match"] = 0;

    var percentArray =[];

    for(i in matchPairArray)
    {

        matches =  matchPairArray[i].matches;

        if (matches == null )
            continue;
        for(j in matches){
            newObj[matches[j].type] += 1;
            console.log(matches[j].type);
            console.log(matches[j].type + ": " + newObj[matches[j].type]);
        }
    }
    return newObj;
}

function getFileDict(matchPairArray){

    var dict = {};

    for ( i in matchPairArray)
    {
         var file1 =  matchPairArray[i].file1;
         var file2 =  matchPairArray[i].file2;




        console.log("filePair");
        for (j in matchPairArray[i].matches)
        {
            console.log(j);

            var match1Lines = matchPairArray[i].matches[j]["file1MatchLines"];
            var match2Lines = matchPairArray[i].matches[j].file2MatchLines;
            if (dict.hasOwnProperty(file1)) {
                console.log('item 1has ' + file1);
                dict[file1] = addUniqueElements(match1Lines, dict[file1]);
            }

            else {
                dict[file1] =  addUniqueElements(match1Lines,[])
                console.log('item1 nope' + file1);
                console.log( dict[file1]);

            }

            console.log( matchPairArray[i]);

            if (dict.hasOwnProperty(file2)) {
                console.log('item2 has ' + file2);

                dict[file2] = addUniqueElements(match2Lines,dict[file2])
            }

            else {
                console.log('item 2 nope ' + file2);
                dict[file2] = addUniqueElements(match2Lines,[]);
            }

        }

    }

    console.log("currentUser : ", userId);
    console.log("global code");
    newAjaxGet("/getCode","mike");



    console.log(dict);
     return dict;


};

function addUniqueElements(newLi,oldLi){
    for (i in newLi)
    {
        var newNum = parseInt(newLi[i]);


        if (oldLi.indexOf(newNum) < 0)
        {
            oldLi.push(newNum);
        };
    }
    return oldLi;
}

function getMatches(userId) {
    const RESULT1 =     newAjaxGet(getMetaDataURL,userId);

    console.log(" Result below  /....   ");

    console.log(RESULT1);

    return RESULT1;
}


function getFileDict(matchPairArray)
{
    var fileDict = {};

    for (i in matchPairArray){
        console.log("matchPair");
        console.log(matchPairArray[i]);
        var currentMatchPair =  matchPairArray[i];


        if (true || matchPairArray.hasOwnProperty("file1") && matchPairArray.hasOwnProperty("file2"))
        {
            console.log("hii");


            var file1 = currentMatchPair["file1"]
            var file2 = currentMatchPair["file2"];

            if (fileDict.hasOwnProperty(file1))
            {
                fileDict[file1] += 1 ;
            }
            else (fileDict[file1] = 1);


            if (fileDict.hasOwnProperty(file2))
            {
                fileDict[file2] += 1 ;

            }
            else fileDict[file2] = 1;

        }

    }
    console.log(fileDict);
    return fileDict;
}

function redirectToCodeMatch()
{

    console.log(" logged redirection ");
    window.location = "../templates/codeMatch.html"
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




// functions for redirection
function redirectToUpload()
{
    window.location = "../templates/upload.html"
}

function redirectToConfig()
{
    window.location = "../templates/configPlagiarismPercentage.html"
}




