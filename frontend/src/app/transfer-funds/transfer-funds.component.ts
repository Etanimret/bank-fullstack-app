import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transfer-funds',
  templateUrl: './transfer-funds.component.html',
  styleUrls: ['./transfer-funds.component.css']
})
export class TransferFundsComponent {
  selectedAccount: any = null;
  balance: number = 0;
  accountNumber: string = '';
  targetAccountNumber: string = '';
  amount: string = '';
  pin: string = '';
  verifyResult: any = null;
  error: string = '';
  successMessage: string = '';
  isVerifying: boolean = false;
  isTransferring: boolean = false;

  constructor(private http: HttpClient, private router: Router) {
    const nav = this.router.getCurrentNavigation();
    this.selectedAccount = nav?.extras?.state?.selectedAccount || null;
    if (this.selectedAccount) {
      this.balance = this.selectedAccount.balance;
      this.accountNumber = this.selectedAccount.accountNumber;
    }
  }

  formatMoney(value: number | string): string {
    if (value === null || value === undefined) return '';
    return (+value).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  verifyTransfer() {
    this.error = '';
    this.successMessage = '';
    this.verifyResult = null;

    if (!/^\d{7}$/.test(this.targetAccountNumber)) {
      this.error = 'Target account number must be 7 digits.';
      return;
    }
    if (!/^\d+(\.\d{1,2})?$/.test(this.amount)) {
      this.error = 'Amount must be a valid number with up to two decimals.';
      return;
    }

    this.isVerifying = true;
    const params = new HttpParams()
      .set('selfAccountNumber', this.accountNumber)
      .set('amount', this.amount)
      .set('targetAccountNumber', this.targetAccountNumber);

    this.http.post<any>('/api/financial/verify-transfer', null, { params })
      .subscribe({
        next: (res) => {
          this.verifyResult = res;
          if (!res.isValid) {
            this.error = res.message || 'Transfer verification failed.';
          }
          this.isVerifying = false;
        },
        error: () => {
          this.error = 'Failed to verify transfer.';
          this.isVerifying = false;
        }
      });
  }

  confirmTransfer() {
    this.error = '';
    this.successMessage = '';

    if (!this.verifyResult?.isValid) {
      this.error = 'Please verify a valid transfer before confirming.';
      return;
    }
    if (!/^\d{6}$/.test(this.pin)) {
      this.error = 'PIN must be 6 digits.';
      return;
    }

    this.isTransferring = true;
    const params = new HttpParams()
      .set('selfAccountNumber', this.accountNumber)
      .set('amount', this.amount)
      .set('targetAccountNumber', this.targetAccountNumber);

    this.http.post<any>('/api/financial/transfer', null, { params })
      .subscribe({
        next: (res) => {
          this.successMessage = 'Transfer success';
          this.balance = res.balance;
          this.pin = '';
          this.verifyResult = null;
          this.amount = '';
          this.targetAccountNumber = '';
          this.isTransferring = false;
        },
        error: () => {
          this.error = 'Transfer failed. Please try again.';
          this.isTransferring = false;
        }
      });
  }
}