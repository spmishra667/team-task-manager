import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './signup.html',
  styleUrl: './signup.css'
})
export class Signup {
  name = ''; email = ''; password = '';
  error = ''; loading = false;

  constructor(private auth: Auth, private router: Router) {}

  signup() {
    if (!this.name || !this.email || !this.password) {
      this.error = 'All fields required'; return;
    }
    if (this.password.length < 6) {
      this.error = 'Password must be at least 6 characters'; return;
    }
    this.loading = true;
    this.auth.signup({ name: this.name, email: this.email, password: this.password }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        this.error = err.error?.message || 'Signup failed';
        this.loading = false;
      }
    });
  }
}