/*
 * Copyright (c) 2016-2020 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
import { registerElementSafely } from './../utils/register.js';
/**
 * @experimental
 * experimental decorator, waiting on Angular issue to be resolved https://github.com/angular/angular/issues/31495
 * Class decorator factory that defines the decorated class as a custom element.
 *
 * ```
 * @customElement('my-element')
 * class MyElement {
 *   render() {
 *     return html``;
 *   }
 * }
 * ```
 */
// TC39 Decorators proposal
const standardCustomElement = (tagName, descriptor) => {
    const { kind, elements } = descriptor;
    return {
        kind,
        elements,
        finisher(classDef) {
            registerElementSafely(tagName, classDef);
        },
    };
};
// Legacy TS Decorator
const legacyCustomElement = (tagName, classDef) => {
    registerElementSafely(tagName, classDef);
    return classDef;
};
export const customElement = (tagName) => (classOrDescriptor) => {
    return typeof classOrDescriptor === 'function'
        ? legacyCustomElement(tagName, classOrDescriptor)
        : standardCustomElement(tagName, classOrDescriptor);
};
//# sourceMappingURL=element.js.map