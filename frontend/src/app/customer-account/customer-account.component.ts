import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AuthService } from '../authen/authService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.css']
})
export class CustomerAccountComponent implements OnInit {
  accounts: any[] = [];
  selectedAccount: any = null;
  message: string = '';

  email: string = '';
  password: string = '';

  constructor(private http: HttpClient, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.email = this.authService.getEmail();
    this.password = this.authService.getPassword();
    this.retrieveAccountNumbers();
  }

  retrieveAccountNumbers(): void {
    const params = new HttpParams()
      .set('email', this.email)
      .set('password', this.password);

    this.http.get<any>('/api/customer/retrieve-account', { params })
      .subscribe({
        next: (customer) => {
          if (customer.accounts && customer.accounts.length > 0) {
            this.accounts = customer.accounts;
            this.message = '';
          } else {
            this.accounts = [];
            this.message = 'Please go to bank and open account with teller';
          }
        },
        error: () => {
          this.accounts = [];
          this.message = 'Error retrieving account information. Please try again later.';
        }
      });
  }

  navigateToTransferFunds(selectedAccount: any): void {
    this.router.navigate(['/transfer-funds'], { state: { selectedAccount } });
  }

  navigateToBankStatement(selectedAccount: any): void {
    this.router.navigate(['/bank-statement'], { state: { selectedAccount } });
  }
}