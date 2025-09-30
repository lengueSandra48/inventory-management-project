import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal-confirmation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal-confirmation.html',
  styleUrl: './modal-confirmation.css'
})
export class ModalConfirmation {
  @Input() isVisible = false;
  @Input() message = '';
  @Input() title = 'Confirmation de suppression';
  
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  onConfirm(): void {
    this.confirmed.emit();
    this.isVisible = false;
  }

  onCancel(): void {
    this.cancelled.emit();
    this.isVisible = false;
  }

  onOverlayClick(event: Event): void {
    if (event.target === event.currentTarget) {
      this.onCancel();
    }
  }
}
