import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { RegisterComponent } from './index/register/register.component';
import {RouterModule, Routes} from "@angular/router";
import { IndexComponent } from './index/index.component';
import { IndexModule } from './index/index.module';
const appRoutes:Routes=[
{path:'',redirectTo:'index',pathMatch:'full'},
//{path:'',component:AppComponent},
//{path:'register',component:RegisterComponent},
  {path:'index',
    component:IndexComponent,
    children: [
      {
        path:'register',
        component:RegisterComponent
      }]}
//{path:'about', loadChildren:'./about/about.module#AboutModule'}
];
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    IndexComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    IndexModule,
    HttpModule,
    JsonpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
