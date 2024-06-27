import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Register from '@/components/Register';
import OrderDetail from '@/components/OrderDetail';
import Payment from '@/components/Payment';
import Rooms from "@/components/Rooms.vue";
import OrderRoom from "@/components/OrderRoom.vue";
import OrderOverview from "@/components/OrderOverview.vue";
import UserManage from "@/components/UserManage.vue"; // 引入 UserManage 组件

Vue.use(Router);

const router = new Router({
  mode: 'history', // 使用 history 模式来避免 URL 中的 #
  routes: [
    {
      path: '/',
      name: 'Rooms',
      component: Rooms
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/order-detail/:orderId',
      name: 'OrderDetail',
      component: OrderDetail
    },
    {
      path: '/order-overview',
      name: 'OrderOverview',
      component: OrderOverview,
      meta: { requiresAuth: true, allowedRoles: ['ADMIN'] } // 添加路由元信息
    },
    {
      path: '/payment/:orderId',
      name: 'Payment',
      component: () => import('@/components/Payment.vue')
    },
    {
      path: '/order-room',
      name: 'OrderRoom',
      component: OrderRoom
    },
    {
      path: '/user-manage', // 新增用户管理页面路由
      name: 'UserManage',
      component: UserManage,
      meta: { requiresAuth: true, allowedRoles: ['ADMIN'] } // 添加路由元信息
    },
    // 可以根据需要添加更多路由配置
  ]
});

// 添加全局导航守卫
router.beforeEach((to, from, next) => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo'));
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 需要登录才能访问的路由
    if (!userInfo) {
      next('/login'); // 未登录，跳转到登录页面
    } else {
      if (to.matched.some(record => record.meta.allowedRoles && !record.meta.allowedRoles.includes(userInfo.role))) {
        next('/'); // 没有权限，跳转到首页
      } else {
        next(); // 有权限，继续导航
      }
    }
  } else {
    next(); // 不需要登录，继续导航
  }
});

export default router;
