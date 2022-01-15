import Vue from 'vue';
import Router from 'vue-router';

import index from '@/pages/views/index';
import message from '@/pages/views/message';
import announcement from '@/pages/views/announcement';
import newBlog from '@/pages/views/newBlog';
import account from '@/pages/views/account';
import admins from '@/pages/views/admins';
import forgetPwd from '@/pages/views/forgetPwd';
import searchBlog from '@/pages/views/searchBlog';
import blog from '@/pages/views/blog';
import myBlog from '@/pages/views/myBlog';
import editBlog from '@/pages/views/editBlog';
import notfound from '@/pages/views/notfound';
// import userManage from '@/pages/views/userManage';
// import codeManage from '@/pages/views/codeManage';
// import announcementManage from '@/pages/views/announcementManage';
// import blogManage from '@/pages/views/blogManage';
import home from '@/pages/home'; // 博客主页

Vue.use(Router);

export default new Router({
  mode: 'history',
  // base: 'blog',
  routes: [
    {
      path: '/blog', // 博客首页
      component: home,
      children: [
        {
          path: '/',
          component: index
        },
        {
          path: '/blog/article/:blogId',
          name: 'blog',
          component: blog
        },
        {
          path: '/blog/myBlog',
          name: 'myBlog',
          component: myBlog
        },
        {
          path: '/editBlog/:blogId',
          name: 'editBlog',
          component: editBlog
        },
        {
          path: '/blog/newBlog',
          name: 'newBlog',
          component: newBlog
        },
        {
          path: '/blog/message',
          name: 'message',
          component: message
        },
        {
          path: '/blog/announcement',
          name: 'announcement',
          component: announcement
        },
        {
          path: '/blog/account',
          name: 'account',
          component: account
        },
        {
          path: '/blog/forgetPwd',
          name: 'forgetPwd',
          component: forgetPwd
        },
        {
          path: '/blog/searchBlog/:searchTxt',
          name: 'searchBlog',
          component: searchBlog
        },
        {
          path: '/blog/admins',
          name: 'admins',
          component: admins
        },
        {
          path: '*',
          name: 'notfound',
          component: notfound
        }
      ]
    }
  ]
});
