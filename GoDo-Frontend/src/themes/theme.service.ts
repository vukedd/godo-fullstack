import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private darkMode = new BehaviorSubject<boolean>(false);
  darkMode$ = this.darkMode.asObservable();

  constructor() {
    if (typeof window !== 'undefined' && window.localStorage) {
      const savedTheme = localStorage.getItem('theme');
      if (savedTheme) {
        this.setDarkMode(savedTheme === 'dark');
      } else {
        const prefersDark = window.matchMedia(
          '(prefers-color-scheme: dark)'
        ).matches;
        this.setDarkMode(prefersDark);
      }
    }
  }

  setDarkMode(isDark: boolean) {
    const html = document.querySelector('html');
    if (isDark) {
      html?.classList.add('dark-mode');
    } else {
      html?.classList.remove('dark-mode');
    }
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    this.darkMode.next(isDark);
  }

  toggleDarkMode() {
    this.setDarkMode(!this.darkMode.value);
  }

  isDarkMode() {
    return this.darkMode;
  }
}
