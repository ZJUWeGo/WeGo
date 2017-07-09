/**
 * Created by DivanoDestiny on 2017/7/9.
 */

$(document).on('click', '.login', function () {


    $(this).attr('disabled', 'true');

    var password = $('#password').val();
    var user = $('#user').val();



    if (user.length<1) {
        layer.msg('请输入有效的管理员帐号');
        $('.login').removeAttr('disabled');
        return;
    }

    if (password.length < 1) {
        layer.msg('请输入有效的密码');
        $('.login').removeAttr('disabled');
        return;
    }



    var data = {
        "id": user,
        "pwd": password
    };
    $.ajax({
        type: 'POST',
        url: '/manage-login',
        data: data,
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 0) {
                layer.msg("登录成功");
                $('.login').removeAttr('disabled');
                location.href = '/login'
            }
            else {

                $('.login').removeAttr('disabled');

                layer.msg('登录失败');

            }
        },
        error: function () {
            layer.msg('登录失败');
            $('.login').removeAttr('disabled');
        }


    })
});