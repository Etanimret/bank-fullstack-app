import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  loginError = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.http.post<boolean>('/customer/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: (result) => {
        if (result) {
          this.loginError = '';
          alert('Login successful!');
        } else {
          this.loginError = 'Invalid credentials';
        }
      },
      error: () => {
        this.loginError = 'Login failed. Please check your credentials.';
      }
    });
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}