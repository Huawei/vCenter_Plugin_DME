/*
 * Copyright (c) 2016-2020 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
import { createId } from './../utils/identity.js';
const legacyId = (descriptor, proto, name) => {
    Object.defineProperty(proto, name, descriptor);
};
const standardId = (descriptor, element) => ({
    kind: 'method',
    placement: 'prototype',
    key: element.key,
    descriptor,
});
export function id() {
    return (protoOrDescriptor, name) => {
        const descriptor = {
            get() {
                const propertyName = name !== undefined ? name : protoOrDescriptor.key;
                if (!this[`__${propertyName}`]) {
                    // _ is used to ensure number is not first since this can create an invalid css selector
                    this[`__${propertyName}`] = createId();
                }
                return this[`__${propertyName}`];
            },
            enumerable: true,
            configurable: true,
        };
        return name !== undefined
            ? legacyId(descriptor, protoOrDescriptor, name)
            : standardId(descriptor, protoOrDescriptor);
    };
}
//# sourceMappingURL=id.js.map