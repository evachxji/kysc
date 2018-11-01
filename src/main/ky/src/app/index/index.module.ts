import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register/register.component';
import {RouterModule, Routes} from "@angular/router";
import { IndexComponent } from './index.component';
import { UploadImageComponent } from './upload-image/upload-image.component';
import { LoginComponent } from './login/login.component';
//import { HeadComponent } from './head/head.component';
const appRoutes:Routes=[
];
@NgModule({
  imports: [
    CommonModule,
  //  RouterModule.forChild(appRoutes)
  ],
  declarations: [
]
})
export class IndexModule { }
