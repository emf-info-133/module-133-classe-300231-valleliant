/**
 * Script pour corriger les problèmes de chemin dans WSL pour React Scripts
 */
const fs = require('fs');
const path = require('path');

// Définir correctement l'environnement comme si nous étions dans un environnement Linux standard
process.env.APPDATA = path.join(__dirname, '.cache');
process.env.NODE_ENV = process.env.NODE_ENV || 'development';

// Correction pour le problème de react-scripts cherchant package.json dans C:\Windows\
const originalResolveFilename = module.constructor._resolveFilename;
module.constructor._resolveFilename = function(request, parent, isMain, options) {
  if (request.includes('package.json') && parent && parent.filename && parent.filename.includes('react-scripts')) {
    const projectPackageJson = path.join(__dirname, 'package.json');
    if (fs.existsSync(projectPackageJson)) {
      return projectPackageJson;
    }
  }
  return originalResolveFilename(request, parent, isMain, options);
};

// Exécuter react-scripts
require('react-scripts/bin/react-scripts'); 