<template>
  <div id="app">
    <el-menu
      :default-active="activeIndex2"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b">
      <el-menu-item index="1"><router-link to="/">房间总览</router-link></el-menu-item>
      <el-submenu index="2">
        <template slot="title">账号管理</template>
        <el-menu-item index="2-1">账号信息</el-menu-item>
        <el-menu-item index="2-2">修改密码</el-menu-item>
      </el-submenu>
      <el-menu-item v-if="role === 'ADMIN'" index="3"><router-link to="/order-overview">订单总览</router-link></el-menu-item>
      <el-menu-item index="4"><router-link to="/order-room">订购页面</router-link></el-menu-item>
    </el-menu>

    <el-row type="flex" justify="center" class="menu-right">
      <template v-if="!isLoggedIn">
        <el-button @click="navigateTo('/login')">登录</el-button>
        <el-button @click="navigateTo('/register')">注册</el-button>
      </template>
      <template v-else>
        <el-col :span="24" align="middle">
          <div>欢迎您：({{ getRoleDisplayName(role) }})   {{ username }}</div>
          <el-button type="danger" @click="logout">退出</el-button>
        </el-col>
      </template>
    </el-row>

    <router-view/>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      isLoggedIn: false,
      username: '',
      role: ''
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    navigateTo(path) {
      if (this.$route.path !== path) {
        this.$router.push(path);
      }
    },
    logout() {
      // 清除登录信息
      localStorage.removeItem('userInfo');
      this.isLoggedIn = false;
      this.username = '';
      this.role = '';
      // 刷新页面
      this.$router.push('/');
      location.reload();
    },
    checkLoginStatus() {
      // 从localStorage获取用户信息
      const userInfo = JSON.parse(localStorage.getItem('userInfo'));
      if (userInfo && userInfo.username) {
        this.isLoggedIn = true;
        this.username = userInfo.username; // 假设userInfo对象中包含username字段
        this.role = userInfo.role; // 确保userInfo对象中包含role字段
      }
    },
    getRoleDisplayName(role) {
      switch (role) {
        case 'USER':
          return '操作员';
        case 'ADMIN':
          return '管理员';
        default:
          return role;
      }
    }
  },
  mounted() {
    this.checkLoginStatus();
  }
}
</script>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #f0f2f5; /* 设置全局背景色为浅灰色 */
  min-height: 100vh; /* 确保背景色覆盖整个页面 */
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }

  .menu-right .el-col span {
    margin-right: 10px;
  }
}
</style>
