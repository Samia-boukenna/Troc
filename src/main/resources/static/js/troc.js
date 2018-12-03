jQuery(function ($) {

    $('#accepter-dmd').on('click', function (e) {
        e.preventDefault();
        var ficId = $('#msg-id').html();
        var nmIE=$("#nm-ie").html();
        var nmIR =$("#nm-ir").html();
        var mailDest=$("#mail-dest").html();
        var mailExp=$("#mail-exp").html();
        var msgId =$("#msg-id").html();
        var data = {
            msgID: ficId,
            type: 'accepter',
            nmIE: nmIE,
            nmIR: nmIR,
            mailDest: mailDest,
            mailExp: mailExp,
            msgId: msgId
        };
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'post',
            url: '/traiter-dmd',
            dataType: 'json',
            data: JSON.stringify(data),
            cache: false,
            success: function (data) {
                alert('data sent');
                alert(data);
            },
            error: function (data) {

            }
        });
    });

    $('#refuser-dmd').on('click', function (e) {
        e.preventDefault();
        var msgId = $('#msg-id').html();
        var ficId = $('#msg-id').html();
        var nmIE=$("#nm-ie").html();
        var nmIR =$("#nm-ir").html();
        var mailDest=$("#mail-dest").html();
        var mailExp=$("#mail-exp").html();
        var msgId =$("#msg-id").html();
        var data = {
            msgID: ficId,
            type: 'refuser',
            nmIE: nmIE,
            nmIR: nmIR,
            mailDest: mailDest,
            mailExp: mailExp,
            msgId: msgId
        };
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'post',
            url: '/traiter-dmd',
            dataType: 'json',
            data: JSON.stringify(data),
            cache: false,
            success: function (data) {
                alert('data sent');
                alert(data);
            },
            error: function (data) {

            }
        });
    });

});