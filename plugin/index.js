/// rns-animator\plugin\index.js
const { withMainApplication } = require('@expo/config-plugins');

module.exports = function withRNSAnimator(config) {
    return withMainApplication(config, (mod) => {
        // Inject import if not already present
        if (!mod.contents.includes('import com.rnsanimator.www.LayoutAnimatorPackage')) {
            mod.contents = mod.contents.replace(
                /package .*?\n/,
                match => match + 'import com.rnsanimator.www.LayoutAnimatorPackage;\n'
            );
        }

        // Inject package in getPackages()
        if (!mod.contents.includes('new LayoutAnimatorPackage()')) {
            mod.contents = mod.contents.replace(
                /val packages = PackageList\(this\)\.packages([\s\S]*?)return packages;/,
                (match, inner) => {
                    return `val packages = PackageList(this).packages${inner}    packages.add(LayoutAnimatorPackage())\n    return packages;`;
                }
            );
        }

        return mod;
    });
};
