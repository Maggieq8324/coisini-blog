<template>
  <el-card id="account">

    <div id="updatePwd">
      <div>
        <h3>更改密码</h3>
        <el-form ref="form" label-width="80px">
          <el-form-item label="旧密码">
            <el-input v-model="oldPassword" placeholder="旧密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="newPassword" placeholder="新密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="confirmPassword" placeholder="再次输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-button type="text" style="width: 100%;color: black">{{mail}}</el-button>
            <el-button type="text" style="width: 85%;" @click="updatePwdSendMail()">
              发送验证码
              <i class="el-icon-loading" v-if="updatePwdSendFlag"/>
            </el-button>
          </el-form-item>
          <el-form-item label="验证码">
            <el-input v-model="updatePwdMailCode" placeholder="收到的验证码"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-left: 45%">
        <el-button icon="el-icon-potato-strips" @click="updatePwd()">更改密码</el-button>
      </div>
    </div>

    <el-divider/>

    <div id="updateMail">
      <div>
        <h3>改绑邮箱</h3>
        <el-form ref="form" label-width="100px">
          <el-form-item label="邮箱">
            <el-button type="text" style="width: 100%;color: black">{{mail}}</el-button>
            <el-button type="text" style="width: 85%;" @click="updateMailSendMailToOld()">
              发送验证码
              <i class="el-icon-loading" v-if="updateMailToOldSendFlag"/>
            </el-button>
          </el-form-item>
          <el-form-item label="原邮箱验证码">
            <el-input v-model="oldMailCode" placeholder="原邮箱验证码"></el-input>
          </el-form-item>
          <el-form-item label="新邮箱">
            <el-input v-model="newMail" placeholder="新邮箱"></el-input>
            <el-button type="text" style="width: 85%;" @click="updateMailSendMailToNew()">
              发送验证码
              <i class="el-icon-loading" v-if="updateMailToNewSendFlag"/>
            </el-button>
          </el-form-item>
          <el-form-item label="新邮箱验证码">
            <el-input v-model="newMailCode" placeholder="新邮箱验证码"></el-input>

          </el-form-item>
        </el-form>
      </div>
      <div style="margin-left: 45%">
        <el-button icon="el-icon-cherry" @click="updateMail()">改绑邮箱</el-button>
      </div>
    </div>

    <el-divider/>

    <div id="updateReward">
      <div>
        <h3>更新打赏码</h3>
        <el-form ref="form" label-width="100px">
          <el-form-item label="当前打赏码">
            <el-image v-if="userReward!==''"
                      style="width: 200px; height: 200px"
                      :src="userReward"
                      :fit="'fit'"/>
            <span v-if="userReward===''">暂无</span>
          </el-form-item>

          <el-form-item label="更新打赏码">
            <el-upload
              class="avatar-uploader"
              :action="upload"
              :headers="getToken()"
              :on-success="handleAvatarSuccess"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>

        </el-form>
      </div>
      <div style="margin-left: 45%">
        <el-button icon="el-icon-orange" @click="updateReward()">更新</el-button>
      </div>
    </div>

  </el-card>
</template>

<script>
import user from '@/api/user';

export default {
  name: 'account',
  data() {
    return {
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
      updatePwdMailCode: '', // 修改密码验证码
      mail: '', // 用户原邮箱
      oldMailCode: '', // 原邮箱验证码
      newMail: '',    // 新邮箱
      newMailCode: '',  // 新邮箱验证码
      updatePwdSendFlag: false,
      updateMailToOldSendFlag: false,
      updateMailToNewSendFlag: false,
      imageUrl: '',
      userReward: '',
      upload: '/api/blog/uploadImg'
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      user.getUserMail().then(resp => {
        if (resp.sta === '00') {
          this.mail = resp.data;
        }
      });
      user.getUserReward().then(resp => {
        if (resp.sta === '00') {
          this.userReward = resp.data;
        } else {
          this.userReward = '';
        }
      });
    },
    getToken() {
      return {'Authorization': this.$store.state.token};
    },
    updateReward() {
      if (this.imageUrl === '') { return; }

      user.updateReward(this.imageUrl).then(resp => {
        if (resp.sta === '00') {
          this.$message.success(resp.message);
          this.load();
        } else {
          this.$message.error(resp.message || '更新失败');
        }
      });
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = res.data;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 1;
      if (!isJPG && !isPNG) {
        this.$message.error('图片不支持除 jpg/png 以外的格式');
      }
      if (!isLt2M) {
        this.$message.error('打赏码图片大小不能超过 1MB!');
      }
      return true;
    },
    updatePwdSendMail() {  // 更改密码发送验证码
      this.updatePwdSendFlag = true;
      this.sendMail(this.mail);
    },
    updateMailSendMailToOld() {  // 更改密码发送验证码到旧邮箱
      this.updateMailToOldSendFlag = true;
      this.sendMail(this.mail);
    },
    updateMailSendMailToNew() {  // 更改密码发送验证码到新邮箱
      this.updateMailToNewSendFlag = true;
      this.sendMail(this.newMail);
    },
    sendMail(mail) {
      var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
      if (!reg.test(mail)) { // 检测字符串是否符合正则表达式
        this.$message.warning('邮箱格式不正确');
        this.updatePwdSendFlag = false;
        this.updateMailToOldSendFlag = false;
        this.updateMailToNewSendFlag = false;
        return;
      }
      user.sendMail(mail).then(resp => {
        if (resp.sta === '00') {
          this.$message.success('发送成功');
          this.updatePwdSendFlag = false;
          this.updateMailToOldSendFlag = false;
          this.updateMailToNewSendFlag = false;
        } else {
          this.updatePwdSendFlag = false;
          this.$message.error(resp.message || '发送失败');
        }
      }).catch(() => {
        this.updatePwdSendFlag = false;
        this.updateMailToOldSendFlag = false;
        this.updateMailToNewSendFlag = false;
      });
    },
    updatePwd() {
      if (this.oldPassword.length <= 0) {
        this.$message.warning('原密码不能为空');
        return;
      }

      if (this.newPassword.length < 6) {
        this.$message.warning('密码不能小于6位');
        return;
      }

      if (this.newPassword !== this.confirmPassword) {
        this.$message.warning('两次输入的密码不一致');
        return;
      }

      if (this.updatePwdMailCode.length <= 0) {
        this.$message.warning('请输入验证码');
        return;
      }

      user.updatePassword(this.oldPassword, this.newPassword, this.updatePwdMailCode).then(resp => {
        if (resp.sta === '00') {
          this.$message.success('修改成功');
          this.$store.commit('logout');// 清除token等信息
          this.$router.push({ // 路由跳转
            path: '/'
          });
        } else {
          this.$message.error(resp.message || '修改失败');
        }
      });
    },
    updateMail() {
      var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
      if (!reg.test(this.newMail)) { // 检测字符串是否符合正则表达式
        this.$message.warning('邮箱格式不正确');
        return;
      }

      if (this.oldMailCode.length <= 0) { //
        this.$message.warning('请填写验证码');
        return;
      }

      if (this.newMailCode.length <= 0) { //
        this.$message.warning('请填写验证码');
        return;
      }

      user.updateMail(this.newMail, this.oldMailCode, this.newMailCode).then(resp => {
        if (resp.sta === '00') {
          this.$message.success('改绑成功');
          this.newMail = '';
          this.oldMailCode = '';
          this.newMailCode = '';
          this.load();
        } else {
          this.$message.error(resp.message || '改绑失败');
        }
      });
    }
  }
};
</script>
<style scoped>
  #account {
    margin: 1% 5%;
    text-align: left;
  }

  #updatePwd {
    padding-left: 30%;
    width: 30%;
  }

  #updateMail {
    padding-left: 30%;
    width: 30%;
  }

  #updateReward {
    padding-left: 30%;
    width: 30%;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #000000;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #000000;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 176px;
    height: 176px;
    text-align: center;
  }

  .avatar {
    width: 176px;
    height: 176px;
    display: block;
  }

</style>
