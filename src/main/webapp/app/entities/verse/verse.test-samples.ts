import { IVerse, NewVerse } from './verse.model';

export const sampleWithRequiredData: IVerse = {
  id: '3fced619-130c-49ac-96f2-7a7b0829c171',
};

export const sampleWithPartialData: IVerse = {
  id: 'f56adb25-91b3-4727-aaac-66fd60a9228e',
  verse: 'meaty deafening',
};

export const sampleWithFullData: IVerse = {
  id: 'f2a1dbc2-5767-440b-96d6-3fab00021198',
  verse: 'worriedly gee',
  diacriticVerse: 'grimy',
};

export const sampleWithNewData: NewVerse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
