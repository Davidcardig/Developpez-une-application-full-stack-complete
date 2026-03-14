import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { PostDetailComponent } from './pages/post-detail/post-detail.component';
import { UnauthGuard } from './guards/unauth.guard';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [UnauthGuard],
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [UnauthGuard]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'themes',
    component: ThemesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'posts/create',
    component: CreatePostComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'posts/:id',
    component: PostDetailComponent,
    canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
