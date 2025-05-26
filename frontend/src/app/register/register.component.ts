import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  customer = {
    email: '',
    password: '',
    citizenId: '',
    gender: '',
    accountHolderNameTh: '',
    accountHolderNameEn: '',
    pin: ''
  };
  registerError = '';
  registerSuccess = '';

  constructor(private http: HttpClient) {}

  register() {
    this.registerError = '';
    this.registerSuccess = '';
    this.http.post('/api/customer/register-account', this.customer, { observe: 'response', responseType: 'text' })
      .subscribe({
        next: (response) => {
          if (response.status === 201 && response.body === 'Register success') {
            this.registerSuccess = 'Account created successfully!';
          } else {
            this.registerError = 'Registration failed.';
          }
        },
        error: (err) => {
          this.registerError = err.error?.message || 'Registration failed.';
        }
      });
  }
}