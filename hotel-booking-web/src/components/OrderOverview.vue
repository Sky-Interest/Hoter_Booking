<template>
  <el-card class="order-overview-box">
    <div slot="header">订单总览</div>
    <el-table :data="paginatedOrders" style="width: 100%">
      <el-table-column prop="id" label="订单号" width="180"></el-table-column>
      <el-table-column prop="user.username" label="用户"></el-table-column>
      <el-table-column prop="room.roomNumber" label="房间号"></el-table-column>
      <el-table-column prop="checkInDate" label="入住日期"></el-table-column>
      <el-table-column prop="checkOutDate" label="退房日期"></el-table-column>
      <el-table-column prop="totalPrice" label="总价"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="viewOrderDetail(scope.row.id)" type="primary" size="mini">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="orders.length">
    </el-pagination>
  </el-card>
</template>

<script>
import { getOrders } from '@/api/service';

export default {
  data() {
    return {
      orders: [],
      currentPage: 1,
      pageSize: 5
    };
  },
  computed: {
    paginatedOrders() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.orders.slice(start, end);
    }
  },
  methods: {
    loadOrders() {
      getOrders().then(response => {
        this.orders = response.data;
      }).catch(error => {
        console.error("加载订单信息失败:", error);
      });
    },
    viewOrderDetail(orderId) {
      this.$router.push({ name: 'OrderDetail', params: { orderId: orderId } });
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.currentPage = 1; // 重置为第一页
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
    }
  },
  mounted() {
    this.loadOrders();
  }
};
</script>

<style scoped>
.order-overview-box {
  max-width: 1000px;
  margin: 30px auto;
}
</style>
