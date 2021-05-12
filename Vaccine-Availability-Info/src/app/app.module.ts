import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterAlertComponent } from './register-alert/register-alert.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterAlertComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    {provide :'BASE_URL', usefactory: getBaseUrl}
  ],
  bootstrap: [AppComponent]
})
export function getBaseUrl() {
    return document.getElementsByTagName('base')[0].href;
}
export class AppModule { }