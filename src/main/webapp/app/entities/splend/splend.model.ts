import { ICategory } from 'app/entities/category/category.model';
import { ISplendVerses } from 'app/entities/splend-verses/splend-verses.model';
import { IUser } from 'app/entities/user/user.model';

export interface ISplend {
  id: string;
  title?: string | null;
  content?: string | null;
  likes?: number | null;
  dislikes?: number | null;
  verified?: boolean | null;
  category?: Pick<ICategory, 'id'> | null;
  verses?: Pick<ISplendVerses, 'id'> | null;
  likedUsers?: Pick<IUser, 'id'>[] | null;
  dislikedSplends?: Pick<IUser, 'id'>[] | null;
}

export type NewSplend = Omit<ISplend, 'id'> & { id: null };
