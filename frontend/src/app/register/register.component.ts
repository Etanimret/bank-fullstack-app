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
    this.http.post<string>('http://localhost:8080/customer/register-account', this.customer)
      .subscribe({
        next: (msg) => {
          this.registerSuccess = 'Account created successfully!';
        },
        error: (err) => {
          this.registerError = err.error?.message || 'Registration failed.';
        }
      });
  }
}