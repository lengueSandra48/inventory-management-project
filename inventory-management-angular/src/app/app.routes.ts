import { Routes } from '@angular/router';

import {PageLogin} from './pages/page-login/page-login'
import {PageInscription} from './pages/page-inscription/page-inscription'
import {PageDashboard} from './pages/page-dashboard/page-dashboard';
import {PageStatistiques} from './pages/page-statistiques/page-statistiques';
import {PageArticle} from './pages/articles/page-article/page-article';
import {NouvelArticle} from './pages/articles/nouvel-article/nouvel-article';
import {PageMvtstk} from './pages/mvtstk/page-mvtstk/page-mvtstk';
import {PageClient} from './pages/client/page-client/page-client';
import {PageFournisseur} from './pages/fournisseur/page-fournisseur/page-fournisseur';
import {NouveauCltFrs} from './composants/nouveau-clt-frs/nouveau-clt-frs';
import {PageCmdCltFrs} from './pages/page-cmd-clt-frs/page-cmd-clt-frs';
import {NouvelleCmdCltFrs} from './composants/nouvelle-cmd-clt-frs/nouvelle-cmd-clt-frs';
import {PageCategories} from './pages/categories/page-categories/page-categories';
import {NouvelleCategory} from './pages/categories/nouvelle-category/nouvelle-category';
import {PageUtilisateur} from './pages/utilisateur/page-utilisateur/page-utilisateur';
import {NouvelUtilisateur} from './pages/utilisateur/nouvel-utilisateur/nouvel-utilisateur';
import {PageProfil} from './pages/profil/page-profil/page-profil';
import {ChangerMotDePasse} from './pages/profil/changer-mot-de-passe/changer-mot-de-passe';
import {PageRoles} from './pages/roles/page-roles/page-roles';
import {NouveauRole} from './pages/roles/nouveau-role/nouveau-role';
import {DashboardOverview} from './pages/dashboard-overview/dashboard-overview';
import {ApplicationGuard} from './services/guard/application-guard';
import {DetailArticle} from './pages/articles/detail-article/detail-article';
import {NouveauMouvement} from './pages/mvtstk/nouveau-mouvement/nouveau-mouvement';
import {DetailMouvement} from './pages/mvtstk/detail-mouvement/detail-mouvement';
import {PageEntrepriseComponent} from './pages/entreprises/page-entreprise/page-entreprise';
import {NouvelleEntrepriseComponent} from './pages/entreprises/nouvelle-entreprise/nouvelle-entreprise';
import {DetailCategorieComponent} from './pages/categories/detail-categorie/detail-categorie';
import {DetailClient} from './pages/client/detail-client/detail-client';
import {DetailCommandeClient} from './pages/commandes/detail-commande-client/detail-commande-client';
import {DetailFournisseur} from './pages/fournisseur/detail-fournisseur/detail-fournisseur';
import {DetailCommandeFournisseur} from './pages/commandes/detail-commande-fournisseur/detail-commande-fournisseur';
import {DetailUtilisateur} from './composants/detail-utilisateur/detail-utilisateur';
import {DetailRole} from './pages/roles/detail-role/detail-role';
import {DetailEntreprise} from './pages/entreprises/detail-entreprise/detail-entreprise';
export const routes: Routes = [
  {
    path: 'login',
    component : PageLogin
  },
  {
    path: 'inscrire',
    component: PageInscription
  },
  {
    path: '',
    component: PageDashboard,
    canActivate: [ApplicationGuard],
    children: [
      {
        path: '',
        component: DashboardOverview,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'statistiques',
        component: PageStatistiques,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'articles',
        component: PageArticle,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouvelarticle/:idArticle',
        component: NouvelArticle,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouvelarticle',
        component: NouvelArticle,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'articles/detail/:id',
        component: DetailArticle,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'mvtstk',
        component: PageMvtstk,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveaumouvement',
        component: NouveauMouvement,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveaumouvement/:id',
        component: NouveauMouvement,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailmouvement/:id',
        component: DetailMouvement,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'clients',
        component: PageClient,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveauclient',
        component: NouveauCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'client'
        }
      },
      {
        path: 'nouveauclient/:id',
        component: NouveauCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'client'
        }
      },
      {
        path: 'detailclient/:id',
        component: DetailClient,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailcommandeclient/:id',
        component: DetailCommandeClient,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'commandesclient',
        component: PageCmdCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'client'
        }
      },
      {
        path: 'nouvellecommandeclt',
        component: NouvelleCmdCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'client'
        }
      },
      {
        path: 'nouvellecommandeclt/:id',
        component: NouvelleCmdCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'client'
        }
      },
      {
        path: 'fournisseurs',
        component: PageFournisseur,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveaufournisseur',
        component: NouveauCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'fournisseur'
        }
      },
      {
        path: 'commandesfournisseur',
        component: PageCmdCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'fournisseur'
        }
      }
      ,
      {
        path: 'nouvellecommandefrs',
        component: NouvelleCmdCltFrs,
        canActivate: [ApplicationGuard],
        data:{
          origin: 'fournisseur'
        }
      }
      ,
      {
        path: 'categories',
        component: PageCategories,
        canActivate: [ApplicationGuard]
      }
      ,
      {
        path: 'nouvellecategorie',
        component: NouvelleCategory,
        canActivate: [ApplicationGuard]
      }
      ,
      {
        path: 'nouvellecategorie/:idCategory',
        component: NouvelleCategory,
        canActivate: [ApplicationGuard]
      }
      ,
      {
        path: 'utilisateurs',
        component: PageUtilisateur,
        canActivate: [ApplicationGuard]
      }
      ,
      {
        path: 'nouvelutilisateur',
        component: NouvelUtilisateur,
        canActivate: [ApplicationGuard]
      }
      ,
      {
        path: 'profil',
        component: PageProfil,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'changermotdepasse',
        component: ChangerMotDePasse,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'roles',
        component: PageRoles,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveaurole',
        component: NouveauRole,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveaurole/:id',
        component: NouveauRole,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouvelutilisateur/:id',
        component: NouvelUtilisateur,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouveauclient/:id',
        component: NouveauCltFrs,
        canActivate: [ApplicationGuard],
        data: {
          origin: 'client'
        }
      },
      {
        path: 'nouveaufournisseur/:id',
        component: NouveauCltFrs,
        canActivate: [ApplicationGuard],
        data: {
          origin: 'fournisseur'
        }
      },
      {
        path: 'entreprises',
        component: PageEntrepriseComponent,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouvelleentreprise',
        component: NouvelleEntrepriseComponent,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouvelleentreprise/:idEntreprise',
        component: NouvelleEntrepriseComponent,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailcategorie/:id',
        component: DetailCategorieComponent,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailfournisseur/:idFournisseur',
        component: DetailFournisseur,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailcommandefournisseur/:idCommandeFournisseur',
        component: DetailCommandeFournisseur,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailutilisateur/:id',
        component: DetailUtilisateur,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailrole/:id',
        component: DetailRole,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'detailentreprise/:id',
        component: DetailEntreprise,
        canActivate: [ApplicationGuard]
      },
      {
        path: 'nouvellecommandefrs/:id',
        component: NouvelleCmdCltFrs,
        canActivate: [ApplicationGuard],
        data: {
          origin: 'fournisseur'
        }
      }
    ]
  }
];
