const { VITE_API_BASE_URL, VITE_BASE_URL, ...otherViteConfig } = import.meta.env;

export const Env = {
    API_BASE_URL: VITE_API_BASE_URL as string,
    BASE_URL: VITE_BASE_URL as string,
    __vite__: otherViteConfig,
};