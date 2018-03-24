$(document).ready(function () {

    $("#results").hide();

    $("#btnChkPlg").click(function (event) {
        event.preventDefault();

        var StructureChange = $('#StructureChange:checked').val();
        if(StructureChange==undefined)
            StructureChange=null;

        var CodeChange = $('#CodeChange:checked').val();
        if(CodeChange==undefined)
            CodeChange=null;

        var CommentChange = $('#CommentChange:checked').val();
        if(CommentChange==undefined)
            CommentChange=null;

        var type = {

            "c1":StructureChange,
            "c2":CodeChange,
            "c3":CommentChange
        }

        $.ajax({
            type: "POST",
            url: "/PostChoices",
            data: JSON.stringify(type),
            contentType: 'application/json',

            success: function (response) {
                console.log("Success");
                fire_ajax_source_upload();

            },
            error: function (e) {
                console.log('page not found' + e);

            }
        });
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
            fire_ajax_suspect_upload();
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

            $.ajax({
                type: "GET",
                enctype: 'multipart/form-data',
                url: "/getResults3",

                success: function (data1) {

                    $("#upload").hide();
                    $("#results").show();

                    //console.log(data1[0].file1);
                    $.each(data1, function (i, f) {

                        var mtchRow = " ";

                        $.each(f.matches, function (i, f2) {

                            mtchRow = "<table>" + "<td>"+ f2.type+ "</td>"
                                + "<td>"+ f2.file1MatchLines+ "</td>"
                                + "<td>"+ f2.file2MatchLines+ "</td>" + "</table>"

                        });

                        var tblRow = "<tr>" + "<td>" + f.file1 + "</td>" +
                            "<td>" + f.file2 + "</td>" + "<td>" +
                            mtchRow + "</td>" + "<td>" + f.percentage + "</td>" + "</tr>"


                        $(tblRow).appendTo("#userdata tbody");

                        // document.getElementById("tst").innerHTML = tblRow;
                        console.log(tblRow);
                    });

                },

                error: function (data1){
                    console.log("Error");

                }

            });

            console.log("SUCCESS : ", data);
            //window.location = "../templates/testResults.html";
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}
