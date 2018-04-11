
$(document).ready(function (){
    /* Variables */
    var p = $("#students").val();
    var row = $(".studentRow");

    /* Functions */
    function getP(){
        p = $("#students").val() ;
        console.log("count : " + p);
    }

    function addRow() {
        row.clone(true, true).appendTo("#studentTable");
    }

    function removeRow(button) {
        button.closest("tr").remove();
    }
    /* Doc ready */
    $(".add").on('click', function () {
        getP();
        if($("#studentTable tr").length < 17) {
            addRow();
            var i = Number(p)+1;
            $("#students").val(i);
        }
        $(this).closest("tr").appendTo("#studentTable");
        if ($("#studentTable tr").length === 2) {
            $(".remove").hide();
        } else {
            $(".remove").show();
        }
    });
    $(".remove").on('click', function () {
        getP();
        if($("#studentTable tr").length === 2) {
            $(".remove").hide();
        }
        else if($("#studentTable tr").length - 1 ==2)
        {
            $(".remove").hide();
            removeRow($(this));
            var i = Number(p)-1;
            $("#students").val(i);
        } else {
            removeRow($(this));
            var i = Number(p)-1;
            $("#students").val(i);
        }
    });
    $("#students").change(function () {
        var i = 0;
        p = $("#students").val();
        var rowCount = $("#studentTable tr").length - 2;
        if(p > rowCount) {
            for(i=rowCount; i<p; i+=1){
                addRow();
            }
            $("#studentTable #addButtonRow").appendTo("#studentTable");
        } else if(p < rowCount) {
        }
    });


    $(".upload").click(function () {
        console.log("hi");


        // on success:
        // $(this).parent()[0].innerHTML= '<img src="img/circleCheck.png" />'


        // on failure:
        // $(this).parent()[0].innerHTML= '<img src="img/circleWrong.png" />'

    });:
});
