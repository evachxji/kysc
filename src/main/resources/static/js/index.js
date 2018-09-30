/**
 * Created by DELL on 2018/9/28.
 */
$('#myModal').on('okHide', function(e){console.log('okHide')});
$('#myModal').on('okHidden', function(e){console.log('okHidden')});
$('#myModal').on('cancelHide', function(e){console.log('cancelHide')});
$('#myModal').on('cancelHidden', function(e){console.log('cancelHidden')});
var uPattern = /^[a-zA-Z0-9_-]{4,15}$/;
var uPattern1 = /^[a-zA-Z0-9]{5,15}$/;
var uPattern2 = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
var mobile1;
var id;
var istrue=false;
var istrue1=false;
var istrue2=false;
var istrue3=false;
$('.input-element input').focusin(function(){
    $(this).parent().addClass('active');

});

function yz(o,uPattern){
    if(!o.val().length > 0) {
        o.parent().removeClass('active');
    }
    else if(!uPattern.test(o.val()) && o.val().length > 0){
        o.parent().removeClass('active').addClass('error');
    }
    else{
        o.parent().removeClass('error').addClass('active');
    }
}
function yz1(o,uPattern){
    o.on('input propertychange', function() {
        if(uPattern.test(o.val()))
        {
            o.parent().removeClass('error').addClass('active');
        }
    });
}
function yz2(o){
    if(!o.val().length > 0) {
        o.parent().removeClass('active').addClass('error');
    }
}
$("#yz2").click(function(){
    mobile1 = String($("#input3").val());
    $.ajax({
        type: "post",
        url : "/user/sms/"+mobile1,
        dataType:'json',
        success: function(json){
            code1=json.code;
            if(code1==0) {
                id=json.id;
                istrue3=true;
                $("#yz2").attr("disabled",true);
                var time=120;
                $("#yz2").text(time+"s后重新获取");
                var set = setInterval(function(){
                    time--;
                    $("#yz2").text(time+"s后重新获取");
                    if(time==0){
                        clearInterval(set);
                        $("#yz2").attr("disabled",false).text("获取验证码");
                    }
                },1000);
            }
            else{
                alert(json.msg);
            }
        },
        error: function (json){
            alert("无法获取");
        }
    });
});
$('#input').blur(function(){
    istrue=false;
    yz($(this),uPattern);
    var username = $(this).val();
    if(uPattern.test(username)) {
        $.ajax({
            type: "get",
            url: "/user/user/"+username,
            dataType: 'json',
            success: function (json) {
                if (json.code==0) {
                    istrue=true;
                    $("#error").css("display", "none");
                    $("#success").css("display", "block");
                }
                else {
                    istrue=false;
                    $("#success").css("display", "none");
                    $("#error").css("display", "block");
                }
            },
            error: function (json){
               alert("请求失败");
            }
        });
    }
});
$('#input1').blur(function(){
    yz($(this),uPattern1);
    if(uPattern1.test($(this).val())){
        istrue1=true;
    }
    else{
        istrue1=false;
    }
});
yz1($('#input'),uPattern);
yz1($('#input1'),uPattern1);
$('#input2').blur(function(){
    if(!$(this).val().length > 0) {
        $(this).parent().removeClass('active');
        istrue2=false;
    }
    else if($(this).val() != $('#input1').val() && $(this).val().length > 0){
        $(this).parent().removeClass('active').addClass('error');
        istrue2=false;
    }
    else{
        $(this).parent().removeClass('error').addClass('active');
        istrue2=true;
    }
});
$("#input2").on('input propertychange', function() {
    if($(this).val() === $('#input1').val())
    {
        $(this).parent().removeClass('error').addClass('active');
    }
});
$("#input3").on('input propertychange', function() {
    if(uPattern2.test($(this).val()))
    {
        $(this).parent().removeClass('error').addClass('active');
        if(!istrue3) {
            $(this).prev().attr("disabled", false);
        }
    }
    else{
        $(this).prev().attr("disabled",true);
    }
});
$('#input3,#input4').blur(function(){
    if(!$(this).val().length > 0) {
        $(this).parent().removeClass('active');
    }
});
$('#input3').blur(function(){
    yz($(this),uPattern2)
});
$("#reg").click(function(){
    yz2($("#input"));
    yz2($("#input1"));
    yz2($("#input2"));
    yz2($("#input3"));
    yz2($("#input4"));
    var username=$("#input").val();
    var password=$("#input1").val();
    var mobile=String($("#input3").val());
    var yzm1=$("#input4").val();
        if (istrue && istrue1 && istrue2 && mobile == mobile1 && yzm1.length>0) {
            $("#input4").parent().removeClass('error').addClass('active');
            $("#load").css("display","inline");
            $.ajax({
                type: "post",
                url: "/user/user/",
                dataType: 'json',
                contentType: "application/json;charset=UTF-8",
                data:JSON.stringify({
                    username:username,
                    password:password,
                    mobile:mobile,
                    id:id,
                    code:yzm1
                }),
                success: function (json) {
                    $("#load").css("display","none");
                    if(json.code==0){
                    alert(json.msg);
                    }
                    else{
                        alert(json.msg);
                    }
                },
                error: function (json) {
                    alert("请求失败");
                }
            });
        }
});