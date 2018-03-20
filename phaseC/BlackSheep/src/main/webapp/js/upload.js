$(document).ready(function () {

    $("#btnChkPlg").click(function (event) {
        event.preventDefault();

        fire_ajax_source_upload();
        fire_ajax_suspect_upload();

    });

});

function fire_ajax_source_upload() {

    var form = $('#sourceForm')[0];
    var data = new FormData(form);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload/student1",
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

function fire_ajax_suspect_upload() {

    var form = $('#suspectForm')[0];
    var data = new FormData(form);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload/student2",
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
