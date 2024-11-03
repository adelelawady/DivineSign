import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: '5323dd60-92ed-4829-b5b0-d28e58089064',
  title: 'hmph',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ICategory = {
  id: '759c45ef-c118-4ef8-adea-4c80e8a48f37',
  title: 'lest aha yuck',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ICategory = {
  id: '4f3e002e-9b07-436e-b1a5-7ddb7cdba15e',
  title: 'what',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCategory = {
  title: 'meh',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
