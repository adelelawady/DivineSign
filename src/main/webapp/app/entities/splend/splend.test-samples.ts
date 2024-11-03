import { ISplend, NewSplend } from './splend.model';

export const sampleWithRequiredData: ISplend = {
  id: '01ee7b60-d67f-460b-9143-558a1dd17c76',
  title: 'including restfully',
  content: '../fake-data/blob/hipster.txt',
  likes: 13878,
  dislikes: 4190,
  verified: false,
};

export const sampleWithPartialData: ISplend = {
  id: '6313fc06-ef7e-459b-9d9c-f88e8bfc9b74',
  title: 'birdbath',
  content: '../fake-data/blob/hipster.txt',
  likes: 30901,
  dislikes: 1597,
  verified: false,
};

export const sampleWithFullData: ISplend = {
  id: 'ea248ef2-4922-4800-8baa-3cc290887c27',
  title: 'poorly',
  content: '../fake-data/blob/hipster.txt',
  likes: 31990,
  dislikes: 13983,
  verified: false,
};

export const sampleWithNewData: NewSplend = {
  title: 'labourer greedily which',
  content: '../fake-data/blob/hipster.txt',
  likes: 9810,
  dislikes: 8391,
  verified: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
