/**
 * Interface with the vSphere Client SDK 6.7 Javascript APIs
 */
export interface ClientSdk {
   app: {
      getContextObjects(): any;
      navigateTo(config: NavConfig): void;
      getNavigationData(): any;
      getClientInfo(): ClientInfo;
      getClientLocale(): string;
   };
   modal: {
      open(config: ModalConfig): void;
      close(data?: any): void;
      setOptions(config: any): void;
      getCustomData(): any;
   };
   event: {
      onGlobalRefresh(callback): void;
   };
}

export class NavConfig {
   constructor(public targetViewId: string, public objectId?: string, public customData?: any) {
   }
}

export class ModalConfig {
   constructor(
      public url: string,
      public title?: string,
      public size?: { width: number; height: number; },
      public closable?: boolean,
      public onClosed?: ((result?: any) => void),
      public contextObjects?: any[],
      public customData?: any
   ) { }
}

export class ClientInfo {
   type: string;
   version: string;
}

/**
 * Interface with the vSphere Client SDK 6.5 Javascript APIs
 */
export interface WebPlatform {
   callActionsController(url: string, jsonData: string, targets?: string): void;
   closeDialog(): void;
   getClientType(): string;
   getClientVersion(): string;
   getString(bundleName: string, key: string, params: any): string;
   getRootPath(): string;
   getUserSession(): UserSession;
   openModalDialog(title, url, width, height, objectId): void;
   sendModelChangeEvent(objectId, opType): void;
   sendNavigationRequest(targetViewId, objectId): void;
   setDialogSize(width, height): void;
   setDialogTitle(title): void;
   setGlobalRefreshHandler(callback, document): void;
}

export class UserSession {
   userName: string;
   clientId: string;
   locale: string;
   serversInfo: ServerInfo[];
}

export class ServerInfo {
   serviceGuid: string;
   serviceUrl: string;
   sessionKey: string;
}
