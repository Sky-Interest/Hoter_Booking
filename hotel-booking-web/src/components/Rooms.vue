<template>
  <div>
    <div class="header">
      房间信息
      <el-button v-if="role === 'ADMIN'" type="primary" @click="showAddRoomDialog = true">增加房间</el-button>
    </div>
    <el-table :data="rooms" style="width: 100%">
      <el-table-column prop="roomNumber" label="房间号"></el-table-column>
      <el-table-column prop="type" label="类型"></el-table-column>
      <el-table-column prop="price" label="价格"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="description" label="备注"></el-table-column>
      <el-table-column v-if="role === 'ADMIN'" label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showEditDialog(scope.row)">修改</el-button>
          <el-button size="mini" type="danger" @click="confirmDelete(scope.row)">维护</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalRooms">
    </el-pagination>

    <!-- 添加房间的对话框 -->
    <el-dialog :visible.sync="showAddRoomDialog" title="增加房间">
      <el-form :model="addRoomForm">
        <el-form-item label="房间号">
          <el-input v-model="addRoomForm.roomNumber" placeholder="请输入房间号"></el-input>
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="addRoomForm.type" placeholder="请选择房间类型">
            <el-option label="单人间" value="SINGLE"></el-option>
            <el-option label="双人间" value="DOUBLE"></el-option>
            <el-option label="套间" value="SUITE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="addRoomForm.price" :min="0" label="价格"></el-input-number>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="addRoomForm.description" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showAddRoomDialog = false">取消添加</el-button>
        <el-button type="primary" @click="addRoom">确认添加</el-button>
      </span>
    </el-dialog>

    <!-- 修改房间信息的对话框 -->
    <el-dialog :visible.sync="showEditRoomDialog" title="修改房间信息">
      <el-form :model="editRoomForm">
        <el-form-item label="房间号">
          <el-input v-model="editRoomForm.roomNumber" disabled placeholder="请输入房间号"></el-input>
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="editRoomForm.type" placeholder="请选择房间类型">
            <el-option label="单人间" value="SINGLE"></el-option>
            <el-option label="双人间" value="DOUBLE"></el-option>
            <el-option label="套间" value="SUITE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="editRoomForm.price" :min="0" label="价格"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editRoomForm.status" placeholder="请选择状态">
            <el-option label="可用" value="AVAILABLE"></el-option>
            <el-option label="已预订" value="BOOKED"></el-option>
            <el-option label="维护中" value="MAINTENANCE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editRoomForm.description" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showEditRoomDialog = false">取消修改</el-button>
        <el-button type="primary" @click="editRoom">确认修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getRooms, addRoom, updateRoom, removeRoom } from '@/api/service';

export default {
  data() {
    return {
      rooms: [],
      showAddRoomDialog: false,
      showEditRoomDialog: false,
      addRoomForm: {
        roomNumber: '',
        type: '',
        price: 0,
        status: 'AVAILABLE',
        description: ''
      },
      editRoomForm: {
        id: '',
        roomNumber: '',
        type: '',
        price: 0,
        description: '',
        status: ''
      },
      // 分页相关数据
      currentPage: 1,
      pageSize: 10,
      totalRooms: 0,
      role: ''
    };
  },
  methods: {
    fetchRooms() {
      getRooms(this.currentPage, this.pageSize).then(response => {
        this.rooms = response.data.content; // 假设后端返回分页后的房间数据
        this.totalRooms = response.data.totalElements; // 假设后端返回总房间数
      }).catch(error => {
        console.error("获取房间列表失败:", error);
      });
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.fetchRooms();
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.fetchRooms();
    },
    addRoom() {
      addRoom(this.addRoomForm).then(() => {
        this.showAddRoomDialog = false;
        this.fetchRooms(); // 重新获取房间列表
      }).catch(error => {
        console.error("添加房间失败:", error);
      });
    },
    showEditDialog(row) {
      this.editRoomForm = Object.assign({}, row); // 复制行数据到表单
      this.showEditRoomDialog = true;
    },
    editRoom() {
      updateRoom(this.editRoomForm).then(() => {
        this.showEditRoomDialog = false;
        this.fetchRooms(); // 重新获取房间列表
      }).catch(error => {
        console.error("修改房间信息失败:", error);
      });
    },
    confirmDelete(row) {
      this.$confirm(`是否确认删除房间号为 ${row.roomNumber} 的房间?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRoom(row.id);
      }).catch(() => {
        // 取消删除操作
      });
    },
    deleteRoom(roomId) {
      removeRoom(roomId).then(() => {
        this.fetchRooms(); // 重新获取房间列表
      }).catch(error => {
        console.error("删除房间失败:", error);
      });
    },
    checkLoginStatus() {
      // 从localStorage获取用户信息
      const userInfo = JSON.parse(localStorage.getItem('userInfo'));
      if (userInfo && userInfo.role) {
        this.role = userInfo.role; // 确保userInfo对象中包含role字段
      }
    }
  },
  created() {
    this.checkLoginStatus();
    this.fetchRooms();
  },
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
