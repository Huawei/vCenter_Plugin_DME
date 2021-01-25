/*
 * Copyright (c) 2016-2020 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
export function syncHTML5Validation(control, messages) {
    messages
        .filter(m => m.hasAttribute('error'))
        .forEach(m => {
        m.setAttribute('hidden', '');
        m.status = 'error';
    });
    control.inputControl.addEventListener('blur', () => control.inputControl.checkValidity());
    control.inputControl.addEventListener('invalid', () => {
        var _a;
        messages.forEach(message => message.setAttribute('hidden', ''));
        (_a = messages.find(message => control.inputControl.validity[message.error])) === null || _a === void 0 ? void 0 : _a.removeAttribute('hidden');
        control.status = 'error';
    });
    control.inputControl.addEventListener('input', () => {
        control.status = control.inputControl.validity.valid ? 'neutral' : control.status;
        messages
            .filter(m => control.inputControl.validity.valid && m.error && !control.inputControl.validity[m.error])
            .forEach(message => message.setAttribute('hidden', ''));
    });
}
//# sourceMappingURL=validate.js.map