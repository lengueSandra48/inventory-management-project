import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService, DashboardStats } from '../../services/dashboard/dashboard.service';

@Component({
  selector: 'app-dashboard-overview',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-overview.html',
  styleUrls: ['./dashboard-overview.css']
})
export class DashboardOverview implements OnInit {
  
  // Dashboard statistics
  totalProducts = 0;
  totalClients = 0;
  totalSuppliers = 0;
  totalOrders = 0;
  isLoading = true;
  errorMessage = '';
  
  // Recent activities
  recentActivities = [
    { type: 'product', action: 'Ajout', item: 'Ordinateur portable HP', time: '2 minutes' },
    { type: 'order', action: 'Commande', item: 'CMD-2025-001', time: '15 minutes' },
    { type: 'client', action: 'Nouveau client', item: 'Jean Dupont', time: '1 heure' },
    { type: 'stock', action: 'Mouvement stock', item: 'Souris optique', time: '2 heures' },
    { type: 'supplier', action: 'Fournisseur', item: 'TechCorp SARL', time: '3 heures' }
  ];
  
  // Quick stats for charts
  monthlyStats = {
    sales: [65, 78, 90, 81, 56, 85, 92],
    orders: [28, 35, 42, 38, 29, 41, 45],
    products: [12, 19, 15, 22, 18, 25, 21]
  };
  
  // Low stock alerts
  lowStockItems: any[] = [];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.dashboardService.getDashboardStats().subscribe({
      next: (stats: DashboardStats) => {
        this.totalProducts = stats.totalProducts;
        this.totalClients = stats.totalClients;
        this.totalSuppliers = stats.totalSuppliers;
        this.totalOrders = stats.totalOrders;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des statistiques:', error);
        this.errorMessage = 'Erreur lors du chargement des données du tableau de bord';
        this.isLoading = false;
      }
    });

    // Charger les activités récentes
    this.dashboardService.getRecentActivities().subscribe({
      next: (activities) => {
        this.recentActivities = activities;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des activités:', error);
      }
    });

    // Charger les articles en stock faible
    this.dashboardService.getLowStockItems().subscribe({
      next: (items) => {
        this.lowStockItems = items;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des stocks faibles:', error);
      }
    });
  }
}
