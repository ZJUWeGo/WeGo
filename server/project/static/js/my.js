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
                location.href = '/back?page=0'
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
$(function () { $("[data-toggle='tooltip']").tooltip(); });

$(document).on('click', '.add', function () {


    $(this).attr('disabled', 'true');
    var btn = $(this);

    var id = btn.siblings('input').attr('id');
    var num = btn.siblings('input').val();
    if (parseInt(num)<0){
        layer.msg('请输入有效数字');
        return;
    }
    var data = {
        "method":'add-item-num',
        "id":id,
        "num":num

    };
    console.log(data);

    $.ajax({
        type: 'POST',
        url: '/back',
        data: data,
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 0) {
                layer.msg("修改成功");
                target = btn.parent('.input-group').parent('.op').siblings('.num');
                console.log(target);
                target.html(parseInt(target.html())+parseInt(num));
                btn.siblings('input').val('');

                // target.text(parseInt(target.innerHTML)+parseInt(num))
                btn.removeAttr('disabled');

            }
            else {

                btn.removeAttr('disabled');

                layer.msg('修改失败');

            }
        },
        error: function () {
            layer.msg('请求失败');
            btn.removeAttr('disabled');
        }


    })
});

$(document).on('click', '.change', function () {


    $(this).attr('disabled', 'true');
    var btn = $(this);

    var id = btn.siblings('input').attr('id');
    var num = btn.siblings('input').val();
    if (parseFloat(num)<0){
        layer.msg('请输入有效数字');
        return;
    }
    var data = {
        "method":'change-item-price',
        "id":id,
        "price":num

    };
    console.log(data);

    $.ajax({
        type: 'POST',
        url: '/back',
        data: data,
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 0) {
                layer.msg("修改成功");
                target = btn.parent('.input-group').parent('.op').siblings('.price');
                console.log(target);
                target.html(parseFloat(num));
                btn.siblings('input').val('');
                // target.text(parseInt(target.innerHTML)+parseInt(num))
                btn.removeAttr('disabled');

            }
            else {

                btn.removeAttr('disabled');

                layer.msg('修改失败');

            }
        },
        error: function () {
            layer.msg('请求失败');
            btn.removeAttr('disabled');
        }


    })
});


$(document).on('click', '.addNewItem', function () {


    $(this).attr('disabled', 'true');
    var btn = $(this);

    var id = $('#newItemId').val();
    var name = $('#newItemName').val();
    var num = $('#newItemNum').val();
    var price = $('#newItemPrice').val();
    if (parseInt(num)<0){
        layer.msg('请输入有效数量');
        return;
    }
    if (parseFloat(price)<0.0){
        layer.msg('请输入有效价格')
    }
    var data = {
        "method":'add-new-item',
        "id":id,
        "num":num,
        "name":name,
        "price":price

    };
    console.log(data);

    $.ajax({
        type: 'POST',
        url: '/back',
        data: data,
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 0) {
                layer.msg("新增成功");
                // target = btn.parent('.input-group').parent('.op').siblings('.num');
                target = btn.parent('.op').parent('tr');

                newRow = `
                            <tr>
                                <th scope="row" class="col-lg-2 id">${id}</th>
                                <td class="col-lg-3 name">${name}</td>
                                <td class="col-lg-2 price">${parseFloat(price).toFixed(2)}</td>
                                <td class="col-lg-2 num">${parseInt(num)}</td>
                                <td class="col-lg-3 op">
                                    <div class="input-group col-sm-12">
                                        <span class="input-group-addon add"><i
                                                class="glyphicon glyphicon-plus" data-toggle="tooltip" title="商品入库"></i></span>
                                        <span class="input-group-addon change" ><i
                                                class="glyphicon glyphicon-edit" data-toggle="tooltip" title="修改价格"></i></span>
                                        <span class="input-group-addon delete"><i
                                                class="glyphicon glyphicon-remove" data-toggle="tooltip"  title="删除商品"></i></span>
                                        <input id="${id}" type="text" class="form-control"
                                               placeholder="参数"></div>


                                </td>
                            </tr>
                `;
                $('#newItemId').val('');
                $('#newItemName').val('');
                $('#newItemNum').val('');
                $('#newItemPrice').val('');

                console.log(target);
                target.before(newRow);
                // target.text(parseInt(target.innerHTML)+parseInt(num))
                btn.removeAttr('disabled');

            }
            else {

                btn.removeAttr('disabled');

                layer.msg('新增失败');

            }
        },
        error: function () {
            layer.msg('请求失败');
            btn.removeAttr('disabled');
        }


    })
});

$(document).on('click', '.delete', function () {


    $(this).attr('disabled', 'true');
    var btn = $(this);

    var id = btn.siblings('input').attr('id');

    var data = {
        "method":'clear-item-num',
        "id":id,
    };
    console.log(data);

    $.ajax({
        type: 'POST',
        url: '/back',
        data: data,
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 0) {

                target = btn.parent('.input-group').parent('.op').siblings('.num');
                console.log(target);
                target.html(0);
                btn.siblings('input').val('');
                // target.text(parseInt(target.innerHTML)+parseInt(num))
                btn.removeAttr('disabled');
                layer.msg("清理成功");

            }
            else {

                btn.removeAttr('disabled');

                layer.msg('清理失败');

            }
        },
        error: function () {
            layer.msg('请求失败');
            btn.removeAttr('disabled');
        }


    })
});

// $(document).on('click', '.jump', function(){
//    page = $('#page').val();
//    window.location = '/back?page='+page;
// });