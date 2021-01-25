import { LitElement } from 'lit-element';
/**
 * Returns a promise when all components have completed rendering one cycle.
 */
export declare function childrenUpdateComplete(elements: LitElement[] | NodeListOf<LitElement>): Promise<unknown[]>;
/**
 * Set all properties that are in common with a parent group and child components.
 * This is helpful when providing convenience group/wrapper components that set the state
 * of child slotted elements (cds-form-group, cds-control-group). Typically call
 * during the `firstUpdated` or `updated` lifecycle.
 */
export declare function syncDefinedProps(props: Map<string, any>, source: {
    [prop: string]: any;
}, targets: {
    [prop: string]: any;
}[]): void;
/**
 * Set all common properties between two instances with given conditions. This is
 * helpful for setting child component properties from the parent given certain
 * conditions.
 */
export declare function syncProps(target: {
    [prop: string]: any;
}, source: {
    [prop: string]: any;
}, conditions: {
    [prop: string]: boolean;
}): void;
