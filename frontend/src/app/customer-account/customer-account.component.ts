import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.css']
})
export class CustomerAccountComponent implements OnInit {
  accountNumbers: string[] = [];
  message: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.retrieveAccountNumbers();
  }

  retrieveAccountNumbers(): void {
    this.http.get<string[]>('/customer/retrieve-account')
      .subscribe({
        next: (accounts) => {
          if (accounts.length > 0) {
            this.accountNumbers = accounts;
          } else {
            this.message = 'No accounts available. Please visit the bank.';
          }
        },
        error: () => {
          this.message = 'Error retrieving account information. Please try again later.';
        }
      });
  }
}