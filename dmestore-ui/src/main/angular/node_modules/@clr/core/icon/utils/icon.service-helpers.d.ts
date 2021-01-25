import { IconAlias, IconAliasLegacyObject, IconRegistry, IconShapeTuple } from '../interfaces/icon.interfaces.js';
export declare function addIcons(shapes: IconShapeTuple[], registry: IconRegistry): void;
export declare function hasIcon(shapeName: string, registry: IconRegistry): boolean;
export declare function getIcon(shapeName: string, registry: IconRegistry): string;
export declare function addIcon(shape: IconShapeTuple, registry: IconRegistry): void;
export declare function setIconAlias(shapeName: string, aliasName: string, registry: IconRegistry): void;
export declare function setIconAliases(iconAlias: IconAlias, registry: IconRegistry): void;
export declare function legacyAlias(aliases: IconAliasLegacyObject, registry: IconRegistry): void;
