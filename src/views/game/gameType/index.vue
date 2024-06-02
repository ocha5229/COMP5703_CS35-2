<template>
	<div class="app-container">
		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">New</el-button>
			</el-col>
		</el-row>

		<el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
			<!-- <el-table-column type="selection" width="55" align="center" /> -->
			<el-table-column label="ID" width="100" align="center" prop="id" />
			<el-table-column label="Game Type" align="center" prop="questionTypeNm" />
			<el-table-column label="Process" width="200" align="center">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit"
						@click="changeType(scope.row)">Change</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete"
						@click="handleDelete(scope.row)">Delete</el-button>
				</template>
			</el-table-column>

		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
			:limit.sync="queryParams.pageSize" @pagination="getList" />

		<el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">

				<el-form-item label="Game Type" prop="questionTypeNm">
					<el-input v-model="form.questionTypeNm" placeholder="Please enter the game type" />
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">SAVE</el-button>
				<el-button @click="cancel">CANCEL</el-button>
			</div>
		</el-dialog>

	</div>
</template>

<script>

import { gameTypeList, addType, getType, updateType, deleteType } from '@/api/game/gameType';
import { getToken } from '@/utils/auth'
import store from "@/store";
export default {
	name: "type",
	components: {},
	data() {
		return {
			loading: true,
			ids: [],
			single: true,
			multiple: true,
			total: 0,
			tableData: [],
			title: "",
			open: false,
			queryParams: {
				pageNum: 1,
				pageSize: 10,
			},
			form: {},
			rules: {
				questionTypeNm: [
					{ required: true, message: "Please enter the game type", trigger: "blur" }
				],
			},
			//
			actionUrl: '',
			//
			headers: '',
			//
			imgUrl: '',
		};
	},
	created() {
		this.getList();
	},
	methods: {
		getList() {
			this.headers = {
				'Authorization': 'Bearer ' + getToken()
			}
			this.loading = true;
			gameTypeList(this.queryParams).then(res => {
				if (res.code == 200) {
					this.tableData = res.rows;
					this.total = res.total;
					this.loading = false;
				}
			})
		},
		changeType(row) {
			this.reset();
			getType(row.id).then(res => {
				this.form = res.data;
				this.title = "Modify game type";
				this.open = true;
			})
			this.title = "Modify game type";
		},
		//
		cancel() {
			this.open = false;
			this.reset();
		},
		//
		reset() {
			this.form = {
				id: undefined,
				gameType: undefined
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
			this.resetForm("queryForm");
			this.handleQuery();
		},
		//
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.id)
			this.selection = selection;
			this.single = selection.length != 1
			this.multiple = !selection.length
		},
		/**  */
		handleAdd() {
			this.reset();
			this.open = true;
			this.title = "New game types added";
		},
		handleDelete(row) {
			this.$confirm('Do you want to delete this type?', "Warning", {
				confirmButtonText: "YES",
				cancelButtonText: "NO",
				type: "warning"
			}).then(() => {
				return deleteType(row.id);
			}).then(() => {
				this.getList();
				this.msgSuccess("Delete successful");
			}).catch(() => {
				this.msgSuccess("Cancel operation");
			})
		},
		/**  */
		submitForm: function () {
			this.$refs["form"].validate(valid => {
				if (valid) {
					if (this.form.id != undefined) {
						updateType(this.form).then(response => {
							this.msgSuccess("Modified successfully");
							this.open = false;
							this.getList();
						});
					} else {
						addType(this.form).then(response => {
							this.msgSuccess("New successfully added");
							this.open = false;
							this.getList();
						});
					}
				}
			});
		},

	}
};
</script>
<style scoped lang="scss">
.el-col {
	margin-bottom: 10px;
}
</style>


