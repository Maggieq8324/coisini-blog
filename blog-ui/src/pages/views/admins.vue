<template>
  <div style="width: 99%">
    <el-container>
      <el-aside style="width: 20%; margin-bottom: -3%">
        <div id="aside">
          <el-menu :default-active="activeIndex" class="el-menu-vertical-demo" @select="handleSelect">
            <el-menu-item class="menuItem" index="1">
              <i class="el-icon-user-solid"></i>
              <span slot="title">用户管理</span>
            </el-menu-item>

            <el-menu-item class="menuItem" index="2" route="/blog/admins/codeManage">
              <i class="el-icon-menu"></i>
              <span slot="title">邀请码管理</span>
            </el-menu-item>

            <el-menu-item class="menuItem" index="3" route="/blog/admins/announcementManage">
              <i class="el-icon-data-board"></i>
              <span slot="title">公告管理</span>
            </el-menu-item>

            <el-menu-item class="menuItem" index="4" route="/blog/admins/blogManage">
              <i class="el-icon-document"></i>
              <span slot="title">博客管理</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      <el-main style="width: 75%">
        <user-manage v-if="activeIndex==='1'"/>
        <code-manage v-if="activeIndex==='2'"/>
        <announcement v-if="activeIndex==='3'"/>
        <blog-manage v-if="activeIndex==='4'"/>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import UserManage from './userManage';
import CodeManage from './codeManage';
import BlogManage from './blogManage';
import Announcement from './announcementManage';

export default {
  name: 'admins',
  components: {Announcement, BlogManage, CodeManage, UserManage},
  data() {
    return {
      activeIndex: '1'
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      this.activeIndex = key;
    }
  }
};
</script>
<style scoped>
  .menuItem {
    margin: 15% 0;
  }

  #aside {
    margin: 10px 0px 10px 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }
</style>
