// rns-animator/plugin/index.js
const { withMainApplication, withProjectBuildGradle } = require('@expo/config-plugins');

module.exports = function withRNSAnimator(config) {
    // 1. Inject import and package registration into MainApplication.kt
    config = withMainApplication(config, config => {
        let contents = config.modResults.contents;

        // ✅ Add import if not present
        const importStatement = 'import com.rnsanimator.www.LayoutAnimatorPackage';
        if (!contents.includes(importStatement)) {
            contents = contents.replace(
                /package\s+[\w.]+;\s*/,
                match => `${match}\n${importStatement}\n`
            );
        }

        // ✅ Inject packages.add(LayoutAnimatorPackage()) if not already present
        if (!contents.includes('LayoutAnimatorPackage()')) {
            contents = contents.replace(
                /val packages = PackageList\(this\)\.packages([\s\S]*?)return packages;/,
                match => {
                    return match.replace(
                        'return packages;',
                        'packages.add(LayoutAnimatorPackage())\n    return packages;'
                    );
                }
            );
        }

        config.modResults.contents = contents;
        return config;
    });

    // Optional - modify android/build.gradle
    config = withProjectBuildGradle(config, config => {
        // Add logic if needed (currently empty)
        return config;
    });

    return config;
};
