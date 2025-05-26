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
  this.http.post('/api/customer/register-account', this.customer, { observe: 'response' })
    .subscribe({
      next: (response) => {
        console.log("Response" + response);
        if (response && (response.status === 201 || response.status === 200 || response.ok)) {
          this.registerSuccess = 'Account created successfully!';
        } else {
          this.registerError = 'Registration failed.';
        }
      },
      error: (err) => {
        console.log("Error" + err);
        this.registerError = err.error?.message || 'Registration failed.';
      }
    });
}
}