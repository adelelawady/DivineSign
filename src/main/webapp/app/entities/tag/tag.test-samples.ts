import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 'ac52ec43-0886-439a-9020-04fcb244c6aa',
  title: 'mount wash tiny',
};

export const sampleWithPartialData: ITag = {
  id: 'c2cf191f-baac-4886-bd0d-c7dedcfd3c02',
  title: 'plump yuck steeple',
};

export const sampleWithFullData: ITag = {
  id: '0763351f-1dff-4bae-bb4c-f12176d86e5c',
  title: 'equal',
};

export const sampleWithNewData: NewTag = {
  title: 'what meanwhile',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
