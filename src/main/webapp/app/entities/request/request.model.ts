import { ISplend } from 'app/entities/splend/splend.model';
import { IUser } from 'app/entities/user/user.model';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface IRequest {
  id: string;
  title?: string | null;
  content?: string | null;
  verified?: boolean | null;
  deleted?: boolean | null;
  status?: keyof typeof RequestStatus | null;
  splend?: Pick<ISplend, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewRequest = Omit<IRequest, 'id'> & { id: null };
