import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {IndividualUserControllerComponent} from "./dashboard/individual-user-controller/individual-user-controller.component";
import {sendPinModalComponent} from "./dashboard/modal/sendPinModal.component";
import {HelpComponentComponent} from './help-component/help-component.component';
import {IndividualUserServiceComponent } from './dashboard/individual-user-service/individual-user-service.component';
import {SendPinModalComponent} from './dashboard/modal/send-pin-modal/send-pin-modal.component';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    IndividualUserControllerComponent,
    sendPinModalComponent,
    HelpComponentComponent,
    IndividualUserServiceComponent,
    SendPinModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule{ }
