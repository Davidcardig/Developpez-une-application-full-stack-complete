export interface PostResponse {
  id: number;
  title: string;
  content: string;
  authorUsername: string;
  themeTitle: string;
  themeId: number;
  createdAt: Date;
  comments: CommentResponse[];
}

export interface PostRequest {
  themeId: number;
  title: string;
  content: string;
}

export interface CommentResponse {
  id: number;
  content: string;
  authorUsername: string;
  createdAt: Date;
}

export interface CommentRequest {
  content: string;
}

