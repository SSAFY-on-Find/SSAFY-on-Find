import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'
import simpleImportSort from 'eslint-plugin-simple-import-sort'
import noRelativeImportPaths from 'eslint-plugin-no-relative-import-paths'

export default tseslint.config(
  { ignores: ['dist'] },
  {
    extends: [js.configs.recommended, ...tseslint.configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      'simple-import-sort': simpleImportSort,
      'no-relative-import-paths': noRelativeImportPaths,
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
      
      // 자동 수정 가능한 기본 규칙들
      'prefer-const': 'error',
      'no-extra-semi': 'error',
      'no-lone-blocks': 'error',
      'eqeqeq': 'error',
      'no-else-return': 'error',
      '@typescript-eslint/no-unused-vars': 'warn',
      
      // Import 자동 정렬
      'simple-import-sort/imports': [
        'error',
        {
          groups: [
            ['^node:'],
            ['^react', '^@?\\w'],
            ['^@/'],
            ['^\\.\\.(?!/?$)', '^\\.\\./?$'],
            ['^\\./'],
            ['^.+\\.(css|scss|sass)$'],
          ],
        },
      ],
      'simple-import-sort/exports': 'error',
      
      // 상대경로를 @ 절대경로로 자동 변환
      'no-relative-import-paths/no-relative-import-paths': [
        'error',
        { 
          allowSameFolder: true,  // 같은 폴더 ./는 허용
          rootDir: 'src',
          prefix: '@'
        }
      ],
    },
  },
)