package com.coisini.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.coisini.config.RedisConfig;
import com.coisini.model.PageResult;
import com.coisini.model.ResponseModel;
import com.coisini.model.SysErrorCode;
import com.coisini.entity.Blog;
import com.coisini.exception.BusinessException;
import com.coisini.service.BlogService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
  *  博文API
 * @author Coisini
 * @date Mar 21, 2020
 */

@Api(tags = "博文api")
@RestController
@RequestMapping("/blogs")
public class BlogController {

    private static final String IMAGE_JPG = ".jpg";

    private static final String IMAGE_PNG = ".png";

    @Autowired
    private BlogService blogService;

    @Autowired
    private FormatUtil formatUtil;

    @ApiOperation(value = "上传图片", notes = "图片")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/uploadImg")
    public ResponseModel uploadImg(MultipartFile file) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkObjectNull(file)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        String fileFormat = formatUtil.getFileFormat(file.getOriginalFilename());

        if (null == fileFormat) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("图片缺少格式");
        	return responseModel;
        }

        if (!IMAGE_JPG.equals(fileFormat.toLowerCase()) && !IMAGE_PNG.equals(fileFormat.toLowerCase())) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("图片不支持除 jpg/png 以外的格式");
        	return responseModel;
        }
        
        try {
            String url = blogService.saveImg(file);
            responseModel.setSta(SysErrorCode.CODE_00);
            responseModel.setMessage("上传成功");
            responseModel.setData(url);
            return responseModel;
        } catch (IOException ioe) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("上传失败" + ioe.getMessage());
        	return responseModel;
        }
    }

    /**
          * 保存博文，博文内容由前端md编辑器生成
     * @param blogBody
     * @param blogTitle
     * @return
     */
    @ApiOperation(value = "发布博文", notes = "博文标题+博文内容+博文标签")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseModel newBlog(String blogTitle, String blogBody, Integer[] tagId) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkStringNull(blogTitle, blogBody) || !formatUtil.checkPositive(tagId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        try {
            blogService.saveBlog(blogTitle, blogBody, tagId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("发布成功");
        	return responseModel;
        } catch (IOException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("发布失败");
        	return responseModel;
        }
    }

    @ApiOperation(value = "根据id查询博文", notes = "博文id")
    @GetMapping("/{blogId}/{isHistory}")
    public ResponseModel findBlogById(@PathVariable Integer blogId, @PathVariable boolean isHistory) throws BusinessException {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkObjectNull(blogId, isHistory)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        try {
        	responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("查询成功");
        	responseModel.setData(blogService.findBlogById(blogId, isHistory));
        	return responseModel;
        } catch (RuntimeException e) {
        	e.printStackTrace();
        	throw new BusinessException("此博客不存在");
        } catch (IOException e) {
        	e.printStackTrace();
        	throw new BusinessException("此博客不存在");
        }
    }

    /**
          * 根据用户分页查询博文
     * @param page      页数
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "根据用户分页查询博文", notes = "页数+显示数量")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myblog/{page}/{showCount}")
    public ResponseModel findBlogByUser(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        PageResult<Blog> pageResult =
                new PageResult<>(blogService.getBlogCountByUser(), blogService.findBlogByUser(page, showCount));

        responseModel.setSta(SysErrorCode.CODE_00);
        responseModel.setMessage("查询成功");
        responseModel.setData(pageResult);
        return responseModel;
    }

    /**
     * 首页分页查询
     * 查询的范围在 最近10条博客 内
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "首页分页查询博文", notes = "页数+显示数量")
    @GetMapping("/home/{page}/{showCount}")
    public ResponseModel homeBlog(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
		/*
		 * if (!formatUtil.checkPositive(page, showCount) || showCount >
		 * RedisConfig.REDIS_NEW_BLOG_COUNT) { return Result.create(StatusCode.OK,
		 * "参数错误"); }
		 */

        try {
            PageResult<Blog> pageResult = new PageResult<>(blogService.getHomeBlogCount(), blogService.findHomeBlog(page, showCount));
            responseModel.setSta(SysErrorCode.CODE_00);
            responseModel.setMessage("查询成功");
            responseModel.setData(pageResult);
            return responseModel;
//            return Result.create(StatusCode.OK, "查询成功", pageResult);
        } catch (IOException e) {
            e.printStackTrace();
            responseModel.setSta(SysErrorCode.CODE_00);
            responseModel.setMessage("查询成功");
            return responseModel;
        }
    }

    /**
          * 热门博文
          * 正常状态
     * @return
     */
    @ApiOperation(value = "首页热门博文", notes = "首页热门博文")
    @GetMapping("/hotBlog")
    public ResponseModel hotBlog() {
    	ResponseModel responseModel = new ResponseModel();
        try {
        	responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("查询成功");
        	responseModel.setData(blogService.findHotBlog());
        } catch (IOException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("服务异常");
        }
		return responseModel;
    }

    /**
          * 博文搜索
          * 正常状态
     * @param search
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页搜索博文", notes = "搜索内容+页码+显示条数")
    @GetMapping("/searchBlog/{page}/{showCount}")
    public ResponseModel searchBlog(String search,
                             @PathVariable Integer page,
                             @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkPositive(page, showCount) || showCount > RedisConfig.REDIS_NEW_BLOG_COUNT) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        if (!formatUtil.checkStringNull(search)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        PageResult<Blog> pageResult = new PageResult<>(blogService.getSearchBlogCount(search),
                blogService.searchBlog(search, page, showCount));
        
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(pageResult);
    	return responseModel;
    }

    /**
          * 查询所有博客，包括删除状态
     * @return
     */
    @ApiOperation(value = "管理员查询博文", notes = "管理员查询博文")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/AllBlog/{page}/{showCount}")
    public ResponseModel findAllBlog(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        PageResult<Blog> pageResult = new PageResult<>(blogService.getAllBlogCount(), blogService.findAllBlog(page, showCount));
        
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(pageResult);
    	return responseModel;
    }

    @ApiOperation(value = "修改博文", notes = "博文id+博文标题+博文内容+博文标签")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{blogId}")
    public ResponseModel updateBlog(@PathVariable Integer blogId, String blogTitle, String blogBody, Integer[] tagId) {
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkStringNull(blogTitle, blogBody)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        if (!formatUtil.checkPositive(tagId) || !formatUtil.checkPositive(blogId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        try {
            blogService.updateBlog(blogId, blogTitle, blogBody, tagId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("修改成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("修改失败" + e.getMessage());
        	return responseModel;
        } catch (JsonProcessingException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("服务异常");
        	return responseModel;
        }
    }

    @ApiOperation(value = "用户删除博文", notes = "博文id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{blogId}")
    public ResponseModel deleteBlog(@PathVariable Integer blogId) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(blogId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        try {
            blogService.deleteBlog(blogId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("删除成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("删除失败" + e.getMessage());
        	return responseModel;
        } catch (JsonProcessingException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("服务异常");
        	return responseModel;
        }
    }

    @ApiOperation(value = "管理员删除博文", notes = "博文id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{blogId}")
    public ResponseModel adminDeleteBlog(@PathVariable Integer blogId) throws JsonProcessingException {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(blogId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        blogService.adminDeleteBlog(blogId);
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("删除成功");
    	return responseModel;
    }

    @ApiOperation(value = "管理员分页搜索博文", notes = "搜索内容+页码+显示条数")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/searchAllBlog/{page}/{showCount}")
    public ResponseModel searchAllBlog(String search,
                                @PathVariable Integer page,
                                @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        if (!formatUtil.checkStringNull(search)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        PageResult<Blog> pageResult = new PageResult<>(blogService.getSearchAllBlogCount(search),
                blogService.searchAllBlog(search, page, showCount));
        
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(pageResult);
    	return responseModel;
    }

    @ApiOperation(value = "按月份归档博客", notes = "按月份归档博客")
    @GetMapping("/statisticalBlogByMonth")
    public ResponseModel statisticalBlogByMonth() {
    	ResponseModel responseModel = new ResponseModel();
        try {
        	responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("查询成功");
        	responseModel.setData(blogService.statisticalBlogByMonth());
        } catch (IOException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("服务异常");
        }
    	
    	return responseModel;
    }

}
