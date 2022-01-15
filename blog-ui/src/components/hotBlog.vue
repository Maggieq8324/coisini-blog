<template>
  <el-card id="hotBlog">
    <!--<hr />-->
    <p>
      <span style="color:#67C23A" class="el-icon-document">热门文章</span>
    </p>
    <hr />
    <div v-for="blog in hotBlog">
      <el-link type="info" style="margin: 5px 0" :underline="false"  @click="router(blog.id)">
        《{{blog.title}}》&nbsp;浏览数:&nbsp;{{blog.blogViews}}
      </el-link>
    </div>
    <br/>
  </el-card>
</template>

<script>
import blog from '@/api/blog';

export default {
  name: 'hotBlog',
  data() {
    return {
      hotBlog: []
    };
  },
  created() {
    blog.getHotBlog().then(resp => {
      if (resp.sta === '00') {
        this.hotBlog = resp.data;
      }
    });
  },
  methods: {
    router(id) {
      scrollTo(0, 0);
      this.$router.push({ // 路由跳转
        path: '/blog/article/' + id
      });
    }
  }

};
</script>
<style scoped>
  #hotBlog {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    text-align: center;

    margin: 20px 0;
  }
</style>
