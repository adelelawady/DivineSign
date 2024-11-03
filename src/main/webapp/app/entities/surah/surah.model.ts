export interface ISurah {
  id: string;
  name?: string | null;
  transliteration?: string | null;
  type?: string | null;
  totalVerses?: number | null;
}

export type NewSurah = Omit<ISurah, 'id'> & { id: null };
