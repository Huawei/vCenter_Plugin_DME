package com.huawei.vcenter;

import com.huawei.vcenter.utils.KeytookUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * VcenterDeployerApplication
 *
 * @author andrewliu
 * @since 2020-09-15
 **/
@SpringBootApplication
public class VcenterDeployerApplication {
    protected static final Logger LOGGER = LoggerFactory.getLogger(VcenterDeployerApplication.class);

    public static void main(String[] args) {
        try {
            KeytookUtil.genKey();
            LOGGER.info("Starting server...");
        } catch (IOException e) {
            LOGGER.error("Starting server error!");
        }

        SpringApplication.run(VcenterDeployerApplication.class, args);

        try {
            LOGGER.info("Server has been started.");
            List<String> hosts = getLocalIp();
            StringBuffer buffer = new StringBuffer();
            for (String host : hosts) {
                buffer.append("\r\n").append("https://").append(host).append(":8443");
            }
            LOGGER.info("Use either URL that vCenter can access to open page: {}", buffer.toString());
        } catch (SocketException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static final List<String> getLocalIp() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        List<String> ipList = new ArrayList();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address && !"127.0.0.1".equals(ip.getHostAddress())) {
                    ipList.add(ip.getHostAddress());
                }
            }
        }
        return ipList;
    }
}
