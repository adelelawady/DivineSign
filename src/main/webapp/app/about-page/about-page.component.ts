/* eslint-disable */

import { Component } from '@angular/core';
import SharedModule from '../shared/shared.module';

@Component({
  selector: 'jhi-about-page',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './about-page.component.html',
  styleUrl: './about-page.component.scss',
})
export default class AboutPageComponent {
  appVersion = '1.0.0';
  lastUpdated = 'November 10, 2024';
}
