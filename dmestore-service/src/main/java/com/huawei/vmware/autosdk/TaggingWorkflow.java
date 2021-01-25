package com.huawei.vmware.autosdk;

import com.vmware.cis.tagging.Category;
import com.vmware.cis.tagging.CategoryModel;
import com.vmware.cis.tagging.CategoryTypes;
import com.vmware.cis.tagging.Tag;
import com.vmware.cis.tagging.TagAssociation;
import com.vmware.cis.tagging.TagModel;
import com.vmware.cis.tagging.TagTypes;
import com.vmware.vapi.std.DynamicID;

import java.util.List;

/**
 * TaggingWorkflow
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class TaggingWorkflow {
    private Category categoryService;

    private Tag taggingService;

    private TagAssociation tagAssociation;

    private SessionHelper sessionHelper;

    /**
     * TaggingWorkflow
     *
     * @param sessionHelper sessionHelper
     */
    public TaggingWorkflow(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
        this.categoryService = this.sessionHelper.vapiAuthHelper.getStubFactory()
            .createStub(Category.class, sessionHelper.sessionStubConfig);
        this.taggingService = this.sessionHelper.vapiAuthHelper.getStubFactory()
            .createStub(Tag.class, sessionHelper.sessionStubConfig);
        this.tagAssociation = this.sessionHelper.vapiAuthHelper.getStubFactory()
            .createStub(TagAssociation.class, sessionHelper.sessionStubConfig);
    }

    /**
     * createTagCategory
     *
     * @param createSpec createSpec
     * @return String
     */
    public String createTagCategory(CategoryTypes.CreateSpec createSpec) {
        return this.categoryService.create(createSpec);
    }

    /**
     * listTagCategory
     *
     * @return List
     */
    public List<String> listTagCategory() {
        return this.categoryService.list();
    }

    /**
     * getTagCategory
     *
     * @param categoryid categoryid
     * @return CategoryModel
     */
    public CategoryModel getTagCategory(String categoryid) {
        return this.categoryService.get(categoryid);
    }

    /**
     * Creates tag
     *
     * @param name        Display name of the tag.
     * @param description Tag description.
     * @param categoryId  ID of the parent category in which this tag will be created.
     * @return Id of the created tag
     */
    public String createTag(String name, String description, String categoryId) {
        TagTypes.CreateSpec spec = new TagTypes.CreateSpec();
        spec.setName(name);
        spec.setDescription(description);
        spec.setCategoryId(categoryId);

        return this.taggingService.create(spec);
    }

    /**
     * Delete an existing tag. User who invokes this API needs delete privilege
     * on the tag.
     *
     * @param tagId the ID of the input tag
     */
    public void deleteTag(String tagId) {
        this.taggingService.delete(tagId);
    }

    /**
     * tag the Object
     *
     * @param tagId        tagId
     * @param objDynamicId objDynamicId
     */
    public void attachTag(String tagId, DynamicID objDynamicId) {
        this.tagAssociation.attach(tagId, objDynamicId);
    }

    /**
     * list tags
     *
     * @return List
     */
    public List<String> listTags() {
        return this.taggingService.list();
    }

    /**
     * listTagsForCategory
     *
     * @param categoryid categoryid
     * @return List
     */
    public List<String> listTagsForCategory(String categoryid) {
        return this.taggingService.listTagsForCategory(categoryid);
    }

    /**
     * TagModel
     *
     * @param tagId tagId
     * @return TagModel
     */
    public TagModel getTag(String tagId) {
        return this.taggingService.get(tagId);
    }
}
