import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register/register.component';
import {RouterModule, Routes} from "@angular/router";
import { IndexComponent } from './index.component';
const appRoutes:Routes=[
 //{path:'',component:IndexComponent},
//{path:'',component:AppComponent},
//{path:'register',component:RegisterComponent},
 // {path:'',redirectTo:'index',pathMatch:'full'},
 // {path:'index',component:IndexComponent},
//  {path:'register',component:RegisterComponent}
//{path:'about', loadChildren:'./about/about.module#AboutModule'}
];
@NgModule({
  imports: [
    CommonModule,
  //  RouterModule.forChild(appRoutes)
  ],
  declarations: [
    //RegisterComponent,
   // IndexComponent
  ]
})
export class IndexModule { }
