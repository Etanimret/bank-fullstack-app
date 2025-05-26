import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../authen/authService';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  constructor(private authService: AuthService, private router: Router) {}

  logout() {
    this.authService.clearCredentials();
    this.router.navigate(['/login']);
  }

  goHome() {
    this.router.navigate(['/']);
  }

  get isSpecialRoute(): boolean {
    const specialRoutes = ['/teller-transaction', '/teller-create-account', '/register'];
    return specialRoutes.includes(this.router.url);
  }
}