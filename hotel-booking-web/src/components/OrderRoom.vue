<template>
    <div class="order_room">
        <header>
            <el-select v-model="selectedRoomType" placeholder="选择类型" @change="handelFilterRooms">
                <el-option v-for="(item, index) in roomTypes" :key="index" :label="item" :value="item" />
            </el-select>
        </header>

        <el-table :data="filterRooms" class="table" max-height="500" :header-cell-style="{ 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }" @row-click="rowClick">
            <el-table-column prop="roomNumber" label="房间号" />
            <el-table-column prop="type" label="类型">
                <template slot-scope="scope">
                    <el-tag type="primary" disable-transitions>{{ scope.row.type }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="price" label="价格" />
        </el-table>

        <el-dialog :visible="dialogVisible" width="70%">
            <!-- 表单 -->
            <el-form :model="orderData" label-width="80px">

                <el-form-item label="房间号">
                    <p style="float:left">{{ orderData.room.roomNumber }}</p>
                </el-form-item>

                <el-form-item label="入住时间">
                    <el-col :span="11">
                        <el-date-picker value-format="yyyy-MM-dd" v-model="orderData.checkInDate" type="date" placeholder="选择开始时间" :picker-options="pickerOptionsBegin" />
                    </el-col>
                    <el-col class="line" :span="2">--</el-col>
                    <el-col :span="11">
                        <el-date-picker value-format="yyyy-MM-dd" v-model="orderData.checkOutDate" type="date" placeholder="选择结束时间" :picker-options="pickerOptionsEnd" />
                    </el-col>
                </el-form-item>

                <el-form-item label="结算" >
                    <p style="float:left">您订购了 {{ daysBooked }} 天，需要 {{ totalPrice }} 元。</p>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="pay">确认支付</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
 import { getAvailableRooms, createOrder } from '@/api/service';
export default {
    name: 'OrderRoom',
    data() {
        return {
            dialogVisible:false,
            availableRooms: [],
            filterRooms:[],
            orderData: {
                user: { id: 1 } ,
                room: {} ,
                checkInDate: new Date() ,
                checkOutDate: new Date() ,
                status: 'PENDING' ,
                totalPrice: 0
            }, // 订单数据
            selectedRoomType: '',
            roomTypes: ['ALL'], // 根据你的数据调整
            pickerOptionsBegin: {
                disabledDate(time) {
                    return time.getTime() < Date.now();
                },
            },
            pickerOptionsEnd: {
                disabledDate: (time) => {
                    return time.getTime() < this.orderData.checkInDate ;
                },
            }
        }
    },
    computed: {
      daysBooked() {
        if (!this.orderData.checkOutDate) return 0;
        const checkInDate = new Date(this.orderData.checkInDate);
        const checkOutDate = new Date(this.orderData.checkOutDate);
        const diffTime = Math.abs(checkOutDate - checkInDate);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
      },
      totalPrice() {
        return this.daysBooked * this.orderData.room.price ;
      }
    },
    methods: {
        rowClick(row){
          // 从localStorage获取用户信息
          const userInfo = JSON.parse(localStorage.getItem('userInfo'));
          if (!userInfo) {
            this.$message.error('请先登录');
            this.$router.push('/login');
            return;
          }
            this.orderData = {
                user: { id:userInfo.id} ,
                room: row ,
                checkInDate: new Date(),
                checkOutDate: new Date() ,
                status: 'PENDING' ,
                totalPrice: row.price
            }
            this.dialogVisible = true ;
        },
        fetchAvailableRooms() {
            getAvailableRooms().then(response => {
                this.filterRooms = this.availableRooms = response.data;
                this.showRoomSelector = true;
                 // 获取房间类型
                const tem = this.availableRooms.reduce((types, cur) => {
                    if (!types.find(item => item == cur.type)) {
                        types.push(cur.type);
                    }
                    return types;
                }, []);
                this.roomTypes = [...this.roomTypes, ...tem];
                this.selectedRoomType = this.roomTypes[0];
            });
        },

        handelFilterRooms: function () {
            this.selectedRoomType === 'ALL'
                ? this.filterRooms = this.availableRooms
                : this.filterRooms = this.availableRooms.filter(item => item.type == this.selectedRoomType)

        },
        /* 支付 */
        pay() {
            createOrder(this.orderData).then(response => {
                const orderId = response.data.id; // 获取后端返回的订单ID
                // 跳转到支付页面，并将订单ID作为参数传递
                this.$router.push({ name: 'Payment', params: { orderId: orderId } });
                this.$message({
                    message: '订单创建成功',
                    type:'success'
                });
                }).catch((err) => {
              console.log('@@@',err)
                    this.$message({
                        message: '订单创建失败',
                        type: 'error'
                    });
                });
        },
    },
    mounted() {
        this.fetchAvailableRooms(); // 初始化数据
    },
}

</script>

<style scoped>
.order_room {
    width: 100vw;
    height: 100vh;
    background-color: white;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-evenly;
    box-shadow: 0 0 5px 10px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
}

header {
    width: 90%;
    height: 10%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 3vw;
}

.table {
    width: 90%;
    max-height: 90%;
}

.item {
    width: 50px;
    height: 50px;
    position: fixed;
    bottom: 5vh;
    right: 5vw;
}

</style>
