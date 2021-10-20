<template>
  <el-card id="introduction">
    <!--<hr />-->
    <p>
      <span style="color:#67C23A" class="el-icon-location-information">公众号</span>
    </p>
    <hr />
    <div>
      <img alt="咸鱼很咸" class="avatar" src="../assets/images/icon.jpg" draggable="false">
    </div>
    <span style="font-size: 20px;font-weight: bold">咸鱼很咸</span>
    <br/>
    <br/>
    {{ introduction }}
    <br/>
  </el-card>
</template>

<script>
import site from '@/api/site';

export default {
  name: 'introduction',
  data() {
    return {
      introduction: ''
    };
  },
  created() {
    site.getSite().then(resp => {
      if (resp.sta === '00') {
        this.introduction = resp.data;
      }
    });
  }

};
</script>
<style scoped>
  #introduction {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    text-align: center;

    margin: 20px 0;
  }

  .avatar {
    width: 90%;
    height: 90%;
  }
</style>
