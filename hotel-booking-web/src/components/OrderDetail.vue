<template>
  <div>
    <el-card>
      <div slot="header">订单详情</div>
      <!-- 显示订单详情 -->
      <p>订单号：{{ orderDetail.id }}</p>
      <p>房间号：{{ orderDetail.roomNumber }}</p>
      <p>入住日期：{{ orderDetail.checkInDate }}</p>
      <p>退房日期：{{ orderDetail.checkOutDate }}</p>
      <p>总价：{{ orderDetail.totalPrice }}</p>
      <!-- 其他信息 -->
    </el-card>
  </div>
</template>

<script>
// 假设有一个API方法 getOrderDetail 来获取订单详情
import { getOrderDetail } from '@/api/service';

export default {
  data() {
    return {
      orderDetail: {}
    };
  },
  created() {
    this.fetchOrderDetail();
  },
  methods: {
    fetchOrderDetail() {
      const orderId = this.$route.params.id; // 假设订单ID通过路由参数传递
      getOrderDetail(orderId).then(response => {
        this.orderDetail = response.data;
      }).catch(error => {
        console.error("获取订单详情失败:", error);
      });
    }
  }
};
</script>
