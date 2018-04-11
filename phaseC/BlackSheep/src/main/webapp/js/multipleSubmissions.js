const UPLOAD_URL = "/upload"
const USER_ID       = localStorage.getItem("user");


$(document).ready(function() {

    $("#configure").click(function(){redirectToConfigure()});

    $("#upload").click(function(){redirectToUpload()});

    $("#codeMatch").click(function(){redirectToCodeMatch()});

    $("#codeStats").click(function(){redirectToCodeStats()});

    $("#done").click(function(){redirectToCodeStats()});

    $("#logOut").click(function(){logOut()});





    /* Variables */
    var p = $("#students").val();
    var row = $(".studentRow");



    function addRow() {
        row.clone(true, true).appendTo("#studentTable");
    }


    /* Doc ready */
    $(".add").on("click", function() {
        getP();
        if ($("#studentTable tr").length < 17) {
            addRow();
            var i = Number(p) + 1;
            $("#students").val(i);
        }
        $(this)
            .closest("tr")
            .appendTo("#studentTable");
        if ($("#studentTable tr").length === 2) {
            $(".remove").hide();
        } else {
            $(".remove").show();
        }
    });
    $(".remove").on("click", function() {
        getP();
        if ($("#studentTable tr").length === 2) {
            $(".remove").hide();
        } else if ($("#studentTable tr").length - 1 == 2) {
            $(".remove").hide();
            removeRow($(this));
            var i = Number(p) - 1;
            $("#students").val(i);
        } else {
            removeRow($(this));
            var i = Number(p) - 1;
            $("#students").val(i);
        }
    });
    $("#students").change(function() {
        var i = 0;
        p = $("#students").val() - 1;
        var rowCount = $("#studentTable tr").length - 2;
        if (p > rowCount) {
            for (i = rowCount; i < p; i += 1) {
                addRow();
            }
            $("#studentTable #addButtonRow").appendTo("#studentTable");
        } else if (p < rowCount) {
        }
    });

    $(".upload").click(function() {
        var files = $(this).parent().parent().parent().find(".directoryUpload").prop("files");
        console.log(files);
        const studentName = $(this).parent().parent().parent().find(".student").prop("value");
        const projectName = $(this).parent().parent().parent().find(".project").prop("value");

        if (studentName == "")
        {
            alert("student name is empty!")
            return;
        }

        if (projectName == "")
        {
            alert("Project  name is empty!")
            return;
        }

        if (files.length ==0)
        {
            alert("student name is empty!")
            return;
        }



        uploadDirectory($(this), studentName, projectName,files);
    });

    function uploadDirectory(button, studentName,projectName,filesArray ) {

        console.log("uploadDirectory() called");
        // function to change the upload button status to uploading to be placed below

        var tempParent  =  button.parent()[0];
        tempParent.innerHTML= '<img src="http://www.bba-reman.com/images/fbloader.gif">';


        console.log("printing button");
        console.log(button);

        console.log("printing studentName");
        console.log(studentName);

        console.log("printing projectName");
        console.log(projectName);

        console.log("printing files Array");
        console.log(filesArray);

        console.log("uploadDirectory()");

        var data = new FormData();

        data.append("userid",USER_ID);

        // data.append("project",projectName+makeid());
        data.append("project",projectName);

        var fileSelect = document.getElementById('directoryFiles');
        var files = fileSelect.files;


        // name to output s



        // Loop through each of the selected files.
        for (var i = 0; i < files.length; i++) {
            var file = files[i];

            // Check the file type.
            // // if required - only python can allowed
            // if (!file.type.match('image.*')) {
            //     continue;
            // }

            console.log("fileName : " + file.webkitRelativePath);

            // Add the file to the request.
            data.append('files[]', file, file.name);
        }
        console.log("printing form data : ");
        // console.log(printFormData(data));


        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: UPLOAD_URL,
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {

                // on success:
                tempParent.innerHTML = '<img src="img/circleCheck.png" />';

                console.log("SUCCESS : ", data);
            },
            error: function (e) {
                console.log("ERROR : ", e);
                // on failure:
                // tempParent.innerHTML= '<img src="img/wrong.png" />'
            }
        });
    }




});
/* Functions */
function getP() {
    const p = $("#students").val();
    console.log("count : " + p);
}

function removeRow(button) {
    button.closest("tr").remove();
}