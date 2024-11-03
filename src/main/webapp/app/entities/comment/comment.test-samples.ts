import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: '27ebeeda-4abd-489e-bc73-e334bb95a7c6',
  content: '../fake-data/blob/hipster.txt',
  likes: 14035,
};

export const sampleWithPartialData: IComment = {
  id: '520bff1e-cafe-482a-aba2-eb29252b296a',
  content: '../fake-data/blob/hipster.txt',
  likes: 10851,
};

export const sampleWithFullData: IComment = {
  id: '9d1cf88b-24e6-4bc3-9b18-bad0ab3e230f',
  content: '../fake-data/blob/hipster.txt',
  likes: 19784,
  deleted: true,
};

export const sampleWithNewData: NewComment = {
  content: '../fake-data/blob/hipster.txt',
  likes: 23725,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
