declare interface ShortcutIterface {
    createShortcut: (name:string) => Promise<number>;
    removeShortcut: (name:string) => Promise<number>;
}

interface Window {
    Shortcut: ShortcutIterface;
}