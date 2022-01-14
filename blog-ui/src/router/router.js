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
import userManage from '@/pages/views/userManage';
import codeManage from '@/pages/views/codeManage';
import announcementManage from '@/pages/views/announcementManage';
import blogManage from '@/pages/views/blogManage';
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
          path: '/message',
          name: 'message',
          component: message
        },
        {
          path: '/announcement',
          name: 'announcement',
          component: announcement
        },
        {
          path: '/newBlog',
          name: 'newBlog',
          component: newBlog
        },
        {
          path: '/account',
          name: 'account',
          component: account
        },
        {
          path: '/admins',
          name: 'admins',
          component: admins,
          children: [  // 这里就是二级路由的配置
            {
              path: 'userManage',
              name: 'userManage',
              component: userManage
            },
            {
              path: 'codeManage',
              name: 'codeManage',
              component: codeManage
            },
            {
              path: 'announcementManage',
              name: 'announcementManage',
              component: announcementManage
            },
            {
              path: 'blogManage',
              name: 'blogManage',
              component: blogManage
            }
          ]
        },
        {
          path: '/forgetPwd',
          name: 'forgetPwd',
          component: forgetPwd
        },
        {
          path: '/searchBlog/:searchTxt',
          name: 'searchBlog',
          component: searchBlog
        },
        {
          path: '/blog/:blogId',
          name: 'blog',
          component: blog,
        },
        {
          path: '/myBlog',
          name: 'myBlog',
          component: myBlog
        },
        {
          path: '/editBlog/:blogId',
          name: 'editBlog',
          component: editBlog
        },
        {
          path: '*',
          name: 'notfound',
          component: notfound
        }
      ]
    },


    // {
    //   path: '/resume',
    //   name: 'resume',
    //   component: resume
    // },
    // {
    //   path: '/',
    //   name: 'index',
    //   component: index
    // },
    // {
    //   path: '/message',
    //   name: 'message',
    //   component: message
    // },
    // {
    //   path: '/announcement',
    //   name: 'announcement',
    //   component: announcement
    // },
    // {
    //   path: '/newBlog',
    //   name: 'newBlog',
    //   component: newBlog
    // },
    // {
    //   path: '/account',
    //   name: 'account',
    //   component: account
    // },
    // {
    //   path: '/admins',
    //   name: 'admins',
    //   component: admins,
    //   children: [  // 这里就是二级路由的配置
    //     {
    //       path: 'userManage',
    //       name: 'userManage',
    //       component: userManage
    //     },
    //     {
    //       path: 'codeManage',
    //       name: 'codeManage',
    //       component: codeManage
    //     },
    //     {
    //       path: 'announcementManage',
    //       name: 'announcementManage',
    //       component: announcementManage
    //     },
    //     {
    //       path: 'blogManage',
    //       name: 'blogManage',
    //       component: blogManage
    //     }
    //   ]
    // },
    // {
    //   path: '/forgetPwd',
    //   name: 'forgetPwd',
    //   component: forgetPwd
    // },
    // {
    //   path: '/searchBlog/:searchTxt',
    //   name: 'searchBlog',
    //   component: searchBlog
    // },
    // {
    //   path: '/blog/:blogId',
    //   name: 'blog',
    //   component: blog
    // },
    // {
    //   path: '/myBlog',
    //   name: 'myBlog',
    //   component: myBlog
    // },
    // {
    //   path: '/editBlog/:blogId',
    //   name: 'editBlog',
    //   component: editBlog
    // },
    // {
    //   path: '*',
    //   name: 'notfound',
    //   component: notfound
    // },

  ]
});
