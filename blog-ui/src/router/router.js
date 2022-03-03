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

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: index
    },
    {
      path: '/article/:blogId',
      name: 'blog',
      component: blog
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
      path: '/newBlog',
      name: 'newBlog',
      component: newBlog
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
      path: '/account',
      name: 'account',
      component: account
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
      path: '/admins',
      name: 'admins',
      component: admins
    },
    {
      path: '*',
      name: 'notfound',
      component: notfound
    }
  ]
});
