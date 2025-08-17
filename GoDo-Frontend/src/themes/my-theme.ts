import { definePreset } from '@primeng/themes';
import Aura from '@primeng/themes/aura';

export const MyTheme = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#f1f5f0',
      100: '#dce6da',
      200: '#c6d6c4',
      300: '#b1c7ae',
      400: '#9cb998',
      500: '#88aa83',
      600: '#7b9b75',
      700: '#6e8c67',
      800: '#617d59',
      900: '#526e4a',
      950: '#374d32',
    },
  },
});
