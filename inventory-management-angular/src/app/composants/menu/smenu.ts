export interface SMenu {
  id?: string;
  titre?: string;
  icon?: string;
  url?: string;
  active?: boolean;
  sousMenu?: Array<SMenu>;
}
