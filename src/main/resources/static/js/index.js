/**
 * Created by DELL on 2018/9/28.
 */
$('#myModal').on('okHide', function(e){console.log('okHide')});
$('#myModal').on('okHidden', function(e){console.log('okHidden')});
$('#myModal').on('cancelHide', function(e){console.log('cancelHide')});
$('#myModal').on('cancelHidden', function(e){console.log('cancelHidden')});
var uPattern = /^[a-zA-Z0-9_-]{4,15}$/;
var uPattern1 = /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/;
var uPattern2 = /^[1][3,4,5,7,8][0-9]{9}$/;
var isTrue=false;
var isTrue1=false;
var isTrue2=false;
var isTrue3=false;
var isTrue4=false;
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
        var username = $("#input").val();
        $.ajax({
            type: "get",
            url : "/user/"+username,
            dataType:'json',
            //data: 'username='+username+'',
            success: function(json){
                if(json.code === 0){
                    $("#error").css("display","none");
                    $("#success").css("display","block");
                }
                else{
                    $("#success").css("display","none");
                    $("#error").css("display","block");
                }
            },
            error: function (json){
                alert("11");
            }
        });
        isTrue=true;
    }
}
function yz1(o,uPattern){
    o.on('input propertychange', function() {
        if(uPattern.test(o.val()))
        {
            isTrue1=true;
            o.parent().removeClass('error').addClass('active');
        }
    });
}

$("#yz2").click(function(){
    var mobile = String($("#input3").val());
    $.ajax({
        type: "post",
        url : "/sms/"+mobile,
        dataType:'json',
        success: function(json){
            alert(mobile);
        },
        error: function (json){
            alert("无法获取");
        }
    });
    $(this).attr("disabled",true);
    var time=120;
    $(this).text(time+"s后重新获取");
    var set = setInterval(function(){
        time--;
        $("#yz2").text(time+"s后重新获取");
        if(time==0){
            clearInterval(set);
            $("#yz2").attr("disabled",false).text("获取验证码");
        }
    },1000);

});
$('#input').blur(function(){
    yz($(this),uPattern);});
$('#input1').blur(function(){
    yz($(this),uPattern1);});
yz1($('#input'),uPattern);
yz1($('#input1'),uPattern1);
$('#input2').blur(function(){
    if(!$(this).val().length > 0) {
        $(this).parent().removeClass('active');
    }
    else if($(this).val() != $('#input1').val() && $(this).val().length > 0){
        $(this).parent().removeClass('active').addClass('error');
    }
    else{
        $(this).parent().removeClass('error').addClass('active');
        isTrue3=true;
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
        $(this).prev().attr("disabled",false);
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
