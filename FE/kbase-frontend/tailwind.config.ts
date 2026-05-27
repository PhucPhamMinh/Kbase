import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#172026",
        line: "#d8dee4",
        panel: "#f7f9fb",
        brand: "#256f68",
        accent: "#b75d3b"
      }
    }
  },
  plugins: []
};

export default config;
