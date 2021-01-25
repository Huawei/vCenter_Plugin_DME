export declare const existsIn: (...a: readonly any[]) => any;
export declare function elementExists(tagName: string, registry?: {
    get: (name: string) => {};
}): boolean;
export declare const existsInWindow: any;
export declare function isBrowser(win?: Window & typeof globalThis): boolean;
