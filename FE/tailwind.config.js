/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
      sans: ['Pretendard', 'sans-serif'],
      },
      colors: {
        main: "#6C5CE7",
        background: "#FAFAFA",
        text: "#1A1A1A",
        subtext: "#A3A3A3",
        line: "#E6E6E6",
        error: "#FF2B37",
        frontend: "#EA580C",
        backend: "#0891B2",
        fullstack: "#F59E0B",
        embedded: "#16A34A",
        infra: "#64748B",
        ai: "#D946EF",
        mobile: "#EC4899",
      },
    },
  },
  plugins: [],
};
