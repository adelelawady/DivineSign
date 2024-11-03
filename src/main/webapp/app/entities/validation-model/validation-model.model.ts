export interface IValidationModel {
  id: string;
  name?: string | null;
  type?: string | null;
}

export type NewValidationModel = Omit<IValidationModel, 'id'> & { id: null };
