<template>
  <div id="newBlog">

    <el-card style="height: 700px;">
      <el-input
        type="text"
        placeholder="请输入标题"
        v-model="title"
        maxlength="100"
        show-word-limit/>
      <br/><br/>
      <mavon-editor ref=md
                    @save="save()"
                    v-model="body"
                    id="editor"
                    @imgAdd="$uploadImg"
                    @imgDel="$imgDel"
                    placeholder="## Start"/>
      <!-- 以下是预览模式配置 -->
      <!--:toolbarsFlag="false"  :subfield="false" defaultOpen="preview"-->

    </el-card>

    <el-card id="tags" v-if="tags.length>0">
      <div>
        <p class="el-icon-mouse">选择一个以上标签</p>
        <el-checkbox-group v-model="checkboxGroup">
          <el-checkbox v-for="tag in tags" :key="tag.id" :label="tag.id" border
            style="margin-top: 10px">
            {{tag.name}}
          </el-checkbox>
        </el-checkbox-group>
      </div>

      <el-button style="margin-top: 3%;" type="primary" plain class="el-icon-document-checked" @click="sendBlog">
        发布
      </el-button>
    </el-card>

    <el-card v-if="tags.length<=0">
      没有标签,请先添加标签
    </el-card>

  </div>

</template>

<script>
import tag from '@/api/tag';
import blog from '@/api/blog';
import file from '@/utils/file';
import { global } from '@/config/global';

export default {

  name: 'newBlog',
  data() {
    return {
      title: '',
      body: '',
      tags: [],
      checkboxGroup: [],
      img_file: {}
    };
  },
  methods: {
    sendBlog() { // 发布博客
      if (this.checkboxGroup.length <= 0 || this.title.length <= 0 || this.body.length <= 0) {
        this.$message.warning('内容不允许为空');
        return;
      }
      var tags = this.checkboxGroup;
      var tagStr = '';
      for (var i = 0; i < tags.length; i++) {
        if (i !== tags.length - 1) { tagStr = tagStr + tags[i] + ','; } else { tagStr = tagStr + tags[i]; }
      }

      blog.sendBlog(this.title, this.body, tagStr).then(resp => {
        if (resp.sta === '00') {
          this.$alert(resp.message, '提示', {
            confirmButtonText: '确定',
            callback: action => {
              if (action === 'confirm') {
                scrollTo(0, 0);
                // history.back();
                window.location.href = '/blog';
              }
            }
          });
        } else {
          this.$message.error(resp.message || '发布失败');
        }

      });
    },
    $uploadImg(pos, $file) { // 图片上传
      // 第一步.将图片上传到服务器.
      var formdata = new FormData();
      formdata.append('file', $file);
      blog.uploadImg(formdata).then(res => {
        // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
        this.$refs.md.$img2Url(pos, global.apiBaseUrl + res.data);
      });
    },
    $imgDel(pos) {
      delete this.img_file[pos];
    },
    save() {
      if (this.title.length > 0 && this.body.length > 0) {
        file.generateTxt(this.title, this.body + '\n' + new Date().toLocaleString());
      }
    }
  },
  created() {
    tag.getTag().then(res => {
      if (res.sta === '00') {
        this.tags = res.data;
      }
    });
  }
};
</script>
<style>
  #newBlog {
    margin: 20px 5% 0 5%;
  }

  #tags {
    margin-top: 1%;
  }

  #editor {
    height: 600px;
  }

  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: #67C23A;
  }

  .el-checkbox.is-bordered.is-checked {
    border-color: #67C23A;
  }

  .el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner {
    background-color: #67C23A;
    border-color: #67C23A;
  }
</style>
