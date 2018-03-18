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

    $("#btnChkPlg").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload/source",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            $("#btnChkPlg").prop("disabled", false);
        },
        error: function (e) {
            $("#resultSource").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnChkPlg").prop("disabled", false);
        }
    });

}

function fire_ajax_suspect_upload() {

    var form = $('#suspectForm')[0];

    var data = new FormData(form);

    $("#btnChkPlg").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload/suspect",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            $("#btnChkPlg").prop("disabled", false);
        },
        error: function (e) {
            $("#resultSuspect").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnChkPlg").prop("disabled", false);
        }
    });
}
