<template>

  <div id="login">
    <div class="main-box">
      <div class="aside-box">
        <div>
          <div class="aside-font">审批管理系统</div>
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="120px" class="demo-ruleForm"
            :hide-required-asterisk="true" :show-message="true" :validate-on-rule-change="true">
            <div class="login-input">
              <div v-bind:style="[userStyle]">
                <el-form-item label="输入账号" prop="username">
                  <el-input v-model="loginForm.username" @keyup.enter.native="handleLogin" @blur="reUser"
                    prefix-icon="el-icon-user"></el-input>
                </el-form-item>
              </div>
              <div v-bind:style="[passStyle]">
                <el-form-item label="输入密码" prop="password">
                  <el-input v-model="loginForm.password" type="password" autocomplete="off" @blur="rePass"
                    @keyup.enter.native="handleLogin" :prefix-icon="passIcon">
                  </el-input>
                </el-form-item>
              </div>
            </div>
            <div class="phone-form">
              <el-form-item label="" prop="username">
                <el-input v-model="loginForm.username" @keyup.enter.native="handleLogin" @blur="reUser"
                  prefix-icon="el-icon-user" placeholder="请输入账号"></el-input>
              </el-form-item>
              <el-form-item label=" " prop="password" style="margin-top: 20px">
                <el-input v-model="loginForm.password" type="password" autocomplete="off" @blur="rePass"
                  @keyup.enter.native="handleLogin" :prefix-icon="passIcon" placeholder="请输入密码">
                </el-input>
              </el-form-item>
            </div>
            <div class="yzm">
              <el-form-item prop="code" v-if="captchaOnOff">
                <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码"
                  @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" />
              </div>
            </div>
            <el-checkbox v-model="loginForm.rememberMe" style="margin: 15px 0px 0px 0px">记住密码</el-checkbox>
            <div class="aside-btn">
              <el-button @click.native.prevent="handleLogin"><span v-if="!loading">登 录</span>
                <span v-else>登 录 中 ···</span>
              </el-button>
              <el-button disabled>
                <router-link :to="'/register'">立即注册</router-link>
              </el-button>
            </div>
          </el-form>
        </div>
        <div class="aside-right-img" style="position: absolute; top: -45px; left: 295px">
          <div style="postion: relative">
            <img src="../assets/logo/login-right.png" style="width: 630px; position: absolute; z-index: 3" />
            <div class="img-linear"></div>
          </div>
        </div>
      </div>

      <div class="aside-right-box"></div>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
export default {
  name: "Login",
  data () {
    return {
      passIcon: "el-icon-lock",
      userStyle: {
        marginBottom: "0px",
        border: "1px solid #f4f8f7",
      },
      passStyle: {
        border: "1px solid #f4f8f7",
      },
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: "",
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
        ],
        code: [{ required: true, trigger: "blur", message: "请输入验证码" }],
      },
      loading: false,
      // 验证码开关
      captchaOnOff: true,
      // 注册开关
      register: false,
      redirect: undefined,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  created () {
    this.getCode();
    this.getCookie();
  },
  methods: {
    reUser () {
      if (this.loginForm.username == "") {
        this.userStyle.marginBottom = "20px";
        this.userStyle.border = "1px solid red";
      } else {
        this.userStyle.marginBottom = "0px";
        this.userStyle.border = "1px solid #f4f8f7";
      }
    },
    rePass () {
      if (this.loginForm.password == "") {
        this.passStyle.border = "1px solid red";
      } else {
        this.passStyle.border = "1px solid #f4f8f7";
      }
    },
    getCode () {
      getCodeImg().then((res) => {
        this.captchaOnOff =
          res.captchaOnOff === undefined ? true : res.captchaOnOff;
        if (this.captchaOnOff) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    getCookie () {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    handleLogin () {
      this.$refs.loginForm.validate((valid, obj) => {
        if (obj.username != undefined) {
          this.userStyle.marginBottom = "20px";
          this.userStyle.border = "1px solid red";
        } else {
          this.userStyle.marginBottom = "0px";
          this.userStyle.border = "1px solid #f4f8f7";
        }
        if (obj.password != undefined) {
          this.passStyle.border = "1px solid red";
        } else {
          this.passStyle.border = "1px solid #f4f8f7";
        }

        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), {
              expires: 30,
            });
            Cookies.set("rememberMe", this.loginForm.rememberMe, {
              expires: 30,
            });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove("rememberMe");
          }
          this.$store
            .dispatch("Login", this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || "/" }).catch(() => { });
            })
            .catch(() => {
              this.loading = false;
              if (this.captchaOnOff) {
                this.getCode();
              }
            });
        }
      });
    },
  },
};
</script>

<style scoped>
/* aside */
/* .main-box {
  margin: 70px;
  border-radius: 15px;
  box-shadow: 0px 0px 10px 10px #e6e8f1;
} */
/* .el-aside {
  height: 530px;
  background-color: white;
  margin-bottom: 0px;
  border-radius: 15px 0px 0px 15px;
}
.el-main {
  background-color: #e9eef3;
  border-radius: 0px 15px 15px 0px;
} */

#login {
  background-color: #f3f5ff;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 1140px;
}
.main-box {
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 0px 10px 10px #e6e8f1;
  border-radius: 15px;
}
.yzm /deep/ .el-form-item__content {
  display: flex;
  /* justify-content: start; */
  flex-direction: column;
  height: 36px;
}
.yzm {
  margin-top: 20px;
  display: flex;
}
.login-code img {
  height: 36px;
}
.yzm /deep/ .el-input {
  width: 120px;
  margin-right: 25px;
}
.aside-box {
  width: 400px;
  height: 530px;
  border-radius: 15px 0px 0px 15px;
  background-color: white;
  position: relative;
}
.aside-box > div {
  padding: 60px;
}
.aside-logo > img {
  width: 250px;
  display: flex;
  /* justify-content: start; */
}
.aside-font {
  margin: 20px 0;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 4px;
  color: #203052;
  text-align: center;
}
div /deep/ .el-form-item {
  flex-direction: column;
  margin-bottom: 0;
}
div /deep/ .el-form-item__label {
  color: #afa8be;
  text-align: left;
  line-height: normal;
  padding-left: 10px;
  font-weight: 500;
}
.login-input /deep/ .el-input__inner {
  border: 0;
  border-radius: 0;
}

div /deep/ .el-form-item__content {
  border: 0;
  border-radius: 0;
  margin-left: 0px !important;
}
div /deep/ .el-input input {
  color: #6384fb;
}
.aside-btn {
  margin-top: 40px;
  display: flex;
  justify-content: space-between;
}
.aside-btn button {
  width: 125px;
  height: 45px;
  border: 1px solid #6d8cfb;
  color: #6d8cfb;
  font-size: 16px;
}
.aside-btn button:nth-of-type(1) {
  background-color: #6384fb;
  color: #ffff;
  box-shadow: 0 0 5px #6384fb;
  border: 0;
}
.aside-btn button:nth-of-type(2) {
  box-shadow: 0 0 1px inset #6384fb;
}
.password {
  position: absolute;
  height: 100%;
  right: 5px;
  top: 0;
  text-align: center;
  color: #c0c4cc;
}
.phone-form {
  display: none;
}
.img-linear {
  width: 383px;
  height: 150px;
  position: absolute;
  bottom: 0;
  top: 380px;
  background-image: linear-gradient(#c9d1f9, #c9d1f942, #c9d1f90d);
  left: 75px;
  z-index: 2;
}
/* aside-right-box */
.aside-right-box {
  height: 530px;
  border-radius: 0px 15px 15px 0px;
  background-color: #f3f5ff;
  width: 700px;
}
@media screen and (max-width: 768px) {
  .aside-right-box {
    display: none;
  }
  .aside-right-img {
    display: none;
  }
  #login {
    min-width: auto;
    background-color: white;
  }
  .main-box {
    box-shadow: 0px 0px 0px 0px #e6e8f1;
  }
  .aside-box {
    height: 600px;
  }
  .aside-btn {
    margin-top: 80px;
  }
  .aside-logo {
    margin-bottom: 80px;
  }
  .login-input {
    display: none;
  }
  .phone-form {
    display: block;
  }
}
</style>