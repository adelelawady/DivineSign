export interface Splind {
  id: number;
  title: string;
  description: string;
  author: string;
  likes: number;
  comments?: number;
  tags: string[];
  createdAt: Date;
}
