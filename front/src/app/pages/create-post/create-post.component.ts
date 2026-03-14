import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Theme } from 'src/app/models/theme.model';
import { PostService } from 'src/app/services/post.service';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {
  postForm!: FormGroup;
  subscribedThemes: Theme[] = [];
  loading = false;
  submitting = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private themeService: ThemeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postForm = this.fb.group({
      themeId: [null, Validators.required],
      title: ['', [Validators.required, Validators.maxLength(200)]],
      content: ['', Validators.required]
    });

    this.loading = true;
    this.themeService.getSubscribedThemes().subscribe({
      next: (themes) => {
        this.subscribedThemes = themes;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid) return;
    this.submitting = true;
    this.errorMessage = '';

    this.postService.createPost(this.postForm.value).subscribe({
      next: (post) => {
        this.router.navigate(['/posts', post.id]);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la création de l\'article.';
        this.submitting = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}

