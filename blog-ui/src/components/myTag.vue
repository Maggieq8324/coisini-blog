<template>
  <el-card id="myTag">
    <p>
      <span style="color:#67C23A" class="el-icon-collection-tag">个人标签</span>
    </p>
    <hr/>

    <div v-for="tag in tags">
      <el-tag type="success" closable @close="deleteTag(tag.id)" style="margin-top: 10px" @click="editTag(tag.id)">
        <el-link type="success" :underline="false">{{tag.name}}</el-link>
      </el-tag>
    </div>
    <div style="margin-top: 8%">
      <el-button class="el-icon-plus" size="mini" @click="addTag">新增标签</el-button>
    </div>

  </el-card>
</template>

<script>
import tag from '@/api/tag';

export default {
  name: 'myTag',
  data() {
    return {
      tags: []
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      tag.getTag().then(resp => {
        if (resp.sta === '00') {
          this.tags = resp.data;
        }
      });
    },
    deleteTag(tagId) {
      this.$confirm('是否删除此标签?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        tag.deleteTag(tagId).then(resp => {
          if (resp.sta === '00') {
            this.load();
            this.$message.success('删除成功');
          } else {
            this.$message.error(resp.msg || '删除失败');
          }
        });
      }).catch(() => {});
    },
    editTag(tagId) {
      this.$prompt('请输入修改后的标签名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        if (value == null || value.length <= 0) {
          this.$message.warning('请输入修改后的标签名');
          return;
        }
        tag.updateTag(tagId, value).then(resp => {
          if (resp.sta === '00') {
            this.load();
            this.$message.success('修改成功');
          } else {
            this.$message.error(resp.msg || '修改失败');
          }
        });
      }).catch(() => {
      });
    },
    addTag() {
      this.$prompt('请输入新标签名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        if (value == null || value.length <= 0) {
          this.$message.warning('请输入新标签名');
          return;
        }
        tag.addTag(value).then(resp => {
          if (resp.sta === '00') {
            this.load();
            this.$message.success('新增成功');
          } else {
            this.$message.error(resp.msg || '新增失败');
          }
        });
      }).catch(() => {
      });
    }
  }
};
</script>
<style scoped>
  #myTag {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    margin: 30px 10px;

  }
</style>
