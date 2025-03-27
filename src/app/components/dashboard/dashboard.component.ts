
import { Component, OnInit } from '@angular/core';
import { HttpClient ,HttpHeaders} from '@angular/common/http';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone: false
})
export class DashboardComponent implements OnInit {
  fullName: string = '';
  isLoading = false;
  userProfile: any = {};
  showProfile = false;
  showDepositForm = false;
  depositAmount: number = 0;
  successMessage: string = '';
  balance: number = 0;
  rechargePlans: any[] = [];
  selectedProvider: string = '';
  selectedPlan: any = null;
  mobileNumber: string = '';
  password: string = '';
  showRechargeForm: boolean = false;  // ✅ Add this line
  showFundTransferForm = false;
  receiver: string = '';
  transferAmount: number = 0;
  isMobileTransfer: boolean = false;
  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchUserProfile();
  }

  fetchUserProfile() {
    this.http.get<{ fullName: string; phoneNumber: string; email: string; address: string; accountNumber: string }>(
      'http://localhost:3000/api/users/profile',
      { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
    ).subscribe({
      next: (data) => {
        this.userProfile = data;
        this.fullName = data.fullName;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error!', text: 'Failed to fetch profile data' });
      }
    });
  }

  toggleProfile() {
    this.showProfile = !this.showProfile;
  }

  toggleDepositForm() {
    this.showDepositForm = !this.showDepositForm;
  }
  toggleRechargeForm() {  // ✅ Add this method
    this.showRechargeForm = !this.showRechargeForm;
  }
  toggleFundTransferForm() {
    this.showFundTransferForm = !this.showFundTransferForm;
  }
  depositMoney() {
    if (this.depositAmount <= 0) {
      Swal.fire({ icon: 'error', title: 'Invalid Amount', text: 'Please enter a valid amount' });
      return;
    }

    const depositData = {
      accountNumber: this.userProfile.accountNumber,
      amount: this.depositAmount
    };

    this.http.post('http://localhost:3000/api/deposits', depositData, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    }).subscribe({
      next: () => {
        Swal.fire({ icon: 'success', title: 'Deposit Successful!', text: `₹${this.depositAmount} deposited successfully.` });
        this.depositAmount = 0;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Deposit Failed!', text: 'Transaction could not be processed.' });
      }
    });
  }

  checkBalance() {
    Swal.fire({
      title: 'Check Balance',
      html: `
        <input type="text" id="accountNumber" class="swal2-input" value="${this.userProfile.accountNumber}" disabled>
        <input type="password" id="password" class="swal2-input" placeholder="Enter Password">
      `,
      showCancelButton: true,
      confirmButtonText: 'Check Balance',
      preConfirm: () => {
        const password = (document.getElementById('password') as HTMLInputElement).value;

        if (!password) {
          Swal.showValidationMessage('Password is required');
          return false;
        }

        return { accountNumber: this.userProfile.accountNumber, password };
      }
    }).then((result) => {
      if (result.isConfirmed) {
        const { accountNumber, password } = result.value!;

        this.http.post<any>('http://localhost:3000/api/check-balance', 
          { accountNumber, password }, 
          { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
        ).subscribe({
          next: (response) => {
            if (response.balance !== undefined) {
              this.balance = response.balance;
              Swal.fire({ icon: 'success', title: 'Account Balance', text: `Your balance is ₹${this.balance}` });
            } else {
              Swal.fire({ icon: 'warning', title: 'Balance Not Found', text: 'No balance data available.' });
            }
          },
          error: (error) => {
            Swal.fire({ icon: 'error', title: 'Error!', text: error.error?.error || 'Failed to fetch balance' });
          }
        });
      }
    });
  }

   // ✅ Fetch Recharge Plans for Selected Provider
   getRechargePlans() {
    if (!this.selectedProvider) {
      Swal.fire({ icon: 'warning', title: 'Error', text: 'Please select a provider' });
      return;
    }

    this.http.get<any[]>(`http://localhost:3000/api/recharge-plans/${this.selectedProvider}`).subscribe({
      next: (response) => {
        this.rechargePlans = response;
      },
      error: (error) => {
        console.error('Error fetching recharge plans:', error);
        Swal.fire({ icon: 'error', title: 'Error', text: 'Failed to fetch recharge plans' });
      }
    });
  }

// ✅ Perform Recharge
recharge() {
  if (!this.mobileNumber || !this.selectedPlan || !this.password) {
    Swal.fire({
      icon: 'warning',
      title: 'Missing Details',
      text: 'Fill all fields before recharging'
    });
    return;
  }

  if (!this.selectedPlan.plan || typeof this.selectedPlan.plan !== 'string') {
    Swal.fire({
      icon: 'error',
      title: 'Invalid Plan Selected',
      text: 'Please select a valid recharge plan before proceeding.'
    });
    return;
  }

  Swal.fire({
    title: 'Confirm Recharge',
    text: `Recharge ${this.mobileNumber} with ${this.selectedPlan.plan}?`,
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Proceed'
  }).then((result) => {
    if (result.isConfirmed) {
      const requestBody = {
        accountNumber: this.userProfile.accountNumber,
        password: this.password,
        mobileNumber: this.mobileNumber,
        provider: this.selectedProvider,
        amount: this.selectedPlan.plan.replace('₹', '') // Ensure plan exists
      };

      const headers = new HttpHeaders({
        Authorization: `Bearer ${localStorage.getItem('token')}`
      });

      this.http.post<any>('http://localhost:3000/api/recharge', requestBody, { headers }).subscribe({
        next: (response) => {
          Swal.fire({
            icon: 'success',
            title: 'Recharge Successful',
            text: response.message
          });
          this.mobileNumber = '';
          this.password = '';
          this.selectedPlan = null;
        },
        error: (error) => {
          console.error('Recharge error:', error);
          Swal.fire({
            icon: 'error',
            title: 'Recharge Failed',
            text: error.error?.error || 'Recharge failed'
          });
        }
      });
    }
  });
}

transferFunds() {
  if (!this.receiver || this.transferAmount <= 0 || !this.password) {
    Swal.fire('Error', 'Please fill in all fields.', 'error');
    return;
  }

  const requestData = {
    senderAccount: localStorage.getItem('accountNumber'), // Assuming account number is stored in localStorage
    receiver: this.receiver,
    amount: this.transferAmount,
    isMobileNumber: this.isMobileTransfer
  };

  this.http.post('http://localhost:3000/api/fund-transfer', requestData).subscribe(
    (response: any) => {
      Swal.fire('Success', response.message, 'success');
      this.showFundTransferForm = false;
    },
    (error) => {
      Swal.fire('Error', 'Fund transfer failed. Please try again.', 'error');
    }
  );
}




  
  


  logoutUser() {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will be logged out!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, logout!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;

        this.http.post('http://localhost:3000/api/users/logout', {}, {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        }).subscribe({
          next: () => {
            this.isLoading = false;
            localStorage.removeItem('token');
            Swal.fire({
              icon: 'success',
              title: 'Logged Out!',
              text: 'Redirecting to login...',
              timer: 2000,
              showConfirmButton: false
            });
            setTimeout(() => this.router.navigate(['/login']), 2000);
          },
          error: () => {
            this.isLoading = false;
            Swal.fire({ icon: 'error', title: 'Logout Failed!', text: 'Please try again.' });
          }
        });
      }
    });
  }
}
