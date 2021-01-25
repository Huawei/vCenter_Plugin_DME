/**
 * keyWasEvented() checks to see if a given key is part of any KeyboardEvent it is passed.
 */
export declare function keyWasEvented(evt: KeyboardEvent, whichKey: string): boolean;
/**
 * onKey() takes a single key and fires a handler if that key is part of
 * the KeyboardEvent it was passed.
 */
export declare function onKey(whichKey: string, evt: KeyboardEvent, handler: any): void;
/**
 * onAnyKey() takes an array of keys and fires a handler if any of the keys are part of
 * the KeyboardEvent it was passed.
 */
export declare function onAnyKey(whichKeys: string[], evt: KeyboardEvent, handler: any): void;
/**
 * onKeyCombo() takes a string representation of a combination of keys and modifier keys such as
 * 'ctrl+shift+a'.
 *
 * onKeyCombo() does not make accommodation for the '+' symbol in a key combo. Consider using 'ctrl+shift+='.
 *
 * onKeyCombo() accounts for 'cmd', 'win', and 'meta' keys inside keycombos. 'cmd+K', 'win+K', and 'meta+K'
 * are all the same thing to onKeyCombo().
 */
export declare function onKeyCombo(whichKeyCombo: string, evt: KeyboardEvent, handler: any): void;
/**
 * getModifierKeysFromKeyCombo() takes a keycombo string and returns an array with all of the
 * special keys in the keycombo.
 */
export declare function getModifierKeysFromKeyCombo(keyCombo: string): string[];
/**
 * removeModifierKeysFromKeyCombo() takes a keycombo string and returns an array with all of the
 * non-special keys in the keycombo.
 */
export declare function removeModifierKeysFromKeyCombo(keyCombo: string): string[];
