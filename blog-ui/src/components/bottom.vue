<template>
  <div id="bottom">
    <div class="line"></div>
    <p id="txt">
      <el-link :underline="false" type="info">© 2021 958小站&nbsp;</el-link>
      <el-link :underline="false" target="_blank" type="info">滇ICP备19004036号</el-link>
    </p>

    <p class="sdfsdf">
      有问题请发邮件至
      <el-link :underline="false" href="mailto:maggieq8324@gmail.com">maggieq8324@gmail.com</el-link>
      或联系站长
      <a
        href="tencent://message/?uin=1936524252&amp;Menu=yes&amp;Service=300&amp;sigT=42a1e5347953b64c5ff3980f8a6e644d4b31456cb0b6ac6b27663a3c4dd0f4aa14a543b1716f9d45"
        title="QQ: 1936524252" draggable="false">
        <img alt="1936524252" class="contact-qq" src="../assets/images/qq.png" draggable="false">
      </a>
    </p>
    <div class="line"/>
  </div>
</template>

<script>
export default {
  name: 'bottom'
};
</script>
<style scoped>
  .line {
    width: 100%;
    height: 4px;
  }

  #bottom {
    color: #303133;
    background: #F2F6FC;
    margin-top: 90px;
  }

  .sdfsdf {
    color: #8c939d;
  }

  .contact-qq {
    width: 20px;
    height: 20px;
  }
</style>
