import { Injectable } from '@angular/core';
import { Observable, forkJoin, map } from 'rxjs';
import { Article } from '../article/article';
import { Cltfrs } from '../cltfrs/cltfrs';
import { Cmdcltfrs } from '../cmdcltfrs/cmdcltfrs';

export interface DashboardStats {
  totalProducts: number;
  totalClients: number;
  totalSuppliers: number;
  totalOrders: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(
    private readonly articleService: Article,
    private readonly cltfrsService: Cltfrs,
    private readonly cmdService: Cmdcltfrs
  ) { }

  getDashboardStats(): Observable<DashboardStats> {
    return forkJoin({
      articles: this.articleService.findAllArticle(),
      clients: this.cltfrsService.findAllClients(),
      fournisseurs: this.cltfrsService.findAllFournisseurs(),
      commandesClient: this.cmdService.findAllCommandesClient(),
      commandesFournisseur: this.cmdService.findAllCommandesFournisseur()
    }).pipe(
      map(data => ({
        totalProducts: data.articles?.length || 0,
        totalClients: data.clients?.length || 0,
        totalSuppliers: data.fournisseurs?.length || 0,
        totalOrders: (data.commandesClient?.length || 0) + (data.commandesFournisseur?.length || 0)
      }))
    );
  }

  getRecentActivities(): Observable<any[]> {
    return forkJoin({
      articles: this.articleService.findAllArticle(),
      clients: this.cltfrsService.findAllClients(),
      fournisseurs: this.cltfrsService.findAllFournisseurs(),
      commandesClient: this.cmdService.findAllCommandesClient(),
      commandesFournisseur: this.cmdService.findAllCommandesFournisseur()
    }).pipe(
      map(data => {
        const activities: any[] = [];

        // Ajouter les articles récents (derniers 2)
        if (data.articles && data.articles.length > 0) {
          data.articles.slice(-2).forEach((article, index) => {
            activities.push({
              type: 'product',
              action: 'Ajout produit',
              item: article.designation || 'Produit sans nom',
              time: this.getRelativeTime((article as any).creationDate),
              sortOrder: Date.now() - (index * 1000) // Ordre artificiel pour tri
            });
          });
        }

        // Ajouter les clients récents (dernier 1)
        if (data.clients && data.clients.length > 0) {
          const recentClient = data.clients[data.clients.length - 1];
          activities.push({
            type: 'client',
            action: 'Nouveau client',
            item: `${recentClient.nom || ''} ${recentClient.prenom || ''}`.trim() || 'Client sans nom',
            time: this.getRelativeTime((recentClient as any).creationDate),
            sortOrder: Date.now() - 2000
          });
        }

        // Ajouter les commandes récentes (dernières 2)
        if (data.commandesClient && data.commandesClient.length > 0) {
          data.commandesClient.slice(-2).forEach((commande, index) => {
            activities.push({
              type: 'order',
              action: 'Commande client',
              item: commande.code || 'Commande sans code',
              time: this.getRelativeTime((commande as any).creationDate),
              sortOrder: Date.now() - (3000 + index * 1000)
            });
          });
        }

        if (data.commandesFournisseur && data.commandesFournisseur.length > 0) {
          data.commandesFournisseur.slice(-1).forEach(commande => {
            activities.push({
              type: 'order',
              action: 'Commande fournisseur',
              item: commande.code || 'Commande sans code',
              time: this.getRelativeTime((commande as any).creationDate),
              sortOrder: Date.now() - 5000
            });
          });
        }

        // Trier par ordre artificiel et limiter à 5
        const sortedActivities = [...activities].sort((a, b) => b.sortOrder - a.sortOrder);
        return sortedActivities.slice(0, 5);
      })
    );
  }

  private getRelativeTime(dateString?: string): string {
    if (!dateString) return 'Récemment';
    
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / (1000 * 60));
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

    if (diffMins < 1) return 'À l\'instant';
    if (diffMins < 60) return `${diffMins} min`;
    if (diffHours < 24) return `${diffHours}h`;
    if (diffDays < 7) return `${diffDays}j`;
    return 'Il y a longtemps';
  }

  getLowStockItems(): Observable<any[]> {
    return this.articleService.findAllArticle().pipe(
      map(articles => {
        // Filtrer les articles avec un stock faible (simulation)
        return articles
          .filter(article => (article as any).quantiteStock && (article as any).quantiteStock < 10)
          .slice(0, 5) // Prendre les 5 premiers
          .map(article => ({
            name: article.designation || 'Article sans nom',
            stock: (article as any).quantiteStock || 0,
            minStock: 10 // Seuil minimum simulé
          }));
      })
    );
  }
}
