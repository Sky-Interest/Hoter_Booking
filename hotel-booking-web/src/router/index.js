import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Register from '@/components/Register';
import OrderDetail from '@/components/OrderDetail';
import Payment from '@/components/Payment';
import Rooms from "@/components/Rooms.vue";
import OrderRoom from "@/components/OrderRoom.vue"; // 引入OrderRoom组件

Vue.use(Router);

export default new Router({
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
    // router/index.js 或类似的路由配置文件
    {
      path: '/payment/:orderId',
      name: 'Payment',
      component: () => import('@/components/Payment.vue')
    },
    {
      path: '/order-room', // 新增的路由路径
      name: 'OrderRoom',
      component: OrderRoom // 指定OrderRoom组件为这个路径的处理组件
    },
    // 可以根据需要添加更多路由配置
  ]
});
