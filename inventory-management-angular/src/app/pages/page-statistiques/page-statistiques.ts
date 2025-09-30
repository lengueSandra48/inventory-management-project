import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../services/dashboard/dashboard.service';
import { Article } from '../../services/article/article';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-page-statistiques',
  imports: [CommonModule],
  templateUrl: './page-statistiques.html',
  styleUrls: ['./page-statistiques.css']
})
export class PageStatistiques implements OnInit {
  
  // KPI Data
  chiffreAffaires = 0;
  totalCommandes = 0;
  articlesEnStock = 0;
  alertesStock = 0;
  
  // Chart Data
  ventesData: number[] = [];
  ventesLabels: string[] = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul'];
  
  // Category Data
  categories: any[] = [];
  
  // Loading states
  isLoading = true;
  errorMessage = '';

  constructor(
    private readonly dashboardService: DashboardService,
    private readonly articleService: Article
  ) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      dashboardStats: this.dashboardService.getDashboardStats(),
      articles: this.articleService.findAllArticle(),
      lowStockItems: this.dashboardService.getLowStockItems()
    }).subscribe({
      next: (data) => {
        // KPI calculations
        const stats = data.dashboardStats;
        this.totalCommandes = stats.totalOrders;
        this.articlesEnStock = stats.totalProducts;
        this.alertesStock = data.lowStockItems.length;
        
        // Simulate revenue calculation (prix * quantité)
        this.chiffreAffaires = this.calculateRevenue(data.articles);
        
        // Generate monthly sales data based on orders
        this.ventesData = this.generateSalesData(data.dashboardStats.totalOrders);
        
        // Generate category performance
        this.categories = this.generateCategoryData(data.articles);
        
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des statistiques:', error);
        this.errorMessage = 'Erreur lors du chargement des statistiques';
        this.isLoading = false;
      }
    });
  }

  private calculateRevenue(articles: any[]): number {
    // Simulation du chiffre d'affaires basé sur les prix des articles
    return articles.reduce((total, article) => {
      const prix = article.prixUnitaireTtc || 0;
      const quantite = article.quantiteStock || 0;
      return total + (prix * Math.min(quantite, 10)); // Simulation ventes
    }, 0);
  }

  private generateSalesData(totalOrders: number): number[] {
    // Générer des données de ventes basées sur le nombre total de commandes
    const baseValue = Math.max(totalOrders / 7, 1);
    return this.ventesLabels.map((_, index) => {
      const variation = Math.random() * 0.4 + 0.8; // Variation entre 80% et 120%
      return Math.round(baseValue * variation);
    });
  }

  private generateCategoryData(articles: any[]): any[] {
    // Grouper les articles par catégorie et calculer les performances
    const categoryGroups = articles.reduce((acc: any, article: any) => {
      const category = article.category?.codeCategory || 'Autre';
      if (!acc[category]) {
        acc[category] = { count: 0, totalStock: 0 };
      }
      acc[category].count++;
      acc[category].totalStock += article.quantiteEnStock || 0;
      return acc;
    }, {});

    // Convertir en tableau et calculer les pourcentages
    const categories = Object.keys(categoryGroups).map((name) => {
      const group = categoryGroups[name];
      const totalValue = group.count * 100; // Simulation de valeur
      return {
        name,
        count: group.count,
        totalStock: group.totalStock,
        totalValue: totalValue,
        percentage: 0
      };
    });

    // Calculer les pourcentages relatifs
    const maxValue = Math.max(...categories.map(c => c.totalValue));
    categories.forEach(category => {
      category.percentage = maxValue > 0 ? Math.round((category.totalValue / maxValue) * 100) : 0;
    });

    return categories.slice(0, 5); // Limiter à 5 catégories
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(value);
  }

  getBarHeight(value: number): number {
    if (!this.ventesData || this.ventesData.length === 0) return 0;
    const maxValue = Math.max(...this.ventesData);
    return maxValue > 0 ? (value / maxValue) * 100 : 0;
  }
}
