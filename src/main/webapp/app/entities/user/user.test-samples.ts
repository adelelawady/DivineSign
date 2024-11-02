import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: '9a7103d5-3aee-4fab-ac15-bf4760e5be5a',
  login: '1{Pm!@NEkbG\\tRf\\.2\\4KEfS\\_UN',
};

export const sampleWithPartialData: IUser = {
  id: '72d3008f-b1c5-4aaa-a030-02687bf78225',
  login: '8',
};

export const sampleWithFullData: IUser = {
  id: '2370d7e0-72a7-46a4-b2df-1d8ace0bf338',
  login: 'hg9S',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
