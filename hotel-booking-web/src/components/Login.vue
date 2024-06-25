<template>
  <el-form @submit.native.prevent="login">
    <el-form-item label="用户名">
      <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
    </el-form-item>
    <el-form-item label="密码">
      <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="login">登录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { loginUser } from '@/api/service';

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    login() {
      console.log(this.loginForm)
       loginUser(this.loginForm).then(response => {
        // 处理登录成功，例如保存token，跳转到主页等
        this.$router.push('/');
      }).catch(error => {
        // 处理登录失败
        console.error("登录失败:", error);
      });
    }
  }
};
</script>
