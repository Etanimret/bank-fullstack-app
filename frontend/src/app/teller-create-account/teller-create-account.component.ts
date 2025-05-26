import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-teller-create-account',
  templateUrl: './teller-create-account.component.html',
  styleUrls: ['./teller-create-account.component.css']
})
export class TellerCreateAccountComponent {
  citizenId = '';
  createError = '';
  createMessage = '';
  customer: any = null;
  retrieveError = '';

  constructor(private http: HttpClient) {}

  createAccount() {
    this.createError = '';
    this.createMessage = '';
    this.http.post('/api/teller/create-account', null, { 
        params: { citizenId: this.citizenId }, 
        responseType: 'text' 
      })
      .subscribe({
        next: (result) => {
          this.createMessage = result;
        },
        error: () => {
          this.createError = 'An error occurred. Please check your input and try again.';
        }
      });
  }

  retrieveAccount() {
    this.customer = null;
    this.retrieveError = '';
    this.http.get<any>('/api/teller/retrieve-account', {
      params: { citizenId: this.citizenId }
    }).subscribe({
      next: (data) => {
        if (data) {
          this.customer = data;
        } else {
          this.retrieveError = 'Data not found';
        }
      },
      error: () => {
        this.retrieveError = 'Data not found';
      }
    });
  }
}