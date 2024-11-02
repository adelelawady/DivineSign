import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '65b51a76-d880-46cc-a93c-f88af49a272a',
};

export const sampleWithPartialData: IAuthority = {
  name: 'efa67aac-7a28-4424-a83d-3f5022a92249',
};

export const sampleWithFullData: IAuthority = {
  name: '6104c0de-9367-4a29-b1be-fbf418a4d47a',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
