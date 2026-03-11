import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  currentUser: User | null = null;
  loading = false;
  saving = false;
  errorMessage = '';
  successMessage = '';
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      email: ['', [Validators.email]],
      username: ['', [Validators.minLength(3), Validators.maxLength(50)]],
      password: ['', [Validators.minLength(6)]]
    });

    this.loading = true;
    this.userService.getProfile().subscribe({
      next: (user) => {
        this.currentUser = user;
        this.profileForm.patchValue({
          email: user.email,
          username: user.username
        });
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger le profil.';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) {
      return;
    }

    this.saving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formValue = this.profileForm.value;
    const request: { email?: string; username?: string; password?: string } = {};

    if (formValue.email && formValue.email !== this.currentUser?.email) {
      request.email = formValue.email;
    }
    if (formValue.username && formValue.username !== this.currentUser?.username) {
      request.username = formValue.username;
    }
    if (formValue.password) {
      request.password = formValue.password;
    }

    this.userService.updateProfile(request).subscribe({
      next: (updatedUser) => {
        this.currentUser = updatedUser;
        this.profileForm.patchValue({
          email: updatedUser.email,
          username: updatedUser.username,
          password: ''
        });
        this.successMessage = 'Profil mis à jour avec succès !';
        this.saving = false;
        // Mise à jour du currentUser dans AuthService
        this.authService.refreshCurrentUser();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour du profil.';
        this.saving = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}

