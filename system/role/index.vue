<template>
	<div class="app-container">

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
					v-hasPermi="['system:role:add']">new</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
					v-hasPermi="['system:role:edit']">edit</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
					@click="handleDelete" v-hasPermi="['system:role:remove']">delete</el-button>
			</el-col>

		</el-row>

		<el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column label="Role Id" prop="roleId" width="120" />
			<el-table-column label="Role Name" prop="roleName" :show-overflow-tooltip="true" width="350" />
			<el-table-column label="Permission character" prop="roleKey" :show-overflow-tooltip="true" width="300" />
			<el-table-column label="Sort" prop="roleSort" width="100" />
			<el-table-column label="status" align="center" width="100">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
						@change="handleStatusChange(scope.row)"></el-switch>
				</template>
			</el-table-column>
			
			<el-table-column label="operate" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope" v-if="scope.row.roleId !== 1">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
						v-hasPermi="['system:role:edit']">edit</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
						v-hasPermi="['system:role:remove']">delete</el-button>
					<el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
						v-hasPermi="['system:role:edit']">
						<el-button size="mini" type="text" icon="el-icon-d-arrow-right">more</el-button>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item command="handleDataScope" icon="el-icon-circle-check"
								v-hasPermi="['system:role:edit']">Data permissions</el-dropdown-item>
							<el-dropdown-item command="handleAuthUser" icon="el-icon-user"
								v-hasPermi="['system:role:edit']">Assigning Users</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
			:limit.sync="queryParams.pageSize" @pagination="getList" />


		<el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="200px">
				<el-form-item label="Role Name" prop="roleName">
					<el-input v-model="form.roleName" placeholder="Please enter the role name" />
				</el-form-item>
				<el-form-item prop="roleKey">
					<span slot="label">
						<el-tooltip
							content="Permission characters defined in the controller, such as @PreAuthorize (' @ss.hasRole ('admin ')) "
							placement="top">
							<i class="el-icon-question"></i>
						</el-tooltip>
						Permission character
					</span>
					<el-input v-model="form.roleKey" placeholder="Please enter permission character" />
				</el-form-item>
				<el-form-item label="Role order" prop="roleSort">
					<el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
				</el-form-item>
				<el-form-item label="status">
					<el-radio-group v-model="form.status">
						<el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{
			dict.label }}</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="Menu permissions">
					<el-checkbox v-model="menuExpand"
						@change="handleCheckedTreeExpand($event, 'menu')">Unfold/fold</el-checkbox>
					<el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">Select
						all/Select
						none</el-checkbox>
					<el-checkbox v-model="form.menuCheckStrictly"
						@change="handleCheckedTreeConnect($event, 'menu')">Father-son
						interaction</el-checkbox>
					<el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" node-key="id"
						:check-strictly="!form.menuCheckStrictly" empty-text="Loading, please wait"
						:props="defaultProps"></el-tree>
				</el-form-item>
				<el-form-item label="remark">
					<el-input v-model="form.remark" type="textarea" placeholder="Please enter content"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">OK</el-button>
				<el-button @click="cancel">Cancel</el-button>
			</div>
		</el-dialog>

		<el-dialog :title="title" :visible.sync="openDataScope" width="500px" append-to-body>
			<el-form :model="form" label-width="80px">
				<el-form-item label="Role name">
					<el-input v-model="form.roleName" :disabled="true" />
				</el-form-item>
				<el-form-item label="Permission character">
					<el-input v-model="form.roleKey" :disabled="true" />
				</el-form-item>
				<el-form-item label="Scope of authority">
					<el-select v-model="form.dataScope" @change="dataScopeSelectChange">
						<el-option v-for="item in dataScopeOptions" :key="item.value" :label="item.label"
							:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="Data permission" v-show="form.dataScope == 2">
					<el-checkbox v-model="deptExpand"
						@change="handleCheckedTreeExpand($event, 'dept')">Unfold/fold</el-checkbox>
					<el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">Select
						all/Select
						none</el-checkbox>
					<el-checkbox v-model="form.deptCheckStrictly"
						@change="handleCheckedTreeConnect($event, 'dept')">Father-son
						interaction</el-checkbox>
					<el-tree class="tree-border" :data="deptOptions" show-checkbox default-expand-all ref="dept"
						node-key="id" :check-strictly="!form.deptCheckStrictly" empty-text="Loading, please wait"
						:props="defaultProps"></el-tree>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitDataScope">OK</el-button>
				<el-button @click="cancelDataScope">Cancel</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole, dataScope, changeRoleStatus, deptTreeSelect } from "@/api/system/role";
import { treeselect as menuTreeselect, roleMenuTreeselect } from "@/api/system/menu";

export default {
	name: "Role",
	dicts: ['sys_normal_disable'],
	data() {
		return {
			loading: true,
			ids: [],
			single: true,
			multiple: true,
			showSearch: true,
			total: 0,
			roleList: [],
			title: "",
			open: false,
			openDataScope: false,
			menuExpand: false,
			menuNodeAll: false,
			deptExpand: true,
			deptNodeAll: false,
			dateRange: [],
			dataScopeOptions: [
				{
					value: "1",
					label: "Full data rights"
				},
				{
					value: "2",
					label: "Customize data permissions"
				},
				
				{
					value: "5",
					label: "Personal data rights only"
				}
			],
			menuOptions: [],
			deptOptions: [],
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				roleName: undefined,
				roleKey: undefined,
				status: undefined
			},

			form: {},
			defaultProps: {
				children: "children",
				label: "label"
			},

			rules: {
				roleName: [
					{ required: true, message: "The role name cannot be empty", trigger: "blur" }
				],
				roleKey: [
					{ required: true, message: "The permission character cannot be empty", trigger: "blur" }
				],
				roleSort: [
					{ required: true, message: "The role sequence cannot be empty", trigger: "blur" }
				]
			}
		};
	},
	created() {
		this.getList();
	},
	methods: {

		getList() {
			this.loading = true;
			listRole(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
				this.roleList = response.rows;
				this.total = response.total;
				this.loading = false;
			}
			);
		},
		getMenuTreeselect() {
			menuTreeselect().then(response => {
				this.menuOptions = response.data;
			});
		},

		getMenuAllCheckedKeys() {
			let checkedKeys = this.$refs.menu.getCheckedKeys();
			let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
			checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
			return checkedKeys;
		},

		getDeptAllCheckedKeys() {
			let checkedKeys = this.$refs.dept.getCheckedKeys();
			let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys();
			checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
			return checkedKeys;
		},

		getRoleMenuTreeselect(roleId) {
			return roleMenuTreeselect(roleId).then(response => {
				this.menuOptions = response.menus;
				return response;
			});
		},

		getDeptTree(roleId) {
			return deptTreeSelect(roleId).then(response => {
				this.deptOptions = response.depts;
				return response;
			});
		},

		handleStatusChange(row) {
			let text = row.status === "0" ? "enable " : "disable";
			this.$modal.confirm('Are you sure you want to ' + text + ' the ' + row.roleName + ' role？').then(function () {
				return changeRoleStatus(row.roleId, row.status);
			}).then(() => {
				this.$modal.msgSuccess(text + "Successful");
			}).catch(function () {
				row.status = row.status === "0" ? "1" : "0";
			});
		},

		cancel() {
			this.open = false;
			this.reset();
		},

		cancelDataScope() {
			this.openDataScope = false;
			this.reset();
		},

		reset() {
			if (this.$refs.menu != undefined) {
				this.$refs.menu.setCheckedKeys([]);
			}
			this.menuExpand = false,
				this.menuNodeAll = false,
				this.deptExpand = true,
				this.deptNodeAll = false,
				this.form = {
					roleId: undefined,
					roleName: undefined,
					roleKey: undefined,
					roleSort: 0,
					status: "0",
					menuIds: [],
					deptIds: [],
					menuCheckStrictly: true,
					deptCheckStrictly: true,
					remark: undefined
				};
			this.resetForm("form");
		},

		handleQuery() {
			this.queryParams.pageNum = 1;
			this.getList();
		},

		resetQuery() {
			this.dateRange = [];
			this.resetForm("queryForm");
			this.handleQuery();
		},

		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.roleId)
			this.single = selection.length != 1
			this.multiple = !selection.length
		},

		handleCommand(command, row) {
			switch (command) {
				case "handleDataScope":
					this.handleDataScope(row);
					break;
				case "handleAuthUser":
					this.handleAuthUser(row);
					break;
				default:
					break;
			}
		},

		handleCheckedTreeExpand(value, type) {
			if (type == 'menu') {
				let treeList = this.menuOptions;
				for (let i = 0; i < treeList.length; i++) {
					this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
				}
			} else if (type == 'dept') {
				let treeList = this.deptOptions;
				for (let i = 0; i < treeList.length; i++) {
					this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
				}
			}
		},

		handleCheckedTreeNodeAll(value, type) {
			if (type == 'menu') {
				this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
			} else if (type == 'dept') {
				this.$refs.dept.setCheckedNodes(value ? this.deptOptions : []);
			}
		},

		handleCheckedTreeConnect(value, type) {
			if (type == 'menu') {
				this.form.menuCheckStrictly = value ? true : false;
			} else if (type == 'dept') {
				this.form.deptCheckStrictly = value ? true : false;
			}
		},

		handleAdd() {
			this.reset();
			this.getMenuTreeselect();
			this.open = true;
			this.title = "Add a role";
		},

		handleUpdate(row) {
			this.reset();
			const roleId = row.roleId || this.ids
			const roleMenu = this.getRoleMenuTreeselect(roleId);
			getRole(roleId).then(response => {
				this.form = response.data;
				this.open = true;
				this.$nextTick(() => {
					roleMenu.then(res => {
						let checkedKeys = res.checkedKeys
						checkedKeys.forEach((v) => {
							this.$nextTick(() => {
								this.$refs.menu.setChecked(v, true, false);
							})
						})
					});
				});
				this.title = "Modify role";
			});
		},

		dataScopeSelectChange(value) {
			if (value !== '2') {
				this.$refs.dept.setCheckedKeys([]);
			}
		},

		handleDataScope(row) {
			this.reset();
			const deptTreeSelect = this.getDeptTree(row.roleId);
			getRole(row.roleId).then(response => {
				this.form = response.data;
				this.openDataScope = true;
				this.$nextTick(() => {
					deptTreeSelect.then(res => {
						this.$refs.dept.setCheckedKeys(res.checkedKeys);
					});
				});
				this.title = "Assign data rights";
			});
		},

		handleAuthUser: function (row) {
			const roleId = row.roleId;
			this.$router.push("/system/role-auth/user/" + roleId);
		},

		submitForm: function () {
			this.$refs["form"].validate(valid => {
				if (valid) {
					if (this.form.roleId != undefined) {
						this.form.menuIds = this.getMenuAllCheckedKeys();
						updateRole(this.form).then(response => {
							this.$modal.msgSuccess("Modified successfully");
							this.open = false;
							this.getList();
						});
					} else {
						this.form.menuIds = this.getMenuAllCheckedKeys();
						addRole(this.form).then(response => {
							this.$modal.msgSuccess("New success");
							this.open = false;
							this.getList();
						});
					}
				}
			});
		},

		submitDataScope: function () {
			if (this.form.roleId != undefined) {
				this.form.deptIds = this.getDeptAllCheckedKeys();
				dataScope(this.form).then(response => {
					this.$modal.msgSuccess("Modified successfully");
					this.openDataScope = false;
					this.getList();
				});
			}
		},

		handleDelete(row) {
			const roleIds = row.roleId || this.ids;
			this.$modal.confirm('Do you want to delete the data item with role number "' + roleIds + '"? ').then(function () {
				return delRole(roleIds);
			}).then(() => {
				this.getList();
				this.$modal.msgSuccess("Successfully deleted");
			}).catch(() => { });
		},

		handleExport() {
			this.download('system/role/export', {
				...this.queryParams
			}, `role_${new Date().getTime()}.xlsx`)
		}
	}
};
</script>
