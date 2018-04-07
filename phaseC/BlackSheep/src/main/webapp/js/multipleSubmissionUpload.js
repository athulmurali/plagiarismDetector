// Info :
// THe following script is designed to send the data to upload Controller
// @RequestMapping(method = RequestMethod.POST, value = "/upload")
// public ResponseEntity<?> uploadFileSource(@RequestParam("userid") String userId,
// 			@RequestParam("project") String projectName,
// 			@RequestParam("files") MultipartFile[] files)

// Reference:
// https://github.ccs.neu.edu/cs5500/team-104/blob/multiple-submissions/phaseC/BlackSheep/src/main/java/com/blacksheep/controller/UploadController.java

// file addition is being added based on this :
// http://blog.teamtreehouse.com/uploading-files-ajax


//     // may be from a text box called project name
//     // || can be derived from the first part of file Name

var user    = "mike";
var project = "proj1";

window.onload =function () {
    console.log("JS loaded");

}


    function uploadMultipleFiles() {
    console.log("uploadMultipleFiles()");

    var fileSelect = document.getElementById('multipleFiles');
    var files = fileSelect.files;

    var data = new FormData();
    data.append("userid", user);
    data.append("project", project + makeid());

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
    console.log("data : multipleFiles to Post");

    console.log("printing form data : ");
    printFormData(data);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

    function uploadDirectory() {

        console.log("uploadDirectory()");

        var data = new FormData();

        data.append("userid",user);
        data.append("project",project+makeid());
        var fileSelect = document.getElementById('directoryFiles');
        var files = fileSelect.files;

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
        console.log(printFormData(data));


        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/upload",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
                console.log("SUCCESS : ", data);
            },
            error: function (e) {
                console.log("ERROR : ", e);
            }
        });
    }
//
    function redirectToCodeStats(){
        location.href = "../templates/codeStats.html";
    }

    function redirectToWelcome(){
        location.href = "../templates/welcome.html";
    }
//
// get random string for project Id
    function makeid() {
        var text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (var i = 0; i < 5; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));

        return text;
    }


    function printFormData(formData){
        // Display the key/value pairs
        for(var pair of formData.entries()) {
            console.log(pair[0]+ ', '+ pair[1]);
        }
    }