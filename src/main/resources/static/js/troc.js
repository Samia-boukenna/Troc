jQuery(function ($) {

    $('#accepter-dmd').on('click', function (e) {
        console.log('hello')
        e.preventDefault();
        var msgId = $(this).parent().find('.msg').val();
        var data = {
            type: 'accepter',
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
                if(data.length && data[0] === 'accep') {
                    alert('demande traitée');
                    location.reload();
                }
            },
            error: function (data) {

            }
        });
    });

    $('#refuser-dmd').on('click', function (e) {
        e.preventDefault();
        var msgId = $(this).parent().find('.msg').val();
        var data = {
            type: 'refuser',
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
                if(data.length && data[0] === 'ref') {
                    alert('demande traitée');
                    location.reload();
                }
            },
            error: function (data) {

            }
        });
    });

});