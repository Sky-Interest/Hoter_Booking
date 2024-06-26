<template>
  <el-card class="order-detail-box">
    <div slot="header">订单详情</div>
    <el-form ref="orderDetailForm">
      <el-form-item label="订单号">
        <el-input v-model="order.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户">
        <el-input v-model="order.user.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="房间号">
        <el-input v-model="order.room.roomNumber" disabled></el-input>
      </el-form-item>
      <el-form-item label="入住日期">
        <el-input v-model="order.checkInDate" disabled></el-input>
      </el-form-item>
      <el-form-item label="退房日期">
        <el-input v-model="order.checkOutDate" disabled></el-input>
      </el-form-item>
      <el-form-item label="总价">
        <el-input v-model="order.totalPrice" disabled></el-input>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { getOrderDetail } from '@/api/service';

export default {
  data() {
    return {
      order: {}
    };
  },
  methods: {
    loadOrderDetails() {
      const orderId = this.$route.params.orderId;
      getOrderDetail(orderId).then(response => {
        this.order = response.data;
      }).catch(error => {
        console.error("加载订单详情失败:", error);
      });
    }
  },
  mounted() {
    this.loadOrderDetails();
  }
};
</script>

<style scoped>
.order-detail-box {
  max-width: 600px;
  margin: 30px auto;
}
</style>
