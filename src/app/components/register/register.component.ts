import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: false
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  isSubmitted = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      address: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get formControls(): { [key: string]: AbstractControl } {
    return this.registerForm.controls;
  }

  registerUser() {
    this.isSubmitted = true;
    if (this.registerForm.invalid) {
      return;
    }

    this.isLoading = true;
    const registerData = this.registerForm.value;

    this.http.post<{ message: string }>('http://localhost:3000/api/users/register', registerData)
      .subscribe({
        next: (response) => {
          this.isLoading = false;

          // ✅ Show SweetAlert success popup
          Swal.fire({
            icon: 'success',
            title: 'Registration Successful!',
            text: 'Redirecting to login page...',
            timer: 2000,
            showConfirmButton: false
          });

          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: () => {
          this.isLoading = false;

          // ❌ Show SweetAlert error popup
          Swal.fire({
            icon: 'error',
            title: 'Registration Failed!',
            text: 'Something went wrong. Please try again.',
          });
        }
      });
  }
}
