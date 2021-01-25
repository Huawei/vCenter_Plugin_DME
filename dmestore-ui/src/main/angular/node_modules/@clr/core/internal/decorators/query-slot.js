/*
 * Copyright (c) 2016-2020 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
import { LogService } from '../services/log.service.js';
// Slot Query decorators are similar to the query decorator in lit-element.
// Instead of querying the component template they query the content slot of the component.
const legacyQuery = (descriptor, proto, name) => {
    Object.defineProperty(proto, name, descriptor);
};
const standardQuery = (descriptor, element) => ({
    kind: 'method',
    placement: 'prototype',
    key: element.key,
    descriptor,
});
/**
 * A property decorator that converts a class property into a getter that
 * executes a querySelector on the element's light DOM Slot.
 *
 * @ExportDecoratedItems
 */
export function querySlot(selector, config) {
    return (protoOrDescriptor, name) => {
        const targetFirstUpdated = protoOrDescriptor.firstUpdated;
        function firstUpdated() {
            const ref = this.querySelector(selector);
            if (!ref && (config === null || config === void 0 ? void 0 : config.required)) {
                const message = config.requiredMessage ||
                    `The <${selector}> element is required to use <${this.tagName.toLocaleLowerCase()}>`;
                if (config.required === 'error') {
                    throw new Error(message);
                }
                else {
                    LogService.warn(message, this);
                }
            }
            if ((config === null || config === void 0 ? void 0 : config.assign) && (ref === null || ref === void 0 ? void 0 : ref.hasAttribute('slot')) === false) {
                ref.setAttribute('slot', config.assign);
            }
            if (targetFirstUpdated) {
                targetFirstUpdated.apply(this);
            }
        }
        protoOrDescriptor.firstUpdated = firstUpdated;
        const descriptor = {
            get() {
                return this.querySelector(selector);
            },
            enumerable: true,
            configurable: true,
        };
        return name !== undefined
            ? legacyQuery(descriptor, protoOrDescriptor, name)
            : standardQuery(descriptor, protoOrDescriptor);
    };
}
/**
 * A property decorator that converts a class property into a getter
 * that executes a querySelectorAll on the element's light DOM Slot.
 *
 * @ExportDecoratedItems
 */
export function querySlotAll(selector, config) {
    return (protoOrDescriptor, name) => {
        const targetFirstUpdated = protoOrDescriptor.firstUpdated;
        function firstUpdated(props) {
            if (config === null || config === void 0 ? void 0 : config.assign) {
                Array.from(this.querySelectorAll(selector))
                    .filter((i) => !i.hasAttribute('slot'))
                    .forEach((i) => i.setAttribute('slot', config.assign));
            }
            if (targetFirstUpdated) {
                targetFirstUpdated.apply(this, [props]);
            }
        }
        protoOrDescriptor.firstUpdated = firstUpdated;
        const descriptor = {
            get() {
                return this.querySelectorAll(selector);
            },
            enumerable: true,
            configurable: true,
        };
        return name !== undefined
            ? legacyQuery(descriptor, protoOrDescriptor, name)
            : standardQuery(descriptor, protoOrDescriptor);
    };
}
//# sourceMappingURL=query-slot.js.map