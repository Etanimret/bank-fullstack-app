import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { TellerCreateAccountComponent } from './teller-create-account/teller-create-account.component';
import { TellerTransactionComponent } from './teller-transaction/teller-transaction.component';
import { CustomerAccountComponent } from './customer-account/customer-account.component';
import { TransferFundsComponent } from './transfer-funds/transfer-funds.component';
import { BankStatementComponent } from './bank-statement/bank-statement.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'teller-create-account', component: TellerCreateAccountComponent },
  { path: 'teller-transaction', component: TellerTransactionComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' } //default route
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    TellerCreateAccountComponent,
    TellerTransactionComponent,
    CustomerAccountComponent,
    TransferFundsComponent,
    BankStatementComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}