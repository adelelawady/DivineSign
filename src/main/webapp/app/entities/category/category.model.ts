import { IValidationModel } from 'app/entities/validation-model/validation-model.model';

export interface ICategory {
  id: string;
  title?: string | null;
  description?: string | null;
  validationMethod?: Pick<IValidationModel, 'id'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
