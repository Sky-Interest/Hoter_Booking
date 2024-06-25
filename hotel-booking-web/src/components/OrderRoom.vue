<template>
  <div>
    <!-- 显示可用房间数量和订房按钮 -->
    <el-button @click="fetchAvailableRooms">查看可用房间</el-button>
    <p>可用房间数量：{{ availableRooms.length }}</p>

    <!-- 显示已选房间信息 -->

    <div v-if="bookedRoom.length" style="width:100%;height: 30%;">
      <div v-for="item in bookedRoom" :key="item.id">
        <p>已选房间：{{ item.roomNumber }}</p>
      </div>
    </div>

    <div v-if="bookedRoom.length" >
      <el-date-picker v-model="checkOutDate" type="date" placeholder="选择结束时间" :picker-options="pickerOptions" />
      <p>您订购了 {{ daysBooked }} 天，需要 {{ totalPrice }} 元。</p>
      <el-button @click="checkout">结算</el-button>
      <el-button @click="clear">清空房间信息</el-button>
    </div>

    <!-- 选择房间的对话框 -->
    <el-dialog :visible.sync="showRoomSelector" title="选择房间" width="30%">
      <el-select v-model="selectedRoomType" filterable placeholder="筛选房间类型" @change="filteredRooms">
        <el-option v-for="type in roomTypes" :key="type" :label="type" :value="type"></el-option>
      </el-select>
      <el-table :data="filteredRooms" style="width: 100%" @row-click="handleRoomSelection">
        <el-table-column property="roomNumber" label="房间号"></el-table-column>
        <el-table-column property="type" label="类型"></el-table-column>
        <el-table-column property="price" label="价格"></el-table-column>
      </el-table>
    </el-dialog>

    <!-- 确认订房对话框 -->
    <el-dialog :visible.sync="showConfirmDialog" title="确认订房" @close="resetSelection">
      <p>是否订购此房间？</p>
      <span slot="footer">
        <el-button @click="showConfirmDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmBooking">确定</el-button>
      </span>
    </el-dialog>



  </div>
</template>

<script>
import { getAvailableRooms, createOrder } from '@/api/service';

export default {
  data() {
    return {
      availableRooms: [],
      showRoomSelector: false,
      showConfirmDialog: false,
      selectedRoomType: '',
      selectedRoom: {},
      bookedRoom: [],
      checkOutDate: '',
      roomTypes: ['SINGLE', 'DOUBLE', 'SUITE'], // 根据你的数据调整
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now();
        },
      }
    };
  },
  computed: {
    filteredRooms() {
      return this.selectedRoomType ? this.availableRooms.filter(room => room.type === this.selectedRoomType) : this.availableRooms;
    },
    daysBooked() {
      if (!this.checkOutDate) return 0;
      const checkInDate = new Date();
      const checkOutDate = new Date(this.checkOutDate);
      const diffTime = Math.abs(checkOutDate - checkInDate);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays;
    },
    totalPrice() {
      let sum = 0;
      this.bookedRoom.forEach(item=>{
        sum += item.price * this.daysBooked ;
      })
      return sum ;
    }
  },
  methods: {
    fetchAvailableRooms() {
      getAvailableRooms().then(response => {
        this.availableRooms = response.data;
        this.showRoomSelector = true;
      });
    },
    handleRoomSelection(room) {
      this.selectedRoom = room;
      this.showRoomSelector = false;
      this.showConfirmDialog = true;
    },
    confirmBooking() {
      if(this.bookedRoom.length==0 || !this.bookedRoom.find(i=>i.id==this.selectedRoom.id)){
        this.bookedRoom.push(this.selectedRoom);
        this.showConfirmDialog = false;
        this.selectedRoom = {};
      }
    },
    checkout() {
      const roomIds = this.bookedRoom.map(room => room.id);

      const orderData = {
        user: { id: 1 },
        room: this.availableRooms.find(room => roomIds.includes(room.id)),
        checkInDate: new Date().toISOString().slice(0, 10),
        checkOutDate: this.checkOutDate,
        status: 'PENDING',
        totalPrice: this.totalPrice
      };

      createOrder(orderData).then(response => {
        const orderId = response.data.id; // 获取后端返回的订单ID
        console.log('订单创建成功，订单ID为', orderId);

        // 跳转到支付页面，并将订单ID作为参数传递
        this.$router.push({ name: 'Payment', params: { orderId: orderId } });

        this.resetSelection();
      }).catch(error => {
        console.error('订单创建失败', error);
      });
    },
    resetSelection() {
      this.selectedRoom = {};
      this.checkOutDate = '';
      this.showRoomSelector = false;
      this.showConfirmDialog = false;
    },
    clear(){
      this.bookedRoom = [];
    }
  },
  mounted() {
    this.fetchAvailableRooms();
  }
};
</script>
