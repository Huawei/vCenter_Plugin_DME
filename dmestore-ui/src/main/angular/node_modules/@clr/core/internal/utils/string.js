/*
 * Copyright (c) 2016-2020 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
export function transformToString(delimiter, fns, ...args) {
    return fns
        .map(fn => {
        return fn(...args);
    })
        .join(delimiter)
        .trim();
}
// have to go this route because ramda curry throws typescript for loops
export function transformToSpacedString(fns, ...args) {
    return transformToString(' ', fns, ...args);
}
export function transformToUnspacedString(fns, ...args) {
    return transformToString('', fns, ...args);
}
export function camelCaseToKebabCase(value) {
    return value.replace(/[A-Z]/g, l => `-${l.toLowerCase()}`);
}
export function kebabCaseToCamelCase(str) {
    return str
        .split('-')
        .map((item, index) => (index ? item.charAt(0).toUpperCase() + item.slice(1).toLowerCase() : item))
        .join('');
}
export function kebabCaseToPascalCase(string) {
    const camelCase = kebabCaseToCamelCase(string);
    return capitalizeFirstLetter(camelCase);
}
/**
 * Take a object map of css properties and if value concatenate string of all computed values
 * Useful for dynamic style tags in lit-html templates
 */
export function setStyles(styles) {
    return createPropStyleSelectors(Object.keys(styles), styles);
}
export function setPropStyles(styles) {
    return createPropStyleSelectors(Object.keys(styles).filter(prop => prop.startsWith('--')), styles);
}
function createPropStyleSelectors(props, styles) {
    return props.reduce((allStyles, prop) => `${allStyles}${styles[prop] ? `${prop}:${styles[prop]};` : ''}`, '');
}
export function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
/** Used for Storybook docs to define knob group for css properties */
export const cssGroup = 'CSS Custom Properties';
/** Used for Storybook docs to define knob group for JavaScript properties */
export const propertiesGroup = 'Default Properties';
//# sourceMappingURL=string.js.map