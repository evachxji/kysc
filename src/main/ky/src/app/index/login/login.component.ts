import { Component, OnInit , ViewChild, TemplateRef, ElementRef} from '@angular/core';
import { Http, Jsonp, Headers } from '@angular/http';
declare var $: any;
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  @ViewChild('user') userName: ElementRef;
  @ViewChild('pass') passWord: ElementRef;

  constructor(
  	private http:Http,
    private jsonp:Jsonp,
    ) { }

  ngOnInit() {
  }
  private headers = new Headers({'Content-Type': 'application/json'})
  isUserName = false;
  isPassWord = false;
  isLoad = false;
  userNameValue = '';
  passWordValue = '';
  user={
    username:this.userNameValue,
    password:this.passWordValue
  }
  yzUser = false;
  yzPass = false;
  isBlur(o,p){
     if($(o.nativeElement).val()!=='' && $(o.nativeElement).val()!==null){
     	if(p === 'isUserName'){
     	this.yzUser = true;
        this.isUserName = true;
    }
    else{
    	this.yzPass = true;
    	this.isPassWord= true;
    }
     }
     else{
     	if(p === 'isUserName'){
        this.isUserName = false;
    }
    else{
    	this.isPassWord= false;
    }
    }

  }
  yzLogin(o,p){
  	if($(o.nativeElement).val()!=='' && $(o.nativeElement).val()!==null){
     	if(p === 'isUserName'){
     	this.yzUser = true;
    }
    else{
    	this.yzPass = true;
    }
     }
     else{
     	if(p === 'isUserName'){
        this.yzUser = false;
    }
    else{
    	this.yzPass= false;
    }
    }

  }
  login(){
  	this.userNameValue = $(this.userName.nativeElement).val();
    this.passWordValue = $(this.passWord.nativeElement).val();
    this.user.username = this.userNameValue;
    this.user.password = this.passWordValue;
    console.log(this.user);
    console.log(this.userNameValue);
    this.isLoad=true;
    this.http
      .post('/user/login/',
        JSON.stringify(
          {
            username:this.userNameValue,
            password:this.passWordValue
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
