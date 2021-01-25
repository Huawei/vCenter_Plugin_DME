export declare function transformToString(delimiter: string, fns: any[], ...args: any[]): string;
export declare function transformToSpacedString(fns: any[], ...args: any[]): string;
export declare function transformToUnspacedString(fns: any[], ...args: any[]): string;
export declare function camelCaseToKebabCase(value: string): string;
export declare function kebabCaseToCamelCase(str: string): string;
export declare function kebabCaseToPascalCase(string: string): string;
/**
 * Take a object map of css properties and if value concatenate string of all computed values
 * Useful for dynamic style tags in lit-html templates
 */
export declare function setStyles(styles: {
    [key: string]: string;
}): string;
export declare function setPropStyles(styles: {
    [key: string]: string;
}): string;
export declare function capitalizeFirstLetter(string: string): string;
/** Used for Storybook docs to define knob group for css properties */
export declare const cssGroup = "CSS Custom Properties";
/** Used for Storybook docs to define knob group for JavaScript properties */
export declare const propertiesGroup = "Default Properties";
