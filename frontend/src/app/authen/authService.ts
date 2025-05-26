import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private emailKey = 'email';
  private passwordKey = 'password';

  setCredentials(email: string, password: string): void {
    localStorage.setItem(this.emailKey, email);
    localStorage.setItem(this.passwordKey, password);
  }

  getEmail(): string {
    return localStorage.getItem(this.emailKey) || '';
  }

  getPassword(): string {
    return localStorage.getItem(this.passwordKey) || '';
  }

  clearCredentials(): void {
    localStorage.removeItem(this.emailKey);
    localStorage.removeItem(this.passwordKey);
  }
}