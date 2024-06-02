<template>
	<div class="app-container">
		<el-row :gutter="20">
			<!---->

			<!---->
			<el-col :span="24" :xs="24">

				<el-row :gutter="10" class="mb8">
					<el-col :span="1.5">
						<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
							v-hasPermi="['system:user:add']">new</el-button>
					</el-col>
					<el-col :span="1.5">
						<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
							@click="handleUpdate" v-hasPermi="['system:user:edit']">edit</el-button>
					</el-col>
					<el-col :span="1.5">
						<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
							@click="handleDelete" v-hasPermi="['system:user:remove']">delete</el-button>
					</el-col>

				</el-row>

				<el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="50" align="center" />
					<el-table-column label="User ID" align="center" key="userId" prop="userId"
						v-if="columns[0].visible" />
					<el-table-column label="User Name" align="center" key="userName" prop="userName"
						v-if="columns[1].visible" :show-overflow-tooltip="true" />
					<el-table-column label="User nickname" align="center" key="nickName" prop="nickName"
						v-if="columns[2].visible" :show-overflow-tooltip="true" />
					<el-table-column label="phone number" align="center" key="phonenumber" prop="phonenumber"
						v-if="columns[4].visible" width="120" />
					<el-table-column label="status" align="center" key="status" v-if="columns[5].visible">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
								@change="handleStatusChange(scope.row)"></el-switch>
						</template>
					</el-table-column>
					<!-- <el-table-column label="Creation time" align="center" prop="createTime" v-if="columns[6].visible"
						width="160">
						<template slot-scope="scope">
							<span>{{ parseTime(scope.row.createTime) }}</span>
						</template>
					</el-table-column> -->
					<el-table-column label="operate" align="center" width="160" class-name="small-padding fixed-width">
						<template slot-scope="scope" v-if="scope.row.userId !== 1">
							<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
								v-hasPermi="['system:user:edit']">edit</el-button>
							<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
								v-hasPermi="['system:user:remove']">delete</el-button>
							<el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
								v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
								<el-button size="mini" type="text" icon="el-icon-d-arrow-right">more</el-button>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item command="handleResetPwd" icon="el-icon-key"
										v-hasPermi="['system:user:resetPwd']">reset password</el-dropdown-item>
									<el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check"
										v-hasPermi="['system:user:edit']">Assigning roles</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</template>
					</el-table-column>
				</el-table>

				<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
					:limit.sync="queryParams.pageSize" @pagination="getList" />
			</el-col>
		</el-row>

		<!--  -->
		<el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="130px">
				<el-row>
					<el-col :span="12">
						<el-form-item label="User nickname" prop="nickName">
							<el-input v-model="form.nickName" placeholder="Please enter the user nickname"
								maxlength="30" style="width: auto" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="phone number" prop="phonenumber">
							<el-input v-model="form.phonenumber" placeholder="Please enter phone number" maxlength="11"
								style="width: auto" />
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item v-if="form.userId == undefined" label="User Name" prop="userName">
							<el-input v-model="form.userName" placeholder="Please enter user name" maxlength="30"
								style="width: auto" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item v-if="form.userId == undefined" label="password" prop="password">
							<el-input v-model="form.password" placeholder="Please enter password" type="password"
								maxlength="20" show-password style="width: auto" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="gender">
							<el-select v-model="form.sex" placeholder="Please select gender">
								<el-option v-for="dict in dict.type.sys_user_sex" :key="dict.value" :label="dict.label"
									:value="dict.value"></el-option>
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="email" prop="email">
							<el-input v-model="form.email" placeholder="Please enter email" maxlength="50"
								style="width: auto" />
						</el-form-item>
					</el-col>


				</el-row>
				<el-row>

					<el-col :span="12">
						<el-form-item label="Role">
							<el-select v-model="form.roleIds" multiple placeholder="Please select a role">
								<el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName"
									:value="item.roleId" :disabled="item.status == 1"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="status">
							<el-radio-group v-model="form.status">
								<el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value"
									:label="dict.value">{{
			dict.label }}</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="remark">
							<el-input v-model="form.remark" type="textarea"
								placeholder="Please enter content"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">OK</el-button>
				<el-button @click="cancel">Cancel</el-button>
			</div>
		</el-dialog>

		<!--  -->
		<el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
			<el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
				:action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
				:on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
				<i class="el-icon-upload"></i>
				<div class="el-upload__text">Drag the file here, or <em> click Upload</em></div>
				<div class="el-upload__tip text-center" slot="tip">
					<div class="el-upload__tip" slot="tip">
						<el-checkbox v-model="upload.updateSupport" /> Whether to update existing user data
					</div>
					<span>Only xls and xlsx files can be imported.</span>
					<el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
						@click="importTemplate">Download template</el-link>
				</div>
			</el-upload>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitFileForm">OK</el-button>
				<el-button @click="upload.open = false">Cancel</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from "@/api/system/user";
import { getToken } from "@/utils/auth";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
	name: "User",
	dicts: ['sys_normal_disable', 'sys_user_sex'],
	components: { Treeselect },
	data() {
		return {
			//
			loading: true,
			//
			ids: [],
			//
			single: true,
			//
			multiple: true,
			//
			showSearch: true,
			//
			total: 0,
			//
			userList: null,
			//
			title: "",
			//
			deptOptions: undefined,
			//
			open: false,
			//
			deptName: undefined,
			//
			initPassword: '123456',
			//
			dateRange: [],
			//
			postOptions: [],
			//
			roleOptions: [],
			//
			form: {},
			defaultProps: {
				children: "children",
				label: "label"
			},
			//
			upload: {
				// （）
				open: false,
				// （）
				title: "",
				//
				isUploading: false,
				//
				updateSupport: 0,
				//
				headers: { Authorization: "Bearer " + getToken() },
				//
				url: process.env.VUE_APP_BASE_API + "/system/user/importData"
			},
			//
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				userName: undefined,
				phonenumber: undefined,
				status: undefined,
				deptId: undefined
			},
			//
			columns: [
				{ key: 0, label: `User ID`, visible: true },
				{ key: 1, label: `User Name`, visible: true },
				{ key: 2, label: `User Nick`, visible: true },
				{ key: 3, label: `dept`, visible: true },
				{ key: 4, label: `Phone Number`, visible: true },
				{ key: 5, label: `Status`, visible: true },
				{ key: 6, label: `Creation Time`, visible: true }
			],
			//
			rules: {
				userName: [
					{ required: true, message: "The user name cannot be empty", trigger: "blur" },
					{ min: 2, max: 20, message: 'The length of the user name must be between 2 and 20', trigger: 'blur' }
				],
				nickName: [
					{ required: true, message: "The user nickname cannot be empty", trigger: "blur" }
				],
				password: [
					{ required: true, message: "The user password cannot be empty", trigger: "blur" },
					{ min: 5, max: 20, message: 'The user password must be between 5 and 20 in length', trigger: 'blur' },
					{ pattern: /^[^<>"'|\\]+$/, message: "Cannot contain illegal characters：< > \" ' \\\ |", trigger: "blur" }
				],
				email: [
					{
						type: "email",
						message: "Please enter the correct email address",
						trigger: ["blur", "change"]
					}
				],
				phonenumber: [
					{
						pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
						message: "Please enter the correct mobile number",
						trigger: "blur"
					}
				]
			}
		};
	},

	created() {
		this.getList();
	},
	methods: {
		/**  */
		getList() {
			this.loading = true;
			listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
				console.info(response);
				this.userList = response.rows;
				this.total = response.total;
				this.loading = false;
			}
			);
		},
		/**  */
		getDeptTree() {
			deptTreeSelect().then(response => {
				this.deptOptions = response.data;
			});
		},
		//
		filterNode(value, data) {
			if (!value) return true;
			return data.label.indexOf(value) !== -1;
		},
		//
		handleNodeClick(data) {
			this.queryParams.deptId = data.id;
			this.handleQuery();
		},
		//
		handleStatusChange(row) {
			let text = row.status === "0" ? "enable " : "disable";
			this.$modal.confirm('Are you sure you want to ' + text + ' the ' + row.userName + ' user？').then(function () {
				return changeUserStatus(row.userId, row.status);
			}).then(() => {
				this.$modal.msgSuccess(text + "Successful");
			}).catch(function () {
				row.status = row.status === "0" ? "1" : "0";
			});
		},
		//
		cancel() {
			this.open = false;
			this.reset();
		},
		//
		reset() {
			this.form = {
				userId: undefined,
				deptId: undefined,
				userName: undefined,
				nickName: undefined,
				password: undefined,
				phonenumber: undefined,
				email: undefined,
				sex: undefined,
				status: "0",
				remark: undefined,
				postIds: [],
				roleIds: []
			};
			this.resetForm("form");
		},
		/**  */
		handleQuery() {
			this.queryParams.pageNum = 1;
			this.getList();
		},
		/**  */
		resetQuery() {
			this.dateRange = [];
			this.resetForm("queryForm");
			this.queryParams.deptId = undefined;
			this.$refs.tree.setCurrentKey(null);
			this.handleQuery();
		},
		//
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.userId);
			this.single = selection.length != 1;
			this.multiple = !selection.length;
		},
		//
		handleCommand(command, row) {
			switch (command) {
				case "handleResetPwd":
					this.handleResetPwd(row);
					break;
				case "handleAuthRole":
					this.handleAuthRole(row);
					break;
				default:
					break;
			}
		},
		/**  */
		handleAdd() {
			this.reset();
			getUser().then(response => {
				this.postOptions = response.posts;
				this.roleOptions = response.roles;
				this.open = true;
				this.title = "add user";
				this.form.password = this.initPassword;
			});
		},
		/**  */
		handleUpdate(row) {
			this.reset();
			const userId = row.userId || this.ids;
			getUser(userId).then(response => {
				this.form = response.data;
				this.postOptions = response.posts;
				this.roleOptions = response.roles;
				this.$set(this.form, "postIds", response.postIds);
				this.$set(this.form, "roleIds", response.roleIds);
				this.open = true;
				this.title = "edit user";
				this.form.password = "";
			});
		},
		/**  */
		handleResetPwd(row) {
			this.$prompt('Please enter a new password for "' + row.userName + '"', "Tips", {
				confirmButtonText: "OK",
				cancelButtonText: "Cancel",
				closeOnClickModal: false,
				inputPattern: /^.{5,20}$/,
				inputErrorMessage: "The user password must be between 5 and 20 in length",
				inputValidator: (value) => {
					if (/<|>|"|'|\||\\/.test(value)) {
						return "Cannot contain illegal characters：< > \" ' \\\ |"
					}
				},
			}).then(({ value }) => {
				resetUserPwd(row.userId, value).then(response => {
					this.$modal.msgSuccess("Changed successfully, the new password is:" + value);
				});
			}).catch(() => { });
		},
		/**  */
		handleAuthRole: function (row) {
			const userId = row.userId;
			this.$router.push("/system/user-auth/role/" + userId);
		},
		/**  */
		submitForm: function () {
			this.$refs["form"].validate(valid => {
				if (valid) {
					if (this.form.userId != undefined) {
						updateUser(this.form).then(response => {
							this.$modal.msgSuccess("Modified successfully");
							this.open = false;
							this.getList();
						});
					} else {
						addUser(this.form).then(response => {
							this.$modal.msgSuccess("New success");
							this.open = false;
							this.getList();
						});
					}
				}
			});
		},
		/**  */
		handleDelete(row) {
			const userIds = row.userId || this.ids;
			this.$modal.confirm('Do you want to delete the data item with user number "' + userIds + '"? ').then(function () {
				return delUser(userIds);
			}).then(() => {
				this.getList();
				this.$modal.msgSuccess("Successfully deleted");
			}).catch(() => { });
		},
		/**  */
		handleExport() {
			this.download('system/user/export', {
				...this.queryParams
			}, `user_${new Date().getTime()}.xlsx`)
		},
		/**  */
		handleImport() {
			this.upload.title = "User import";
			this.upload.open = true;
		},
		/**  */
		importTemplate() {
			this.download('system/user/importTemplate', {
			}, `user_template_${new Date().getTime()}.xlsx`)
		},
		//
		handleFileUploadProgress(event, file, fileList) {
			this.upload.isUploading = true;
		},
		//
		handleFileSuccess(response, file, fileList) {
			this.upload.open = false;
			this.upload.isUploading = false;
			this.$refs.upload.clearFiles();
			this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "Import result", { dangerouslyUseHTMLString: true });
			this.getList();
		},
		//
		submitFileForm() {
			this.$refs.upload.submit();
		}
	}
};
</script>
