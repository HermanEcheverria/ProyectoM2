// eslint.config.js
import vue from 'eslint-plugin-vue'
import tseslint from '@typescript-eslint/eslint-plugin'
import tsParser from '@typescript-eslint/parser'

export default [
  // Ignora artefactos y el backend
  {
    ignores: [
      '**/node_modules/**',
      '**/dist/**',
      '**/coverage/**',
      'backend/**',
      '**/target/**',
      'deploy/**'
    ]
  },

  // Reglas recomendadas de Vue (ya configuran vue-eslint-parser para .vue)
  ...vue.configs['flat/recommended'],

  // Reglas para TypeScript (sin type-check pesado)
  {
    files: ['**/*.ts'],
    languageOptions: {
      parser: tsParser,
      parserOptions: { sourceType: 'module', ecmaVersion: 'latest' }
    },
    plugins: { '@typescript-eslint': tseslint },
    rules: {
      '@typescript-eslint/no-unused-vars': ['warn', { argsIgnorePattern: '^_', varsIgnorePattern: '^_' }]
    }
  },

  // Ajustes específicos de Vue para bajar el ruido en PR
  {
    files: ['**/*.vue'],
    rules: {
      // Evita 50+ errores por <script> sin lang
      'vue/block-lang': 'off',
      // Permite nombres de 1 palabra (Login, Draft, etc.)
      'vue/multi-word-component-names': 'off',
      // Mantén como error lo que sí es crítico
      'vue/no-dupe-keys': 'error'
    }
  }
]
