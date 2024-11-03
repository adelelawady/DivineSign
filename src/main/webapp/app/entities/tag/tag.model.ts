import { ISplend } from 'app/entities/splend/splend.model';

export interface ITag {
  id: string;
  title?: string | null;
  splend?: Pick<ISplend, 'id'> | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
