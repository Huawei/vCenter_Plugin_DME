package com.huawei.vmware.mo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Properties;



/**
 * SnapshotDescriptor
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class SnapshotDescriptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotDescriptor.class);

    private final Properties properties = new Properties();
    private final String fileNameStr = "snapshot%d.disk%d.fileName";
    private final String snapshotStr = "snapshot.numSnapshots";
    private final String strNumDisks = "snapshot%d.numDisks";
    private final String stringS = "%s = \"%s\"";

    /**
     * SnapshotDescriptor
     */
    public SnapshotDescriptor() {
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * parse
     *
     * @param vmsdFileContent vmsdFileContent
     * @throws IOException IOException
     */
    public void parse(byte[] vmsdFileContent) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(vmsdFileContent), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    String name = tokens[0].trim();
                    String value = tokens[1].trim();
                    if (value.charAt(0) == '\"') {
                        value = value.substring(1, value.length() - 1);
                    }

                    properties.put(name, value);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * removeDiskReferenceFromSnapshot
     *
     * @param diskFileName diskFileName
     */
    public void removeDiskReferenceFromSnapshot(String diskFileName) {
        String numSnapshotsStr = properties.getProperty(snapshotStr);
        if (numSnapshotsStr != null) {
            int numSnaphosts = Integer.parseInt(numSnapshotsStr);
            for (int i = 0; i < numSnaphosts; i++) {
                String numDisksStr = properties.getProperty(String.format(strNumDisks, i));
                int numDisks = Integer.parseInt(numDisksStr);

                boolean diskFound = false;
                for (int j = 0; j < numDisks; j++) {
                    String keyName = String.format(fileNameStr, i, j);
                    String fileName = properties.getProperty(keyName);
                    if (!diskFound) {
                        if (fileName.equalsIgnoreCase(diskFileName)) {
                            diskFound = true;
                            properties.remove(keyName);
                        }
                    } else {
                        properties.setProperty(String.format(fileNameStr, i, j - 1), fileName);
                    }
                }

                if (diskFound) {
                    properties.setProperty(String.format(strNumDisks, i), String.valueOf(numDisks - 1));
                }
            }
        }
    }

    /**
     * getVmsdContent
     *
     * @return byte[]
     */
    public byte[] getVmsdContent() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(bos, "UTF-8"));
            out.write(".encoding = \"UTF-8\"");
            out.newLine();
            out.write(String.format("snapshot.lastUID = \"%s\"", properties.getProperty("snapshot.lastUID")));
            out.newLine();
            String numSnapshotsStr = properties.getProperty(snapshotStr);
            if (numSnapshotsStr == null || numSnapshotsStr.isEmpty()) {
                numSnapshotsStr = "0";
            }
            out.write(String.format("snapshot.numSnapshots = \"%s\"", numSnapshotsStr));
            out.newLine();

            String value = properties.getProperty("snapshot.current");
            if (value != null) {
                out.write(String.format("snapshot.current = \"%s\"", value));
                out.newLine();
            }

            String key;
            for (int i = 0; i < Integer.parseInt(numSnapshotsStr); i++) {
                key = String.format("snapshot%d.uid", i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                key = String.format("snapshot%d.filename", i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                key = String.format("snapshot%d.displayName", i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                key = String.format("snapshot%d.description", i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                key = String.format("snapshot%d.createTimeHigh", i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                key = String.format("snapshot%d.createTimeLow", i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                key = String.format(strNumDisks, i);
                value = properties.getProperty(key);
                out.write(String.format(stringS, key, value));
                out.newLine();

                int numDisks = Integer.parseInt(value);
                for (int j = 0; j < numDisks; j++) {
                    key = String.format(fileNameStr, i, j);
                    value = properties.getProperty(key);
                    out.write(String.format(stringS, key, value));
                    out.newLine();

                    key = String.format("snapshot%d.disk%d.node", i, j);
                    value = properties.getProperty(key);
                    out.write(String.format(stringS, key, value));
                    out.newLine();
                }
            }
        } catch (IOException e) {
            assert false;
            LOGGER.error("Unexpected exception ", e);
        }

        return bos.toByteArray();
    }

    private int getSnapshotId(String seqStr) {
        if (seqStr != null) {
            int seq = Integer.parseInt(seqStr);
            String numSnapshotStr = properties.getProperty(snapshotStr);
            assert numSnapshotStr != null;
            for (int i = 0; i < Integer.parseInt(numSnapshotStr); i++) {
                String value = properties.getProperty(String.format("snapshot%d.uid", i));
                if (value != null && Integer.parseInt(value) == seq) {
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * getCurrentDiskChain
     *
     * @return SnapshotInfo
     */
    public SnapshotInfo[] getCurrentDiskChain() {
        ArrayList<SnapshotInfo> list = new ArrayList<SnapshotInfo>();
        String current = properties.getProperty("snapshot.current");
        int id;
        while (current != null) {
            id = getSnapshotId(current);
            String numDisksStr = properties.getProperty(String.format(strNumDisks, id));
            int numDisks = 0;
            if (numDisksStr != null && !numDisksStr.isEmpty()) {
                numDisks = Integer.parseInt(numDisksStr);
                DiskInfo[] disks = new DiskInfo[numDisks];
                for (int i = 0; i < numDisks; i++) {
                    disks[i] = new DiskInfo(properties.getProperty(String.format(fileNameStr, id, i)),
                        properties.getProperty(String.format("snapshot%d.disk%d.node", id, i)));
                }

                SnapshotInfo info = new SnapshotInfo();
                info.setId(id);
                info.setNumOfDisks(numDisks);
                info.setDisks(disks);
                info.setDisplayName(properties.getProperty(String.format("snapshot%d.displayName", id)));
                list.add(info);
            }

            current = properties.getProperty(String.format("snapshot%d.parent", id));
        }

        return list.toArray(new SnapshotInfo[0]);
    }

    /**
     * SnapshotInfo
     *
     * @since 2020-12-11
     */
    public static class SnapshotInfo {
        private int id;

        private String displayName;

        private int numOfDisks;

        private DiskInfo[] disks;

        /**
         * SnapshotInfo
         */
        public SnapshotInfo() {
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setDisplayName(String name) {
            displayName = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setNumOfDisks(int numOfDisks) {
            this.numOfDisks = numOfDisks;
        }

        public int getNumOfDisks() {
            return numOfDisks;
        }

        public void setDisks(DiskInfo[] disks) {
            this.disks = disks;
        }

        public DiskInfo[] getDisks() {
            return disks;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("SnapshotInfo : { id: ");
            sb.append(id);
            sb.append(", displayName: ").append(displayName);
            sb.append(", numOfDisks: ").append(numOfDisks);
            sb.append(", disks: [");
            if (disks != null) {
                int i = 0;
                for (DiskInfo diskInfo : disks) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(diskInfo.toString());
                    i++;
                }
            }
            sb.append("]}");

            return sb.toString();
        }
    }

    /**
     * DiskInfo
     *
     * @since 2020-12-11
     */
    public static class DiskInfo {
        private final String diskFileName;

        private final String deviceName;

        /**
         * DiskInfo
         *
         * @param diskFileName diskFileName
         * @param deviceName   deviceName
         */
        public DiskInfo(String diskFileName, String deviceName) {
            this.diskFileName = diskFileName;
            this.deviceName = deviceName;
        }

        public String getDiskFileName() {
            return diskFileName;
        }

        public String getDeviceName() {
            return deviceName;
        }

        @Override
        public String toString() {
            return "DiskInfo: { device: " + deviceName + ", file: " + diskFileName + " }";
        }
    }
}
