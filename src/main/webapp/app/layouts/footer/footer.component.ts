/* eslint-disable */
import { Component } from '@angular/core';
import { TranslateDirective } from 'app/shared/language';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styles: [
    `
      .app-footer {
        background-color: #f0f2f5; /* Light gray to match NG-Zorro style */
        padding: 20px;
        text-align: center;
        font-size: 14px;
        color: #595959; /* Slightly muted color for a softer appearance */

        .footer-content {
          max-width: 800px;
          margin: 0 auto;
        }

        p {
          margin: 5px 0;
        }
      }
    `,
  ],

  imports: [TranslateDirective],
})
export default class FooterComponent {}
