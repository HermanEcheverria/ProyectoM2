/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_URL: string; // agrega todas tus variables VITE_ aquí
  // readonly VITE_OTRA_VARIABLE: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
