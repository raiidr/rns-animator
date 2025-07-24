// rns-animator\plugin\index.js
const {
    withMainApplication,
    withAndroidManifest,
    withProjectBuildGradle,
} = require('@expo/config-plugins');

module.exports = function withRNSAnimator(config) {
    // 1. Inject the package import & registration
    config = withMainApplication(config, async config => {
        const contents = config.modResults.contents;

        // 1a. Add import
        const importStatement = `import com.rnsanimator.www.LayoutAnimatorPackage;`;
        if (!contents.includes(importStatement)) {
            config.modResults.contents = contents.replace(
                /package\s+[\w.]+;\r?\n/,
                `&\n${importStatement}`
            );
        }

        // 1b. Register the package in getPackages()
        config.modResults.contents = config.modResults.contents.replace(
            /packages = PackageList\(this\)\.packages;/,
            `packages = PackageList(this).packages;\n    packages.add(new LayoutAnimatorPackage());`
        );

        return config;
    });

    // 2. (Optional) Ensure your android/build.gradle has react-native plugin
    config = withProjectBuildGradle(config, config => {
        // You can check and inject `apply plugin: 'com.facebook.react'` etc.
        return config;
    });

    return config;
};
