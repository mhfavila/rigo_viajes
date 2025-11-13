/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    // Esta es la línea más importante.
    // Le dice a Tailwind que escanee todos los archivos .html y .ts
    // en busca de clases como 'bg-white', 'grid', etc.
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
