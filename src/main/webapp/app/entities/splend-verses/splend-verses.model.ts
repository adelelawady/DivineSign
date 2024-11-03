import { IValidationModel } from 'app/entities/validation-model/validation-model.model';

export interface ISplendVerses {
  id: string;
  word?: string | null;
  number?: string | null;
  condition?: string | null;
  validationMethod?: Pick<IValidationModel, 'id'> | null;
}

export type NewSplendVerses = Omit<ISplendVerses, 'id'> & { id: null };
