<template>
  <div class="app-container">
    <h4 class="form-header h4">Basic information</h4>
    <el-form ref="form" :model="form" label-width="120px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item label="User nickname" prop="nickName">
            <el-input v-model="form.nickName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item label="Login account" prop="userName">
            <el-input  v-model="form.userName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <h4 class="form-header h4">Role information</h4>
    <el-table v-loading="loading" :row-key="getRowKey" @row-click="clickRow" ref="table" @selection-change="handleSelectionChange" :data="roles.slice((pageNum-1)*pageSize,pageNum*pageSize)">
      <el-table-column label="Serial number" type="index" align="center"  width="120">
        <template slot-scope="scope">
          <span>{{(pageNum - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column type="selection" :reserve-selection="true" width="55"></el-table-column>
      <el-table-column label="Role ID" align="center" prop="roleId" />
      <el-table-column label="Role name" align="center" prop="roleName" />
      <el-table-column label="Permission character" align="center" prop="roleKey" />
      <el-table-column label="Creation time" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />

    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
        <el-button type="primary" @click="submitForm()">submit</el-button>
        <el-button @click="close()">Back</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getAuthRole, updateAuthRole } from "@/api/system/user";

export default {
  name: "AuthRole",
  data() {
    return {
       //
      loading: true,
      //
      total: 0,
      pageNum: 1,
      pageSize: 10,
      //
      roleIds:[],
      //
      roles: [],
      //
      form: {}
    };
  },
  created() {
    const userId = this.$route.params && this.$route.params.userId;
    if (userId) {
      this.loading = true;
      getAuthRole(userId).then((response) => {
        this.form = response.user;
        this.roles = response.roles;
        this.total = this.roles.length;
        this.$nextTick(() => {
          this.roles.forEach((row) => {
            if (row.flag) {
              this.$refs.table.toggleRowSelection(row);
            }
          });
        });
        this.loading = false;
      });
    }
  },
  methods: {
    /**  */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    //
    handleSelectionChange(selection) {
      this.roleIds = selection.map((item) => item.roleId);
    },
    //
    getRowKey(row) {
      return row.roleId;
    },
    /**  */
    submitForm() {
      const userId = this.form.userId;
      const roleIds = this.roleIds.join(",");
      updateAuthRole({ userId: userId, roleIds: roleIds }).then((response) => {
        this.$modal.msgSuccess("Authorization succeeds");
        this.close();
      });
    },
    /**  */
    close() {
      const obj = { path: "/system/user" };
      this.$tab.closeOpenPage(obj);
    },
  },
};
</script>
