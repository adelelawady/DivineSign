import { ISurah } from 'app/entities/surah/surah.model';
import { ISplendVerses } from 'app/entities/splend-verses/splend-verses.model';

export interface IVerse {
  id: string;
  verse?: string | null;
  diacriticVerse?: string | null;
  surah?: Pick<ISurah, 'id'> | null;
  splendVerses?: Pick<ISplendVerses, 'id'> | null;
}

export type NewVerse = Omit<IVerse, 'id'> & { id: null };
