import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module'; // Import Routing Module
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // Add this
import { HttpClientModule } from '@angular/common/http'; // Add this for API calls

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,           // Add FormsModule
    ReactiveFormsModule,   // Add ReactiveFormsModule
    HttpClientModule ,      // Add HttpClientModule for API calls
    

  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
