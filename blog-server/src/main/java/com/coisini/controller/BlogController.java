package com.coisini.controller;

import com.coisini.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.coisini.config.RedisConfig;
import com.coisini.entity.Blog;
import com.coisini.exception.BusinessException;
import com.coisini.service.BlogService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

/**
 * @Description 博客API
 * @author coisini
 * @date Jan 16, 2022
 * @version 2.0
 */
@Api(tags = "博客api")
@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BlogController {

    private static final String IMAGE_JPG = ".jpg";

    private static final String IMAGE_PNG = ".png";

    private final BlogService blogService;

    private final FormatUtil formatUtil;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @ApiOperation(value = "上传图片", notes = "图片")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/uploadImg")
    public UnifyResponse uploadImg(MultipartFile file) {
        if (!formatUtil.checkObjectNull(file)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        String fileFormat = formatUtil.getFileFormat(file.getOriginalFilename());

        if (null == fileFormat) {
            return UnifyResponse.fail(UnifyCode.ERROR, "图片缺少格式", null);
        }

        if (!IMAGE_JPG.equals(fileFormat.toLowerCase()) && !IMAGE_PNG.equals(fileFormat.toLowerCase())) {
            return UnifyResponse.fail(UnifyCode.ERROR, "图片不支持除 jpg/png 以外的格式", null);
        }
        
        try {
            String url = blogService.saveImg(file);
            return UnifyResponse.success(UnifyCode.UPLOAD_SUCCESS, url);
        } catch (IOException e) {
            log.error("图片上传失败", e);
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "上传失败" + e.getMessage(), null);
        }
    }

    /**
     * 保存博客，博客内容由前端md编辑器生成
     * @param blogBody
     * @param blogTitle
     * @return
     */
    @ApiOperation(value = "发布博客", notes = "博客标题+博客内容+博客标签")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping
    public UnifyResponse newBlog(String blogTitle, String blogBody, Integer[] tagId) {
        if (!formatUtil.checkStringNull(blogTitle, blogBody) || !formatUtil.checkPositive(tagId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        try {
            blogService.saveBlog(blogTitle, blogBody, tagId);
            return UnifyResponse.success(UnifyCode.PUBLISH_SUCCESS);
        } catch (IOException e) {
            log.error("发布失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "发布失败" + e.getMessage(), null);
        }
    }

    /**
     * 根据id查询博客
     * @param blogId
     * @param isHistory
     * @return
     * @throws BusinessException
     */
    @ApiOperation(value = "根据id查询博客", notes = "博客id")
    @GetMapping("/{blogId}/{isHistory}")
    public UnifyResponse findBlogById(@PathVariable Integer blogId, @PathVariable boolean isHistory) throws BusinessException {
        if (!formatUtil.checkObjectNull(blogId, isHistory)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        try {
            Blog blog = blogService.findBlogById(blogId, isHistory);
            return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, blog);
        } catch (Exception e) {
            log.error("查询失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "此博客不存在", null);
        }
    }

    /**
     * 根据用户分页查询博客
     * @param page      页数
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "根据用户分页查询博客", notes = "页数+显示数量")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/myblog/{page}/{showCount}")
    public UnifyResponse findBlogByUser(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<Blog> pageResult =
                new PageResult<>(blogService.getBlogCountByUser(), blogService.findBlogByUser(page, showCount));

        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 首页分页查询
     * 查询的范围在 最近10条博客 内
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "首页分页查询博客", notes = "页数+显示数量")
    @GetMapping("/home/{page}/{showCount}")
    public UnifyResponse homeBlog(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkPositive(page, showCount)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            PageResult<Blog> pageResult = new PageResult<>(blogService.getHomeBlogCount(), blogService.findHomeBlog(page, showCount));
            return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
        } catch (IOException e) {
            log.error("查询失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "查询失败", null);
        }
    }

    /**
     * 热门博客
     * @return
     */
    @ApiOperation(value = "首页热门博客", notes = "首页热门博客")
    @GetMapping("/hotBlog")
    public UnifyResponse hotBlog() {
        try {
            List<Blog> hotBlog = blogService.findHotBlog();
            return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, hotBlog);
        } catch (IOException e) {
            log.error("查询失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "查询失败", null);
        }
    }

    /**
     * 博客搜索
     * @param search
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页搜索博客", notes = "搜索内容+页码+显示条数")
    @GetMapping("/searchBlog/{page}/{showCount}")
    public UnifyResponse searchBlog(String search,
                             @PathVariable Integer page,
                             @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount) || showCount > RedisConfig.REDIS_NEW_BLOG_COUNT) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        if (!formatUtil.checkStringNull(search)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<Blog> pageResult = new PageResult<>(blogService.getSearchBlogCount(search),
                blogService.searchBlog(search, page, showCount));
        
        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 查询所有博客，包括删除状态
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "管理员查询博客", notes = "管理员查询博客")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/AllBlog/{page}/{showCount}")
    public UnifyResponse findAllBlog(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        PageResult<Blog> pageResult = new PageResult<>(blogService.getAllBlogCount(), blogService.findAllBlog(page, showCount));
        
        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 修改博客
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagId
     * @return
     */
    @ApiOperation(value = "修改博客", notes = "博客id+博客标题+博客内容+博客标签")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{blogId}")
    public UnifyResponse updateBlog(@PathVariable Integer blogId, String blogTitle, String blogBody, Integer[] tagId) {

        if (!formatUtil.checkStringNull(blogTitle, blogBody)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        if (!formatUtil.checkPositive(tagId) || !formatUtil.checkPositive(blogId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            blogService.updateBlog(blogId, blogTitle, blogBody, tagId);
            return UnifyResponse.success(UnifyCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error("修改失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "修改失败", null);
        }
    }

    /**
     * 用户删除博客
     * @param blogId
     * @return
     */
    @ApiOperation(value = "用户删除博客", notes = "博客id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{blogId}")
    public UnifyResponse deleteBlog(@PathVariable Integer blogId) {

        if (!formatUtil.checkPositive(blogId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            blogService.deleteBlog(blogId);
            return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("删除失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "删除失败", null);
        }
    }

    /**
     * 管理员删除博客
     * @param blogId
     * @return
     * @throws JsonProcessingException
     */
    @ApiOperation(value = "管理员删除博客", notes = "博客id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{blogId}")
    public UnifyResponse adminDeleteBlog(@PathVariable Integer blogId) throws JsonProcessingException {

        if (!formatUtil.checkPositive(blogId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        blogService.adminDeleteBlog(blogId);
        return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
    }

    /**
     * 管理员分页搜索博客
     * @param search
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "管理员分页搜索博客", notes = "搜索内容+页码+显示条数")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/searchAllBlog/{page}/{showCount}")
    public UnifyResponse searchAllBlog(String search,
                                @PathVariable Integer page,
                                @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        if (!formatUtil.checkStringNull(search)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<Blog> pageResult = new PageResult<>(blogService.getSearchAllBlogCount(search),
                blogService.searchAllBlog(search, page, showCount));
        
        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 按月份归档博客
     * @return
     */
    @ApiOperation(value = "按月份归档博客", notes = "按月份归档博客")
    @GetMapping("/statisticalBlogByMonth")
    public UnifyResponse statisticalBlogByMonth() {
        try {
            return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, blogService.statisticalBlogByMonth());
        } catch (Exception e) {
            log.error("查询失败", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "查询失败" + e.getMessage(), null);
        }
    }

}
