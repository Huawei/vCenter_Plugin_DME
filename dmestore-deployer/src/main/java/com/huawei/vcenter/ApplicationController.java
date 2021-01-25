package com.huawei.vcenter;

import com.huawei.vcenter.utils.Validations;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ApplicationController
 *
 * @author andrewliu
 * @since 2020-12-08
 */
@RestController
public class ApplicationController {
    private static final String ZIP_FILE = ".zip";

    private static final String PACKAGE_URL = "packageUrl";

    private static final String VCENTER_USER_NAME = "vcenterUsername";

    private static final String VCENTER_PASSWORD = "vcenterPassword";

    private static final String VCENTER_IP = "vcenterIP";

    private static final String VCENTER_PORT = "vcenterPort";

    private static final int TWO_BYTE_LEN = 2048;

    /**
     * onload
     *
     * @param request request
     * @param response response
     * @return Map
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @Description: onload.
     */
    @RequestMapping(value = "rest", method = RequestMethod.GET)
    public Map onload(final HttpServletRequest request, final HttpServletResponse response)
        throws UnsupportedEncodingException {
        String rqHd = request.getHeader("Accept-Language");
        if (rqHd != null) {
            String rqHeader = URLEncoder.encode(rqHd, StandardCharsets.UTF_8.displayName());
            response.addHeader("Accept-Language", rqHeader);
        }
        return Validations.onloadChecker(request);
    }

    /**
     * onsubmit
     *
     * @param formData formata
     * @return Map Map
     */
    @RequestMapping(value = "rest", method = RequestMethod.POST)
    public Map onsubmit(final @RequestBody Map<String, String> formData) {
        return Validations.onSubmit(formData.get(PACKAGE_URL), formData.get(VCENTER_USER_NAME),
            formData.get(VCENTER_PASSWORD), formData.get(VCENTER_IP), formData.get(VCENTER_PORT), "", "", "", "", "");
    }

    @RequestMapping(value = "rest/register", method = RequestMethod.POST)
    public Map register(final @RequestBody Map<String, String> formData) {
        return Validations.onSubmit(formData.get(PACKAGE_URL), formData.get(VCENTER_USER_NAME),
            formData.get(VCENTER_PASSWORD), formData.get(VCENTER_IP), formData.get(VCENTER_PORT),
            formData.get("version"), "", "", "", "");
    }

    @RequestMapping(value = "rest/registerByDME", method = RequestMethod.POST)
    public Map registerByDme(final HttpServletRequest request, final @RequestBody Map<String, String> formData) {
        Map map = Validations.onloadChecker(request);
        String username = formData.get(VCENTER_USER_NAME);
        String password = formData.get(VCENTER_PASSWORD);
        String vcenterip = formData.get(VCENTER_IP);
        String vcenterport = formData.get(VCENTER_PORT);
        String dmeIp = formData.get("dmeIP");
        String dmePort = formData.get("dmePort");
        String dmeUsername = formData.get("dmeUsername");
        String dmePassword = formData.get("dmePassword");

        String path = (String) map.get("path");
        List<String> packageNamelist = (List<String>) map.get("packageNameList");
        List<String> versionList = (List<String>) map.get("versionList");
        String packageUrl = path + packageNamelist.get(0);
        String version = versionList.get(0);

        return Validations.onSubmit(packageUrl, username, password, vcenterip, vcenterport, version, dmeIp, dmePort,
            dmeUsername, dmePassword);
    }

    @RequestMapping(value = "rest/unregister", method = RequestMethod.POST)
    public Map unRegister(final @RequestBody Map<String, String> formData) {
        return Validations.unRegister(formData.get(PACKAGE_URL), formData.get(VCENTER_USER_NAME),
            formData.get(VCENTER_PASSWORD), formData.get(VCENTER_IP), formData.get(VCENTER_PORT),
            formData.get("keepData"));
    }

    @RequestMapping(value = "rest/unregisterByDME", method = RequestMethod.POST)
    public Map unregisterByDme(final @RequestBody Map<String, String> formData) {
        return Validations.unRegister("", formData.get(VCENTER_USER_NAME), formData.get(VCENTER_PASSWORD),
            formData.get(VCENTER_IP), formData.get(VCENTER_PORT), formData.get("keepData"));
    }

    @RequestMapping(value = Constants.UPDATE_FILE, method = RequestMethod.GET)
    public void getPackage(final HttpServletResponse response) throws IOException {
        getPackage(response, "dme-vcenter-plugin");
    }

    @RequestMapping(value = "package/{zipName}", method = RequestMethod.GET)
    public void getPackage(final HttpServletResponse response, final @PathVariable String zipName) throws IOException {
        File file;
        if (zipName.endsWith(ZIP_FILE)) {
            file = new File(zipName);
        } else {
            file = new File(zipName + ".zip");
        }
        response.setContentType("application/zip");
        response.setContentLengthLong(file.length());
        try (OutputStream out = response.getOutputStream(); InputStream in = new FileInputStream(file)) {
            byte[] target = new byte[TWO_BYTE_LEN];
            int length;
            while ((length = in.read(target)) > 0) {
                out.write(target, 0, length);
            }
        }
    }
}
