import { IValidationModel, NewValidationModel } from './validation-model.model';

export const sampleWithRequiredData: IValidationModel = {
  id: '80ad3646-1a4a-4d9b-b978-13946cc51f98',
};

export const sampleWithPartialData: IValidationModel = {
  id: '3096da9e-c99b-4f82-9351-468100fecb4d',
  name: 'modulo nor',
};

export const sampleWithFullData: IValidationModel = {
  id: 'a2c77557-5fec-44cd-80e2-6a65ea8aee87',
  name: 'culminate',
  type: 'enthusiastically',
};

export const sampleWithNewData: NewValidationModel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
