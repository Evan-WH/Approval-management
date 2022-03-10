<template>
  <div class="app-container home">

    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>审批管理系统v1.0.0</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <audio controls="controls" hidden src="../assets/mp3/15011.mp3" ref="audio"></audio>
  </div>
</template>

<script>
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from "@/api/system/message";
import { getUserProfile } from "@/api/system/user";
export default {
  name: "Index",
  dicts: ['sys_notice_status'],
  data () {
    return {
      messageList: '',
      // 版本号
      version: "3.8.1",
      myMessage: [],
      timer: null,
      readMessage: [],
      user: []
    };
  },
  methods: {
    goTarget (href) {
      window.open(href, "_blank");
    },
    /** 查询审批通过通知列表 */
    getList () {
      this.loading = true;
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    showNotify () {
      this.myMessage = []
      this.readMessage = []
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows;
        this.total = response.total;
        this.loading = false;
        getUserProfile().then(response => {
          this.user = response.data;
          this.roleGroup = response.roleGroup;
          this.postGroup = response.postGroup;
          //   门岗角色通知
          for (let i = 0; i < this.messageList.length; i++) {
            if (parseInt(this.messageList[i].roleId) == this.user.roles[0].roleId) {
              this.myMessage.push(this.messageList[i])
            }
          }
          for (let j = 0; j < this.myMessage.length; j++) {
            if (this.myMessage[j].messageStatus != 1) {
              this.readMessage.push(this.myMessage[j])
            }
          }
          if (this.readMessage[0].Status != 1) {
            const h = this.$createElement;
            this.$refs.audio.play()
            this.$notify({
              dangerouslyUseHTMLString: true,
              title: this.myMessage[0].messageTitle,
              message: h('span', { style: 'color: pink' }, this.myMessage[0].messageContent,
              )
            });
          } else {
            return 0;
          }
        });



      });
    },
  },
  mounted () {
    // *5分钟定时刷新投诉条数
    this.timer = setInterval(() => {
      this.showNotify();
    }, 10000)
  },
  beforeDestroy () {
    clearInterval(this.timer);
  },
  created () {
    // this.showNotify()
    this.getList()
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

<style lang="scss">
/**
修改notify样式
 */
.notify-msg {
  margin: 0;
  padding: 0;
  width: 440px;
  .el-notification__group {
    margin: 0;
    padding: 0;
  }
}
</style>


<style lang="scss" scoped>
.notify-msg-box {
  margin: 0;
  padding: 0;
  .notify-msg-top {
    display: flex;
    padding: 30px 30px 30px 20px;
    .notify-msg-icon {
      .image {
        width: 60px;
        height: 60px;
        background-image: url("../assets/logo/logo.png");
        background-repeat: no-repeat;
        background-size: 100% 100%;
        -moz-background-size: 100% 100%;
      }
    }
    .notify-msg-top-right {
      padding-left: 30px;
      .notify-msg-title {
        font-weight: 700;
        font-size: 20px;
        color: #333333;
      }
      .notify-msg-context {
        padding-top: 15px;
        color: #666666;
        font-size: 14px;
      }
    }
  }
  .notify-msg-bottom {
    display: flex;
    height: 50px;
    width: 440px;
    justify-content: space-between;
    align-items: center;
    display: flex;
    background-color: #f1f1f1;
    .notify-msg-send {
      padding-left: 15px;
      width: 220px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .notify-msg-time {
      text-align: right;
      width: 220px;
      padding-right: 15px;
    }
  }
  .notify-bottom {
    width: 440px;
    display: flex;
    flex-direction: row-reverse;
    .el-button {
      margin-bottom: 20px;
      margin-right: 30px;
      color: white;
      font-weight: 700;
      width: 100px;
      padding-top: 12px;
      padding-bottom: 12px;
      font-size: 14px;
      background-color: #409eff;
    }
  }
}
</style>



