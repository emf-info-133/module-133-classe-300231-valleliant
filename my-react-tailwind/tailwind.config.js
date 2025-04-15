/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}", // Scanne les fichiers JS/JSX/TS/TSX dans src
    "./public/index.html",        // Scanne index.html
  ],
  theme: {
    extend: {
      // Vous pouvez étendre le thème par défaut ici
      // Exemple : ajouter des couleurs personnalisées
      colors: {
        'brand-primary': '#6366F1', // indigo-500
        'brand-secondary': '#F59E0B', // amber-500
      },
      // Exemple : ajouter des fontes personnalisées (si configurées dans index.css)
      // fontFamily: {
      //   sans: ['Inter', 'sans-serif'],
      // },
    },
  },
  plugins: [
    // Ajoutez des plugins Tailwind si nécessaire (ex: @tailwindcss/forms)
    // require('@tailwindcss/forms'),
  ],
} 