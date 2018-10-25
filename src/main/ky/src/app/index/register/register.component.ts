import {Component, Inject, Input, OnInit, ViewChild, TemplateRef, ElementRef} from '@angular/core';
import { Http, Jsonp, Headers } from '@angular/http';
declare var $: any;

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  @ViewChild('input') userName: ElementRef;
  @ViewChild('input1') password: ElementRef;
  @ViewChild('input2') tPassword: ElementRef;
  @ViewChild('input3') mobile: ElementRef;
  @ViewChild('input4') yzm: ElementRef;
  @ViewChild('input5') yzmB: ElementRef;
  userNameValue='';
  passwordValue='';
  mobileValue='';
  yzmValue='';
  yzmID='';
  yzUserName = false;
  yzUserName1 = false;
  yzUserName2 = false;
  yzPassword = false;
  yzTPassword = false;
  yzMobile = true;
  yzMobile1 = false;
  yzYzm = false;
  isLoad=false;
  uPattern = /^[a-zA-Z0-9_-]{4,15}$/;
  uPattern1 = /^[a-zA-Z0-9]{5,15}$/;
  uPattern2 = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
  constructor(
    private http:Http,
    private jsonp:Jsonp,
  ) {};

  ngOnInit() {
  }
  private headers = new Headers({'Content-Type': 'application/json'})
  user={
    userName: false,
    password: false,
    tPassword: false,
    mobile: false,
    yzm: false
  };
  userMessage={
    username: this.userNameValue,
    password: this.password,
    mobile: this.mobileValue
  };
  identifyCode={
    id: this.yzmID,
    mobile: this.mobileValue,
    code: this.yzmValue
  }
  blur(o,p,uPattern){
    if($(o.nativeElement).val().length<=0){
      switch(p){
        case 'userName':this.user.userName=false;break;
        case 'password':this.user.password=false;break;
        case 'tPassword':this.user.tPassword=false;break;
        case 'mobile':this.user.mobile=false;break;
        case 'yzm':this.user.yzm=false;this.yzYzm=true;break;
      }
    }
    else{
      this.yz(o,p,uPattern);
    }
  }
  yz(o,p,uPattern){
    switch(p){
      case 'userName':
        if(!uPattern.test($(o.nativeElement).val())){
          this.yzUserName=true;
        }
        else{
          this.yzUserName=false;
          this.yzUserName2=true;
          this.userNameValue=$(o.nativeElement).val();
          this.http.get("/user/user/" + this.userNameValue)
            .subscribe(data =>{
                if (data.json().code == 0) {
                  this.yzUserName1 = true;
                  this.userNameValue=$(o.nativeElement).val();
                }
                else {
                  this.yzUserName1= false;
                }
              },err=>{
                alert('请求失败');
              }
            );
        }
        break;
      case 'password':
        if(!uPattern.test($(o.nativeElement).val())){
          this.yzPassword=true;
        }
        else{
          this.yzPassword=false;
          this.passwordValue=$(o.nativeElement).val();
        }
        break;
      case 'tPassword':
        if($(o.nativeElement).val() !== this.passwordValue){
          this.yzTPassword=true;
        }
        else{
          this.yzTPassword=false;
        }
        break;
      case 'mobile':
        if(!uPattern.test($(o.nativeElement).val())){
          this.yzMobile=true;
        }
        else{
          this.mobileValue=$(o.nativeElement).val();
          this.yzMobile=false;
        }
        break;
      case 'yzm':
        if($(o.nativeElement).val().length<=0){
          this.yzYzm=true;
          this.user.yzm=true;
        }
        else{
          this.yzYzm=false;
        }
        break;
    }

  }
  jishi(){
    let time = 120;
    $(this.yzmB.nativeElement).text(time + "s后重新获取");
    let setTime = setInterval(set=>{
      time--;
      $(this.yzmB.nativeElement).text(time + "s后重新获取");
      if (time == 0) {
        clearInterval(setTime);
        this.yzMobile1=false;
        $(this.yzmB.nativeElement).text("获取验证码");
      }
    }, 1000);
  }
  getYzm(){
    this.http
      .post( "/user/sms/" + this.mobileValue,
        '', {headers:this.headers})
      .subscribe(res=>{
          if (res.json().code == 0) {
            this.yzMobile1=true;
            this.yzmID=res.json().id;
            this.jishi();
          }
          else {
            alert(res.json().msg);
          }
        }
      );
  }
  register(){
    this.yzmValue=$(this.yzm.nativeElement).val();
    this.isLoad=true;
    this.http
      .post('/user/user/',
        JSON.stringify(
          {user: this.userMessage,
           identifyCode: this.identifyCode
          }),
        {headers:this.headers})
      .subscribe(res=>{
        if(res.json().code===0){
          this.isLoad=false;
          alert(res.json().msg);
        }
        else{
          this.isLoad=false;
          alert(res.json().msg);
        }
      });
  }
}
