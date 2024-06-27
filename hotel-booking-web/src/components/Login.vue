<template>
  <div class="login-container">
    <el-card class="login-box">
      <div slot="header" class="login-header">登录</div>
      <el-form @submit.native.prevent="login" ref="loginForm">
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
    </el-card>
  </div>
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
      loginUser(this.loginForm).then(response => {
        // 假设后端返回的用户信息包含在response.data中，并且包含user_id
        const userInfo = response.data;
        localStorage.setItem('userInfo', JSON.stringify(userInfo)); // 将用户信息保存到localStorage
        this.$router.push('/');
        location.reload();
      }).catch(error => {
        console.error("登录失败:", error);
      });
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-box {
  width: 400px;
  padding: 20px;
}

.login-header {
  font-size: 24px;
  text-align: center;
}

.login-button {
  width: 100%;
}
</style>
