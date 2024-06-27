<template>
  <div>
    <div class="header">
      用户信息
    </div>
    <el-table :data="users" style="width: 100%">
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="role" label="角色">
        <template slot-scope="scope">
          <el-tag :type="userStatus(scope.row.role).type">{{ userStatus(scope.row.role).label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini"  type="primary" @click="showEditDialog(scope.row)">修改</el-button>
          <el-button
            size="mini"
            :type="userStatus(scope.row.role).buttonType"
            @click="confirmUserStatusChange(scope.row)"
          >{{ userStatus(scope.row.role).buttonText }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改用户信息的对话框 -->
    <el-dialog :visible.sync="showEditUserDialog" title="修改用户信息">
      <el-form :model="editUserForm">
        <el-form-item label="用户名">
          <el-input v-model="editUserForm.username" disabled placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="editUserForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editUserForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editUserForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editUserForm.role" placeholder="请选择角色">
            <el-option label="操作员" value="USER"></el-option>
            <el-option label="管理员" value="ADMIN"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showEditUserDialog = false">取消修改</el-button>
        <el-button type="primary" @click="editUser">确认修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getUsers, updateUser, disableUser, enableUser } from '@/api/service';

export default {
  data() {
    return {
      users: [],
      showEditUserDialog: false,
      editUserForm: {
        id: '',
        username: '',
        password: '',
        email: '',
        phone: '',
        role: ''
      }
    };
  },
  methods: {
    fetchUsers() {
      getUsers()
        .then(response => {
          this.users = response.data;
        })
        .catch(error => {
          console.error("获取用户列表失败:", error);
        });
    },
    showEditDialog(row) {
      this.editUserForm = Object.assign({}, row);
      this.showEditUserDialog = true;
    },
    editUser() {
      updateUser(this.editUserForm)
        .then(() => {
          this.showEditUserDialog = false;
          this.$message.success('用户信息修改成功');
          this.fetchUsers();
        })
        .catch(error => {
          console.error("修改用户信息失败:", error);
        });
    },
    confirmUserStatusChange(row) {
      const targetStatus = row.role === 'OTHER' ? '启用' : '禁用';
      this.$confirm(`是否确认${targetStatus}用户名为 ${row.username} 的用户?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          if (row.role === 'OTHER') {
            this.enableUser(row.id);
          } else {
            this.disableUser(row.id);
          }
        })
        .catch(() => {
          // 取消操作
        });
    },
    disableUser(userId) {
      disableUser(userId)
        .then(() => {
          this.$message.success('用户已禁用');
          this.fetchUsers();
        })
        .catch(error => {
          console.error("禁用用户失败:", error);
        });
    },
    enableUser(userId) {
      enableUser(userId)
        .then(() => {
          this.$message.success('用户已启用');
          this.fetchUsers();
        })
        .catch(error => {
          console.error("启用用户失败:", error);
        });
    },
    userStatus(role) {
      if (role === 'OTHER') {
        return {
          label: '已禁用',
          type: 'danger',
          buttonText: '启用',
          buttonType: 'success'
        };
      } else {
        return {
          label: role === 'ADMIN' ? '管理员' : '操作员',
          type: role === 'ADMIN' ? 'primary' : '',
          buttonText: '禁用',
          buttonType: 'danger'
        };
      }
    }
  },
  created() {
    this.fetchUsers();
  }
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
