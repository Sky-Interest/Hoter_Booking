<template>
  <el-card class="payment-box">
    <div slot="header">支付信息</div>
    <el-form @submit.native.prevent="submitPayment" ref="paymentForm">
      <el-form-item label="订单号">
        <el-input v-model="paymentForm.orderId" disabled></el-input>
      </el-form-item>
      <el-form-item label="支付金额">
        <el-input v-model="paymentForm.amount" disabled></el-input>
      </el-form-item>
      <el-form-item label="支付方式">
        <el-select v-model="paymentForm.method" placeholder="请选择支付方式">
          <el-option label="信用卡" value="credit_card"></el-option>
          <el-option label="支付宝" value="alipay"></el-option>
          <el-option label="微信支付" value="wechat"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="cancelPayment">取消支付</el-button>
        <el-button type="primary" @click="submitPayment">提交支付</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { getOrderDetail, submitPayment,updateOrderStatus } from '@/api/service';

export default {
  data() {
    return {
      paymentForm: {
        orderId: '',
        amount: '',
        method: ''
      }
    };
  },
  methods: {
    submitPayment() {
      const paymentData = {
        order: {id: this.paymentForm.orderId},
        amount: this.paymentForm.amount,
        method: this.paymentForm.method,
      };

      submitPayment(paymentData).then(response => {
        this.$message.success('支付成功');
        this.$router.push({ name: 'OrderDetail', params: { orderId: this.paymentForm.orderId } });
        // 支付成功后的处理逻辑
      }).catch(error => {
        console.error("支付失败:", error);
        this.$message.error('支付失败，请重试');
      });
    },
    cancelPayment() {
      // 假设有一个API函数来更新订单状态
      updateOrderStatus(this.paymentForm.orderId, 'CANCELLED').then(() => {
        this.$message.info('支付已取消');
        this.$router.push('/order-room');
      }).catch(error => {
        console.error("取消支付失败:", error);
        this.$message.error('操作失败，请重试');
      });
    },
    loadOrderDetails() {
      const orderId = this.$route.params.orderId;
      this.paymentForm.orderId = orderId;
      getOrderDetail(orderId).then(response => {
        this.paymentForm.amount = response.data.totalPrice;
        // 设置支付金额和其他订单信息
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
.payment-box {
  max-width: 600px;
  margin: 30px auto;
}
</style>
