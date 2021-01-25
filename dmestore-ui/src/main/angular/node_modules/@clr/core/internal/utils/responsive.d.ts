/// <reference types="@types/resize-observer-browser" />
import { LitElement } from 'lit-element';
export declare type ResponsiveComponent = LitElement & {
    layout: string;
    responsive: boolean;
    layoutStable: boolean;
};
export interface LayoutConfig {
    layouts: string[];
    initialLayout: string;
}
export declare function elementResize(element: HTMLElement, callbackFn: () => void): ResizeObserver;
/**
 * Given a ResponsiveComponent this function will loop through a list of layout
 * options and change the layout of the component until the components layout
 * condition is satisfied.
 */
export declare function updateComponentLayout(component: ResponsiveComponent, layoutConfig: LayoutConfig, fn: () => void): ResizeObserver;
