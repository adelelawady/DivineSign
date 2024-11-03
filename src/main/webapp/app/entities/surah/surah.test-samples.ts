import { ISurah, NewSurah } from './surah.model';

export const sampleWithRequiredData: ISurah = {
  id: '799b2d54-18e9-49c9-bcc7-8ee33a9b08a6',
};

export const sampleWithPartialData: ISurah = {
  id: 'cfe88ded-d392-4913-9279-75a7a0a51395',
  name: 'hmph',
  transliteration: 'storyboard',
  type: 'scale concerning afterwards',
  totalVerses: 8208,
};

export const sampleWithFullData: ISurah = {
  id: 'fe94fa0e-c4af-419c-b3db-89e123af7c25',
  name: 'newsletter provided hygienic',
  transliteration: 'peter',
  type: 'bah',
  totalVerses: 24238,
};

export const sampleWithNewData: NewSurah = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
