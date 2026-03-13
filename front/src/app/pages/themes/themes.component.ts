import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Theme } from 'src/app/models/theme.model';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  themes: Theme[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private themeService: ThemeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.loading = true;
    this.themeService.getAllThemes().subscribe({
      next: (themes) => {
        this.themes = themes;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les thèmes.';
        this.loading = false;
      }
    });
  }

  subscribe(theme: Theme): void {
    this.themeService.subscribe(theme.id).subscribe({
      next: () => {
        theme.subscribed = true;
        this.successMessage = `Abonné à "${theme.title}" avec succès !`;
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: () => {
        this.errorMessage = `Erreur lors de l'abonnement à "${theme.title}".`;
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}

