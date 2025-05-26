import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-bank-statement',
  templateUrl: './bank-statement.component.html',
  styleUrls: ['./bank-statement.component.css']
})
export class BankStatementComponent {
  statements: any[] = [];
  selectedMonth: string = '';
  error: string = '';

  constructor(private http: HttpClient) {}

  retrieveStatements() {
    if (!this.selectedMonth) {
      this.error = 'Please select a month.';
      return;
    }

    this.http.get<any[]>(`/api/financial/retrieveAllStatements?month=${this.selectedMonth}`)
      .subscribe({
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