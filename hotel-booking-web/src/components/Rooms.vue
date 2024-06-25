<template>
  <div>
    <el-table :data="rooms" style="width: 100%">
      <el-table-column prop="roomNumber" label="房间号"></el-table-column>
      <el-table-column prop="type" label="类型"></el-table-column>
      <el-table-column prop="price" label="价格"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <!-- 更多列 -->
    </el-table>
  </div>
</template>

<script>
import { getRooms } from '@/api/service';

export default {
  data() {
    return {
      rooms: []
    };
  },
  created() {
    this.fetchRooms();
  },
  methods: {
    fetchRooms() {
      getRooms().then(response => {
        this.rooms = response.data;
      }).catch(error => {
        console.error("获取房间列表失败:", error);
      });
    }
  }
};
</script>
