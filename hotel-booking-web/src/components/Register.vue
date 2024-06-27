<template>
  <el-form @submit.native.prevent="register">
    <el-form-item label="用户名">
      <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
    </el-form-item>
    <el-form-item label="邮箱">
      <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
    </el-form-item>
    <el-form-item label="密码">
      <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="register">注册</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { createUser } from '@/api/service';

export default {
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        role: 'USER'
      }
    };
  },
  methods: {
    register() {
      createUser(this.registerForm).then(response => {
        // 处理注册成功
        this.$router.push('/login');
      }).catch(error => {
        // 处理注册失败
        console.error("注册失败:", error);
      });
    }
  }
};
</script>
