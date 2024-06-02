<template>
	<div class="app-container">

		<el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
			<!-- <el-table-column type="selection" width="55" align="center" /> -->
			<el-table-column label="ID" width="100" align="center" prop="historyId" />
			<el-table-column label="Game Title" align="center" prop="gameTitle" />
			<el-table-column label="Nick Name" align="center" prop="nickName" />
			<el-table-column label="Start time" align="center" prop="createTime" />
			<el-table-column label="Answer time" align="center" prop="answerTime" />
			<el-table-column label="Process" align="center" width="200">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-plus" @click="handleDetail(scope.row)">
						Detail</el-button>
					<el-button size="mini" type="text" icon="el-icon-document" @click="handleDisscution(scope.row)">
						Discussion</el-button>
				</template>
			</el-table-column>

		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<el-dialog title="Comment" :visible.sync="open" width="500px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<el-form-item label="Evaluate" prop="evaluate">
					<el-input type="textarea" autosize placeholder="Please enter the evaluate" v-model="form.evaluate"
						:readonly="form.readonly">
					</el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm" v-if="!form.readonly">SAVE</el-button>
				<el-button @click="cancel">CANCEL</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { historyList, getEvaluate, updateEvaluate } from '@/api/game/history';
import { getToken } from '@/utils/auth'
import store from "@/store";
export default {
	name: "type",
	components: {},
	data() {
		return {
			loading: true,
			open: false,
			form: {},
			ids: [],
			single: true,
			multiple: true,
			total: 0,
			tableData: [],
			title: "",
			queryParams: {
				pageNum: 1,
				pageSize: 10,
			},
			//
			actionUrl: '',
			//
			headers: '',
			//
			imgUrl: '',
			//
			imageUrl: '',
			rules: {
				evaluate: [
					{ required: true, message: "Evaluate cannot be empty", trigger: "blur" }
				],
			},
			role: '',
		};
	},
	created() {
		this.getList();
		this.role = this.$store.getters.roles[0];
		console.log(this.role, 'this.role');
	},
	methods: {
		/**  */
		getList() {
			this.headers = {
				'Authorization': 'Bearer ' + getToken()
			}
			this.loading = true;
			historyList(this.queryParams).then(res => {
				if (res.code == 200) {
					this.tableData = res.rows;
					this.total = res.total;
					this.loading = false;
				}
			})
		},
		submitForm() {
			this.$refs["form"].validate(valid => {
				if (valid) {
					updateEvaluate(this.form).then(response => {
						if (response.code == 200) {
							this.msgSuccess("Comment successfully");
							this.open = false;
							this.getList();
						}

					});
				}
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
				id: undefined,
				evaluate: undefined
			};
			this.resetForm("form");
		},
		handleDetail(row) {
			this.$router.push({ path: "/game/historyDetail", query: { historyId: row.historyId, id: row.id } });
		},
		handleDisscution(row) {

			getEvaluate(row.historyId).then(res => {
				if (res.code == 200) {
					this.form = res.data;
					if (this.role == 'user') {
						this.form.readonly = true;
					} else {
						this.form.readonly = false;
					}
					this.open = true;
				}
			})


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
	}
};
</script>
<style scoped lang="scss">
.el-col {
	margin-bottom: 10px;
}
</style>
