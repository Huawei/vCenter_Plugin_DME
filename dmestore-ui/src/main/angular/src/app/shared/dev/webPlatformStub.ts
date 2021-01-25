import { ClientSdk, ClientInfo, NavConfig, ModalConfig, WebPlatform, UserSession
}  from "../vSphereClientSdkTypes";

export const globalStubs = {
   actionDevService: null
};

export const clientSdkStub: ClientSdk = {
   app: {
      getContextObjects(): any {
         return null;
      },
      navigateTo(config: NavConfig): void {
         console.log("navigateTo called");
      },
      getNavigationData(): any {
         return null;
      },
      getClientInfo(): ClientInfo {
         return {type: "html", version: "6.7.0.300"};
      },
      getClientLocale(): string {
         return "en-US";
      }
   },
   modal: {
      open(config: ModalConfig): void { },
      close(data: any): void { },
      setOptions(config: any): void { },
      getCustomData(): any { return null; }
   },
   event: {
      onGlobalRefresh(callback): void { }
   }
};

/**
 * The WebPlatform stub used for dev mode and unit testing
 */
export const webPlatformStub: WebPlatform = {

   callActionsController(url: string, jsonData: string, targets?: string): void {
      console.log("callActionsController called: url = " + url + ', jsonData = ' + jsonData);
      globalStubs.actionDevService.callActionsController(url, jsonData, targets);
   },
   closeDialog(): void {
      console.log("closeDialog called");
   },
   getClientVersion(): string { return "6.5.0"; },
   getClientType(): string { return "html"; },

   getRootPath(): string {
      // Angular CLI proxy in dev mode:
      //  => Use the same port 4201 defined in package.json
      return "http://localhost:4201/ui";
   },

   getString(bundleName: string, key: string, params: any): string { return ""; },
   getUserSession(): UserSession { return null; },
   openModalDialog(): void {
      console.log("openModalDialog called");
   },
   sendModelChangeEvent(): void {
      console.log("sendModelChangeEvent called");
   },
   sendNavigationRequest(): void {
      console.log("sendNavigationRequest called");
   },
   setDialogSize(width: string, height: string): void {
      console.log("setDialogSize called: width = " + width + "height = " + height);
   },
   setDialogTitle(title: string): void {
      console.log("setDialogTitle called: title = " + title);
   },
   setGlobalRefreshHandler(callback, document): void {
      console.log("setGlobalRefreshHandler called");
   }
};



