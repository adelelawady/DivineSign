import { ISplend } from 'app/entities/splend/splend.model';
import { IUser } from 'app/entities/user/user.model';

export interface IComment {
  id: string;
  content?: string | null;
  likes?: number | null;
  deleted?: boolean | null;
  splend?: Pick<ISplend, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
  parents?: Pick<IComment, 'id'>[] | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
