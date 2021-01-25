package com.huawei.vmware.util;

import static org.mockito.Mockito.spy;

import com.vmware.pbm.PbmCapabilityMetadata;
import com.vmware.pbm.PbmCapabilityMetadataPerCategory;
import com.vmware.pbm.PbmCapabilityMetadataUniqueId;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className PbmUtilTest
 * @description TODO
 * @date 2020/12/2 11:05
 */
public class PbmUtilTest {

    @InjectMocks
    PbmUtil pbmUtil = new PbmUtil();

    @Mock
    private static VmwareContextPool s_pool;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getStorageResourceType() {
        pbmUtil.getStorageResourceType();
    }

    @Test
    public void getTagCategoryMeta() {
        List<PbmCapabilityMetadataPerCategory> schema = spy(ArrayList.class);
        PbmCapabilityMetadataPerCategory pbmCapabilityMetadataPerCategory = spy(PbmCapabilityMetadataPerCategory.class);
        pbmCapabilityMetadataPerCategory.setSubCategory("tag");
        PbmCapabilityMetadata pbmCapabilityMetadata = spy(PbmCapabilityMetadata.class);
        PbmCapabilityMetadataUniqueId pbmCapabilityMetadataUniqueId = spy(PbmCapabilityMetadataUniqueId.class);
        pbmCapabilityMetadataUniqueId.setId("tag");
        pbmCapabilityMetadata.setId(pbmCapabilityMetadataUniqueId);
        pbmCapabilityMetadataPerCategory.getCapabilityMetadata().add(pbmCapabilityMetadata);
        schema.add(pbmCapabilityMetadataPerCategory);
        pbmUtil.getTagCategoryMeta("tag", schema);
    }
}