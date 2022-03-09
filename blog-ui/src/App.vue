<template>
  <div class="main-content" id="scollStyle">
    <div id="app">
      <bar/>
      <router-view/>
      <bottom/>
    </div>
  </div>

</template>

<script>
import bar from '@/components/bar';
import bottom from '@/components/bottom';

export default {
  components: {bar, bottom},
  name: 'App'
};
</script>

<style scoped>
  #scollStyle::-webkit-scrollbar
  {
    width: 4px;
    background-color: #F5FBFE;
  }
  .main-content {
    position: absolute;
    overflow-y: auto;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 0;
    font-size: 15px;
    font-family: 'KaiTi' !important;
    /*background: rgba(209, 212, 212, 0.5);*/
  }
  #app {
    /*font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;*/
    text-align: center;
    margin-top: 55px;
    background-color: rgba(242, 245, 252, 0.3);
    width: 100%;
    height: 100%;
  }

</style>
