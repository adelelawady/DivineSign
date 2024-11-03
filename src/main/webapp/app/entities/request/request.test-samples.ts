import { IRequest, NewRequest } from './request.model';

export const sampleWithRequiredData: IRequest = {
  id: '610de254-6e66-42ec-a270-9ffea9d29fcf',
  title: 'smarten considering underneath',
  content: '../fake-data/blob/hipster.txt',
  verified: true,
};

export const sampleWithPartialData: IRequest = {
  id: 'bc507e4b-ce0e-4507-8fdf-7c62d7f022c0',
  title: 'exacerbate who underneath',
  content: '../fake-data/blob/hipster.txt',
  verified: true,
};

export const sampleWithFullData: IRequest = {
  id: '60267ed0-682b-4f62-8b36-fdfe9e772857',
  title: 'yowza shimmering ride',
  content: '../fake-data/blob/hipster.txt',
  verified: false,
  deleted: true,
  status: 'COMPLETED',
};

export const sampleWithNewData: NewRequest = {
  title: 'any actually provided',
  content: '../fake-data/blob/hipster.txt',
  verified: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
