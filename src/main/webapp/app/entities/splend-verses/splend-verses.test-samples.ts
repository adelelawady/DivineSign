import { ISplendVerses, NewSplendVerses } from './splend-verses.model';

export const sampleWithRequiredData: ISplendVerses = {
  id: '5a0161ee-a8e1-4bdb-99db-43a75759e592',
};

export const sampleWithPartialData: ISplendVerses = {
  id: 'ac2b1b3c-6fa1-4b75-a1f4-7c7b69da3e3b',
  number: 'kooky adolescent',
};

export const sampleWithFullData: ISplendVerses = {
  id: '50659f7d-6c26-4f55-9497-b17893c00e6c',
  word: 'violin but',
  number: 'famously',
  condition: 'follower lest video',
};

export const sampleWithNewData: NewSplendVerses = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
