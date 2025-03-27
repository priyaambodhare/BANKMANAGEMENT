import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: false
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  isSubmitted = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.loginForm = this.fb.group({
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get formControls(): { [key: string]: AbstractControl } {
    return this.loginForm.controls;
  }

  loginUser() {
    this.isSubmitted = true;
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    const loginData = this.loginForm.value;

    this.http.post<{ message: string, token: string }>('http://localhost:3000/api/users/login', loginData)
      .subscribe({
        next: (response) => {
          this.isLoading = false;

          // ✅ Show SweetAlert success popup
          Swal.fire({
            icon: 'success',
            title: 'Login Successful!',
            text: 'Redirecting to your dashboard...',
            timer: 2000,
            showConfirmButton: false
          });

          localStorage.setItem('token', response.token); // Store token

          setTimeout(() => this.router.navigate(['/dashboard']), 2000);
        },
        error: () => {
          this.isLoading = false;

          // ❌ Show SweetAlert error popup
          Swal.fire({
            icon: 'error',
            title: 'Invalid Credentials',
            text: 'Please check your phone number and password.',
          });
        }
      });
  }
}
