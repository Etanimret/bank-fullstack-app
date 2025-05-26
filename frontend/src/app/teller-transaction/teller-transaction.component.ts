import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-teller-transaction',
  templateUrl: './teller-transaction.component.html'
})
export class TellerTransactionComponent {
  type: 'deposit' | 'withdraw' = 'deposit';
  accountNumber = '';
  amount = '';
  terminalId: string = '0101';
  message = '';
  error = '';

  constructor(private http: HttpClient) {}

  confirm() {
    this.message = '';
    this.error = '';

    // Validation
    if (!/^\d{7}$/.test(this.accountNumber)) {
      this.error = 'Account Number must be 7 digits.';
      return;
    }
    if (!/^\d+(\.\d{1,2})?$/.test(this.amount)) {
      this.error = 'Amount must be a number with up to two decimals.';
      return;
    }
    if (!/^\d{4}$/.test(this.terminalId)) {
      this.error = 'Terminal ID must be 4 digits.';
      return;
    }

    const params = new HttpParams()
      .set('accountNumber', this.accountNumber)
      .set('amount', this.amount)
      .set('terminalId', this.terminalId);

    const url = this.type === 'deposit'
      ? '/api/financial/deposit'
      : '/api/financial/withdraw';

    this.http.post(url, null, { params })
      .subscribe({
        next: (res) => {
          this.message = 'Transaction successful!';
        },
        error: (err) => {
          this.error = 'Transaction failed. Please check your input and try again.';
        }
      });
  }
}