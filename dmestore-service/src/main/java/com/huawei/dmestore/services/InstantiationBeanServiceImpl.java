package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.H2DataBaseDao;
import com.huawei.dmestore.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * InstantiationBeanServiceImpl
 *
 * @author liugq
 * @since 2020-09-15
 **/
public class InstantiationBeanServiceImpl implements InstantiationBeanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantiationBeanServiceImpl.class);

    private static final String FILE_SEPARATOR = "/";

    private SystemService systemService;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    @Override
    public void init() {
        if (!FileUtils.isWindows()) {
            try {
                File newDbFile = new File(FileUtils.getPath(true) + FILE_SEPARATOR + H2DataBaseDao.getDbFileName());
                String oldDbFile = FileUtils.getOldDbFolder() + FILE_SEPARATOR + H2DataBaseDao.getDbFileName();

                // no DB file in new path
                if (new File(oldDbFile).exists() && !newDbFile.exists()) {
                    // move db file
                    LOGGER.info("Copying DB file from {} to {}", H2DataBaseDao.getDbFileName(), newDbFile.getName());
                    Files.copy(Paths.get(oldDbFile), Paths.get(newDbFile.getAbsolutePath()),
                        StandardCopyOption.REPLACE_EXISTING);

                    // move key files
                    String oldFolder = FileUtils.getOldFolder();
                    String newFolder = FileUtils.getPath();
                    LOGGER.info("Copying key files...");
                    Files.copy(Paths.get(oldFolder + FILE_SEPARATOR + FileUtils.BASE_FILE_NAME),
                        Paths.get(newFolder + FILE_SEPARATOR + FileUtils.BASE_FILE_NAME),
                        StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(Paths.get(oldFolder + FILE_SEPARATOR + FileUtils.WORK_FILE_NAME),
                        Paths.get(newFolder + FILE_SEPARATOR + FileUtils.WORK_FILE_NAME),
                        StandardCopyOption.REPLACE_EXISTING);
                    FileUtils.setFilePermission(new File(newFolder + FILE_SEPARATOR + FileUtils.BASE_FILE_NAME));
                    FileUtils.setFilePermission(new File(newFolder + FILE_SEPARATOR + FileUtils.WORK_FILE_NAME));
                }
            } catch (IOException e) {
                LOGGER.warn("Cannot move file");
            }
        }
        systemService.initDb();
    }
}
