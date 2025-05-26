import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../authen/authService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  loginError = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  login() {
    this.http.post<boolean>(
      '/api/customer/login',
      null,
      { params: { email: this.email, password: this.password } }
    ).subscribe({
      next: (result) => {
        if (result) {
          this.loginError = '';
          this.authService.setCredentials(this.email, this.password);
          alert('Login successful!');
          this.router.navigate(['/customer-account']);
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