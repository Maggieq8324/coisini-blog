import request from '@/utils/request';
import qs from 'qs';

export default {
  getHotBlog() {
    return request({
      url: '/blogs/hotBlog',
      method: 'get'
    });
  },
  getStatisticalBlogByMonth() {
    return request({
      url: '/blogs/statisticalBlogByMonth',
      method: 'get'
    });
  },
  getBlogHome(page, showCount) {
    return request({
      url: '/blogs/home/' + page + '/' + showCount,
      method: 'get'
    });
  },
  getBlogById(id, isClick) {
    return request({
      url: '/blogs/' + id + '/' + isClick,
      method: 'get'
    });
  },
  getMyBlog(page, showCount) {
    return request({
      url: '/blogs/myblog/' + page + '/' + showCount,
      method: 'get'
    });
  },
  sendBlog(blogTitle, blogBody, tagId) {  // 发布博客
    // alert(qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody,'tagId':tagId}))
    return request({
      url: '/blogs',
      method: 'post',
      data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
    });
  },
  uploadImg(formdata) {
    return request({
      url: '/blogs/uploadImg',
      method: 'post',
      data: formdata,
      headers: {'Content-Type': 'multipart/form-data'}
    });
  },
  editBlog(blogId, blogTitle, blogBody, tagId) {  // 发布博客
    return request({
      url: '/blogs/' + blogId,
      method: 'put',
      data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
    });
  },
  adminDeleteBlog(blogId) { // 管理员删除博客
    return request({
      url: '/blogs/admin/' + blogId,
      method: 'delete'
    });
  },
  userDeleteBlog(blogId) { // 普通用户删除博客
    return request({
      url: '/blogs/' + blogId,
      method: 'delete'
    });
  },
  adminGetBlog(page, showCount) {
    return request({
      url: '/blogs/AllBlog/' + page + '/' + showCount,
      method: 'get'
    });
  },
  adminSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blogs/searchAllBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    });
  },
  userSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blogs/searchBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    });
  }
};
