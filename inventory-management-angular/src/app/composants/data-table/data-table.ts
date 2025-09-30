import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface DataTableColumn {
  key: string;
  label: string;
  sortable?: boolean;
  type?: 'text' | 'date' | 'badge' | 'actions';
  badgeConfig?: {
    [key: string]: { class: string; label?: string };
  };
}

export interface DataTableAction {
  icon: string;
  class: string;
  tooltip: string;
  action: string;
}

@Component({
  selector: 'app-data-table',
  imports: [CommonModule],
  templateUrl: './data-table.html',
  styleUrl: './data-table.css'
})
export class DataTable {
  @Input() columns: DataTableColumn[] = [];
  @Input() data: any[] = [];
  @Input() actions: DataTableAction[] = [];
  @Input() isLoading = false;
  @Input() itemsPerPage = 10;
  @Input() showPagination = true;
  
  @Output() actionClick = new EventEmitter<{action: string, item: any}>();
  @Output() sortChange = new EventEmitter<{column: string, direction: 'asc' | 'desc'}>();

  currentPage = 1;
  sortColumn = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  get totalPages(): number {
    return Math.ceil(this.data.length / this.itemsPerPage);
  }

  get paginatedData(): any[] {
    if (!this.showPagination) return this.data;
    
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.data.slice(startIndex, endIndex);
  }

  get pages(): number[] {
    const pages = [];
    const start = Math.max(1, this.currentPage - 2);
    const end = Math.min(this.totalPages, start + 4);
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }

  onSort(column: DataTableColumn): void {
    if (!column.sortable) return;
    
    if (this.sortColumn === column.key) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column.key;
      this.sortDirection = 'asc';
    }
    
    this.sortChange.emit({
      column: column.key,
      direction: this.sortDirection
    });
  }

  onActionClick(action: DataTableAction, item: any): void {
    this.actionClick.emit({
      action: action.action,
      item: item
    });
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
      this.currentPage = page;
    }
  }

  goToFirst(): void {
    this.goToPage(1);
  }

  goToPrevious(): void {
    this.goToPage(this.currentPage - 1);
  }

  goToNext(): void {
    this.goToPage(this.currentPage + 1);
  }

  goToLast(): void {
    this.goToPage(this.totalPages);
  }

  getCellValue(item: any, column: DataTableColumn): any {
    const keys = column.key.split('.');
    let value = item;
    
    for (const key of keys) {
      value = value?.[key];
    }
    
    return value;
  }

  getBadgeConfig(column: DataTableColumn, value: any): any {
    return column.badgeConfig?.[value] || { class: 'badge-default', label: value };
  }

  get Math() {
    return Math;
  }
}
