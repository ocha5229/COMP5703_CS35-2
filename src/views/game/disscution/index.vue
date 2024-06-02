<template>
	<div class="app-container">
		<el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
			<!-- <el-table-column type="selection" width="55" align="center" /> -->
			<el-table-column label="ID" width="100" align="center" prop="id" />
			<el-table-column label="Game Type" align="center" prop="questionTypeNm" />
			<el-table-column label="Game Title" align="center" prop="gameTitle" />
			<el-table-column label="Game Description" align="center" prop="remark" />
			<el-table-column label="Comments" align="center" width="200">
				<template slot-scope="scope">
					<el-button v-show="scope.row.type != 4" size="mini" type="text" icon="el-icon-view"
						@click="handleDetail(scope.row)">
						View Comments</el-button>
				</template>
			</el-table-column>
			<el-table-column label="Process" align="center" width="200">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-document" @click="handleDisscution(scope.row)">
						Discussion</el-button>
				</template>
			</el-table-column>

		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<el-dialog title="Fill in comments" :visible.sync="open" width="500px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<!-- <el-form-item :label="item.userName" v-for="item in form.historyData" :key="item.id">
					<el-input type="textarea" autosize v-model="item.content">
					</el-input>
				</el-form-item> -->

				<el-form-item label="content" prop="content">
					<el-input type="textarea" autosize placeholder="Please enter the evaluate" v-model="form.content">
					</el-input>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">SAVE</el-button>
				<el-button @click="cancel">CANCEL</el-button>
			</div>
		</el-dialog>

		<el-dialog title="View comments" :visible.sync="viewOpen" width="500px" append-to-body>
			<div v-if="historyData.length == 0">There are currently no comments available</div>
			<div v-else>
				<div v-for="item in historyData">{{ item.userName }}：{{ item.content }}</div>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="viewCancel">CANCEL</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { gameList } from '@/api/game/subject';
import { disscutionList, addDisscution } from '@/api/game/disscution';
import { getToken } from '@/utils/auth'
import store from "@/store";
export default {
	name: "type",
	components: {},
	data() {
		return {
			loading: true,
			open: false,
			viewOpen: false,
			form: {
				gameId: undefined,
				content: undefined,
			},
			historyData: [],
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
				content: [
					{ required: true, message: "Evaluate cannot be empty", trigger: "blur" }
				],
			},
			role: '',
		};
	},
	created() {
		this.getList();
		this.role = this.$store.getters.roles[0];
	},
	methods: {
		/**  */
		getList() {
			this.headers = {
				'Authorization': 'Bearer ' + getToken()
			}
			this.loading = true;
			gameList(this.queryParams).then(res => {
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
					addDisscution(this.form).then(response => {
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
		viewCancel() {
			this.viewOpen = false;
			this.historyData = [];
		},
		//
		reset() {
			this.form = {
				gameId: undefined,
				content: undefined,
			};
			this.resetForm("form");
		},
		handleDetail(row) {
			this.historyData = [];
			disscutionList({ gameId: row.id }).then(res => {
				if (res.code == 200) {
					this.historyData = res.data;
					this.viewOpen = true;
				}
			})
		},
		handleDisscution(row) {
			this.form = {
				gameId: row.id,
				content: undefined
			}
			this.open = true;
			// disscutionList({ gameId: row.id }).then(res => {
			// 	if (res.code == 200) {
			// 		this.form.historyData = res.data;
			// 		this.open = true;
			// 	}
			// })
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
