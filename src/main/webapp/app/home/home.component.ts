/* eslint-disable */
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { NzLayoutModule } from 'ng-zorro-antd/layout';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { Splind } from 'app/components/splind-card/splind';
import { SplindCardComponent } from 'app/components/splind-card/splind-card.component';
import { ModalService } from 'app/services/modal.service';
import { ModalFormComponent } from 'app/components/modal-form/modal-form.component';
import { SplendsViewComponent } from '../public/components/splends-view/splends-view.component';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',

  imports: [SharedModule, RouterModule, SplindCardComponent, ModalFormComponent, SplendsViewComponent],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  ideas: Splind[] = [
    {
      id: 1,
      title: 'Smart Home Automation System',
      description:
        'A system that uses AI to learn user preferences and automatically adjust home settings like temperature, lighting, and security.',
      author: 'John Doe',
      likes: 42,
      comments: 15,
      tags: ['AI', 'IoT', 'Smart Home'],
      createdAt: new Date(),
    },
    {
      id: 2,
      title: 'Sustainable Food Packaging',
      description: 'Biodegradable packaging solution made from agricultural waste that can completely decompose within 30 days.',
      author: 'Jane Smith',
      likes: 38,
      comments: 21,
      tags: ['Sustainability', 'Innovation', 'Green'],
      createdAt: new Date(),
    },
  ];
  private readonly destroy$ = new Subject<void>();

  private accountService = inject(AccountService);
  private router = inject(Router);
  private modalService = inject(ModalService);
  constructor() {}
  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  showAddIdeaModal(): void {
    // create and show modal for adding new idea
    this.modalService.showModal(
      `

    this is modal

    `,
      'HTML Content Preview',
    );
  }

  showModal(formId: string): void {}
}
