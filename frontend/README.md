# Frontend

![example workflow](https://github.com/RawToast/Kiso/actions/workflows/test.yml/badge.svg)

## Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/en/) (>=20.0.0)
- [Bun](https://bun.sh/) (>=1.0.0)

### Quick Start

- Install dependencies with `bun install`
- Copy `.env.example` to `.env` and update any values
- Start the development server with `bun vite`.

### Other Commands

- `bun build` - Build the application for production
- `bun lint` - Run ESLint
- `bun format` - Run Prettier
- `bun test` - Run Jest tests

### VS Code Setup

To view dependencies in the editor install the [ZipFS extension](https://marketplace.visualstudio.com/items?itemName=arcanis.vscode-zipfs)

You can setup VS Code to use Prettier for formatting by installing the [Prettier extension](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode)

### Formatting

- Install the Prettier-Standard plugin: <cmd/ctrl + p> `ext install prettier-standard-vscode`
- Right click in any typescript file and select `Format Document With...`
- Select `Configure Default Formatter...`
- Select `prettier-standard-vscode`

### Local CI

The github actions can be ran locally using [Act](https://github.com/nektos/act)

The following command will run the github action locally, which is useful for debugging or testing new configuration: `act -P ubuntu-22.04=cypress/included:12.17.3`

### Updating Components

The following will update all installed components to the latest version

`for file in src/components/ui/*.tsx; do bunx shadcn-ui@latest add -y -o $(basename "$file" .tsx); done`

## Cool Stuff to Check Out

- [React Router](https://reactrouter.com/)
- [React Query](https://react-query.tanstack.com/)
- [React Hook Form](https://react-hook-form.com/)
- [React Hot Toast](https://react-hot-toast.com/)
- [React Spinners](https://www.davidhu.io/react-spinners/)
- [React Use](https://github.com/streamich/react-use)
- [React Select](https://react-select.com/home)
- [Zustand](https://github.com/pmndrs/zustand)
- [Zod](https://zod.dev)
- [Tailwind CSS](https://tailwindcss.com/)
- [Heroicons](https://heroicons.com/)
- [React Icons](https://react-icons.github.io/react-icons/)
- [Vitest](https://vitest.dev)
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)

# Old Readme

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type aware lint rules:

- Configure the top-level `parserOptions` property like this:

```js
   parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
    project: ['./tsconfig.json', './tsconfig.node.json'],
    tsconfigRootDir: __dirname,
   },
```

- Replace `plugin:@typescript-eslint/recommended` to `plugin:@typescript-eslint/recommended-type-checked` or `plugin:@typescript-eslint/strict-type-checked`
- Optionally add `plugin:@typescript-eslint/stylistic-type-checked`
- Install [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react) and add `plugin:react/recommended` & `plugin:react/jsx-runtime` to the `extends` list
