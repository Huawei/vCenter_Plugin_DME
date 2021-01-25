package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.BandPorts;
import com.huawei.dmestore.model.Dtrees;
import com.huawei.dmestore.model.EthPortInfo;
import com.huawei.dmestore.model.FailoverGroup;
import com.huawei.dmestore.model.FileSystem;
import com.huawei.dmestore.model.FileSystemDetail;
import com.huawei.dmestore.model.LogicPorts;
import com.huawei.dmestore.model.NfsShares;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.StorageControllers;
import com.huawei.dmestore.model.StorageDetail;
import com.huawei.dmestore.model.StorageDisk;
import com.huawei.dmestore.model.StoragePool;
import com.huawei.dmestore.model.StoragePort;
import com.huawei.dmestore.model.Volume;
import com.huawei.dmestore.model.VolumeListRestponse;

import java.util.List;
import java.util.Map;

/**
 * DmeStorageService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public interface DmeStorageService {
    /**
     * list storage
     *
     * @return List
     * @throws DmeException when error
     */
    List<Storage> getStorages() throws DmeException;

    /**
     * get storage detail
     *
     * @param storageId storage id
     * @return StorageDetail
     * @throws DmeException when error
     */
    StorageDetail getStorageDetail(String storageId) throws DmeException;

    /**
     * list storage pool
     *
     * @param storageId storage id
     * @param mediaType media type
     * @return List
     * @throws DmeException when error
     */
    List<StoragePool> getStoragePools(String storageId, String mediaType) throws DmeException;

    /**
     * list logic ports
     *
     * @param storageId storage id
     * @return List
     * @throws DmeException when error
     */
    List<LogicPorts> getLogicPorts(String storageId) throws DmeException;

    /**
     * list volumes by page
     *
     * @param storageId storage id
     * @param pageSize pageSize
     * @param pageNo pageNo
     * @return VolumeListRestponse
     * @throws DmeException when error
     */
    VolumeListRestponse getVolumesByPage(String storageId, String pageSize, String pageNo) throws DmeException;

    /**
     * list FileSystem
     *
     * @param storageId storage id
     * @return List
     * @throws DmeException when error
     */
    List<FileSystem> getFileSystems(String storageId) throws DmeException;

    /**
     * list dtree
     *
     * @param storageId storage id
     * @return List
     * @throws DmeException when error
     */
    List<Dtrees> getDtrees(String storageId) throws DmeException;

    /**
     * list NFS share
     *
     * @param storageId storage id
     * @return List
     * @throws DmeException when error
     */
    List<NfsShares> getNfsShares(String storageId) throws DmeException;

    /**
     * list band ports
     *
     * @param storageId storage id
     * @return List
     * @throws DmeException when error
     */
    List<BandPorts> getBandPorts(String storageId) throws DmeException;

    /**
     * list storage controller
     *
     * @param storageDeviceId storage device id
     * @return List
     * @throws DmeException when error
     */
    List<StorageControllers> getStorageControllers(String storageDeviceId) throws DmeException;

    /**
     * list storage disk
     *
     * @param storageDeviceId storage device id
     * @return List
     * @throws DmeException when error
     */
    List<StorageDisk> getStorageDisks(String storageDeviceId) throws DmeException;

    /**
     * list storage disk
     *
     * @param storageSn storage sn
     * @return List
     * @throws DmeException when error
     */
    List<EthPortInfo> getStorageEthPorts(String storageSn) throws DmeException;

    /**
     * get volume detail
     *
     * @param volumeId volume id
     * @return Map
     * @throws DmeException when error
     */
    Map<String, Object> getVolume(String volumeId) throws DmeException;

    /**
     * get storage port
     *
     * @param storageDeviceId storage Device Id
     * @param portType port Type
     * @return List
     * @throws DmeException when error
     */
    List<StoragePort> getStoragePort(String storageDeviceId, String portType) throws DmeException;

    /**
     * get Failover Groups
     *
     * @param storageId storage Id
     * @return List
     * @throws DmeException when error
     */
    List<FailoverGroup> getFailoverGroups(String storageId) throws DmeException;

    /**
     * get fs detail
     *
     * @param fileSystemId fs Id
     * @return FileSystemDetail
     * @throws DmeException when error
     */
    FileSystemDetail getFileSystemDetail(String fileSystemId) throws DmeException;

    /**
     * list StoragePer formance
     *
     * @param storageIds storage Ids
     * @return List
     * @throws DmeException when error
     */
    List<Storage> listStoragePerformance(List<String> storageIds) throws DmeException;

    /**
     * list Storage Pool Performance
     *
     * @param storagePoolIds storage Pool Ids
     * @return List
     * @throws DmeException when error
     */
    List<StoragePool> listStoragePoolPerformance(List<String> storagePoolIds) throws DmeException;

    /**
     * list Storage Controller Performance
     *
     * @param storageControllerIds storage Controller Ids
     * @return List
     * @throws DmeException when error
     */
    List<StorageControllers> listStorageControllerPerformance(List<String> storageControllerIds) throws DmeException;

    /**
     * list Storage Disk Performance
     *
     * @param storageDiskIds storage Disk Ids
     * @return List
     * @throws DmeException when error
     */
    List<StorageDisk> listStorageDiskPerformance(List<String> storageDiskIds) throws DmeException;

    /**
     * list Storage PortPer Performance
     *
     * @param storagePortIds storage Port Ids
     * @return List
     * @throws DmeException when error
     */
    List<StoragePort> listStoragePortPerformance(List<String> storagePortIds) throws DmeException;

    /**
     * list Volume Performance
     *
     * @param volumeId volume Id
     * @return List
     * @throws DmeException when error
     */
    List<Volume> listVolumesPerformance(List<String> volumeId) throws DmeException;

    /**
     * get volume by name
     *
     * @param name volume name
     * @return Boolean
     * @throws DmeException when error
     */
    Boolean queryVolumeByName(String name) throws DmeException;

    /**
     * list Volume Performance
     *
     * @param name fs name
     * @param storageId storage Id
     * @return Boolean
     * @throws DmeException when error
     */
    Boolean queryFsByName(String name, String storageId) throws DmeException;

    /**
     * list Volume Performance
     *
     * @param name fs name
     * @param storageId storage Id
     * @return Boolean
     * @throws DmeException when error
     */
    Boolean queryShareByName(String name, String storageId) throws DmeException;

    /**
     * list Volume Performance
     *
     * @param poolRawId poolRawId
     * @return String
     * @throws DmeException when error
     */
    String getStorageByPoolRawId(String poolRawId) throws DmeException;
}
