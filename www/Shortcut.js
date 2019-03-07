const PLUGIN_NAME = 'Shortcut';

exports.createShortcut = function (shortcutText) {
    if (shortcutText === void 0 || shortcutText === null) {
        shortcutText = '';
        return Promise.reject(5)
    }

    return new Promise(function (resolve, reject) {
        window.cordova.exec(
            function (args) {
                resolve(args);
            },
            function (args) {
                reject(args);
            },
            PLUGIN_NAME,
            'createShortcut',
            [shortcutText]
        );
    });
}

exports.removeShortcut = function (shortcutText) {
    if (shortcutText === void 0 || shortcutText === null) {
        shortcutText = '';
        return Promise.reject(5)
    }

    return new Promise(function (resolve, reject) {
        window.cordova.exec(
            function (args) {
                resolve(args);
            },
            function (args) {
                reject(args);
            },
            PLUGIN_NAME,
            'removeShortcut',
            [shortcutText]
        );
    });
}

// Copyright 2013 Jorge Cisneros jorgecis@gmail.com

// var Shortcut = function () { };

// Shortcut.prototype.CreateShortcut = function (shortcut_text, successCallback, errorCallback) {
//     cordova.exec(
//         successCallback,
//         errorCallback,
//         'ShortcutPlugin',
//         'addShortcut',
//         [{
//             "shortcuttext": shortcut_text
//         }]
//     );
// };
// Shortcut.prototype.RemoveShortcut = function (shortcut_text, successCallback, errorCallback) {
//     cordova.exec(
//         successCallback,
//         errorCallback,
//         'ShortcutPlugin',
//         'delShortcut',
//         [{
//             "shortcuttext": shortcut_text
//         }]
//     );
// };

// if (!window.plugins) {
//     window.plugins = {};
// }
// if (!window.plugins.Shortcut) {
//     window.plugins.Shortcut = new Shortcut();
// }
