import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-bank-statement',
  templateUrl: './bank-statement.component.html',
  styleUrls: ['./bank-statement.component.css']
})
export class BankStatementComponent {
  statements: any[] = [];
  selectedMonth: string = '';
  error: string = '';
  selectedAccount: any = null;

  constructor(private http: HttpClient, private router: Router) {
    const nav = this.router.getCurrentNavigation();
    this.selectedAccount = nav?.extras?.state?.selectedAccount || null;
  }

  retrieveStatements() {
    if (!this.selectedMonth) {
      this.error = 'Please select a month.';
      return;
    }
    if (!this.selectedAccount) {
      this.error = 'No account selected.';
      return;
    }

    const [year, month] = this.selectedMonth.split('-').map(Number);
    const fromDate = new Date(year, month - 1, 1, 0, 0, 0);
    const toDate = new Date(year, month, 0, 23, 59, 59);

    this.http.get<any[]>(
      `/api/financial/retrieveAllStatements`,
      {
        params: {
          accountId: this.selectedAccount.id,
          fromDate: fromDate.toISOString(),
          toDate: toDate.toISOString()
        }
      }
    ).subscribe({
      next: (res) => {
        this.statements = res;
        this.error = '';
      },
      error: () => {
        this.error = 'Failed to retrieve statements. Please try again.';
      }
    });
  }
}