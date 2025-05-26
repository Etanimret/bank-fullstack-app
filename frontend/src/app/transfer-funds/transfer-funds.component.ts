import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-transfer-funds',
  templateUrl: './transfer-funds.component.html',
  styleUrls: ['./transfer-funds.component.css']
})
export class TransferFundsComponent {
  accountNumber = '';
  balance = 0;
  transferAmount = '';
  error = '';
  successMessage = '';

  constructor(private http: HttpClient) {
    this.retrieveAccountInfo();
  }

  retrieveAccountInfo() {
    this.http.get<{ accountNumber: string; balance: number }>('/customer/retrieve-account')
      .subscribe({
        next: (data) => {
          this.accountNumber = data.accountNumber;
          this.balance = data.balance;
        },
        error: () => {
          this.error = 'Failed to retrieve account information.';
        }
      });
  }

  validateTransfer() {
    const amount = parseFloat(this.transferAmount);
    if (isNaN(amount) || amount <= 0) {
      this.error = 'Transfer amount must be a positive number.';
      return false;
    }
    if (amount > this.balance) {
      this.error = 'Insufficient balance for this transfer.';
      return false;
    }
    this.error = '';
    return true;
  }

  transferFunds() {
    if (!this.validateTransfer()) {
      return;
    }

    this.http.post('/financial/verify-transfer', { accountNumber: this.accountNumber, amount: this.transferAmount })
      .subscribe({
        next: () => {
          this.successMessage = 'Transfer verified. Proceeding with the transfer.';
          this.executeTransfer();
        },
        error: () => {
          this.error = 'Transfer verification failed. Please check your input.';
        }
      });
  }

  executeTransfer() {
    this.http.post('/financial/transfer', { accountNumber: this.accountNumber, amount: this.transferAmount })
      .subscribe({
        next: () => {
          this.successMessage = 'Transfer successful!';
          this.transferAmount = '';
          this.retrieveAccountInfo(); // Refresh balance after transfer
        },
        error: () => {
          this.error = 'Transfer failed. Please try again.';
        }
      });
  }
}