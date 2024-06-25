<template>
  <el-card class="payment-box">
    <div slot="header">支付信息</div>
    <el-form @submit.native.prevent="submitPayment" ref="paymentForm">
      <el-form-item label="订单号">
        <el-input v-model="paymentForm.orderId" disabled></el-input>
      </el-form-item>
      <el-form-item label="支付金额">
        <el-input v-model="paymentForm.amount" placeholder="请输入支付金额"></el-input>
      </el-form-item>
      <el-form-item label="支付方式">
        <el-select v-model="paymentForm.method" placeholder="请选择支付方式">
          <el-option label="信用卡" value="credit_card"></el-option>
          <el-option label="支付宝" value="alipay"></el-option>
          <el-option label="微信支付" value="wechat"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitPayment">提交支付</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
// 假设有一个API方法 submitPayment 来处理支付逻辑
import { submitPayment } from '@/api/service';

export default {
  data() {
    return {
      paymentForm: {
        orderId: '123456', // 假设订单ID是已知的，实际应用中可能通过路由参数传递
        amount: '',
        method: ''
      }
    };
  },
  methods: {
    submitPayment() {
      // 这里应该有表单验证逻辑
      submitPayment(this.paymentForm).then(response => {
        // 处理支付成功
        this.$message.success('支付成功');
        // 可能需要跳转到订单详情页面或支付成功页面
      }).catch(error => {
        // 处理支付失败
        console.error("支付失败:", error);
        this.$message.error('支付失败，请重试');
      });
    }
  }
};
</script>

<style scoped>
.payment-box {
  max-width: 600px;
  margin: 30px auto;
}
</style>
