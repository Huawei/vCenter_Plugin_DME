/*
 * Copyright (c) 2016-2020 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
export class EventEmitter {
    constructor(target, eventName) {
        this.target = target;
        this.eventName = eventName;
    }
    emit(value, options) {
        this.target.dispatchEvent(new CustomEvent(this.eventName, Object.assign({ detail: value }, options)));
    }
}
// Legacy TS Decorator
function legacyEvent(descriptor, protoOrDescriptor, name) {
    Object.defineProperty(protoOrDescriptor, name, descriptor);
}
// TC39 Decorators proposal
function standardEvent(descriptor, element) {
    return {
        kind: 'method',
        placement: 'prototype',
        key: element.key,
        descriptor,
    };
}
export function event() {
    return (protoOrDescriptor, name) => {
        const descriptor = {
            get() {
                return new EventEmitter(this, name !== undefined ? name : protoOrDescriptor.key);
            },
            enumerable: true,
            configurable: true,
        };
        return name !== undefined
            ? legacyEvent(descriptor, protoOrDescriptor, name)
            : standardEvent(descriptor, protoOrDescriptor);
    };
}
//# sourceMappingURL=event.js.map