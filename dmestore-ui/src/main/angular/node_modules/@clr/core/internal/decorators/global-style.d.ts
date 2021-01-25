/**
 * Appends a global `<style>` tag to the light DOM of a given custom element.
 * This is useful for when styles need to be applied that are not supported
 * withing a `::slotted()` selector such as ::-vendor style selectors.
 */
export declare function globalStyle(): (protoOrDescriptor: any, name: string) => any;
