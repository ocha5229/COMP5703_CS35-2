<template>
	<div class="app-container">

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">New Game</el-button>
			</el-col>
		</el-row>

		<el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
			<!-- <el-table-column type="selection" width="55" align="center" /> -->
			<el-table-column label="ID" width="100" align="center" prop="id" />
			<el-table-column label="Game Type" align="center" prop="questionTypeNm" />
			<el-table-column label="Game Title" align="center" prop="gameTitle" />
			<el-table-column label="Game Description" align="center" prop="remark" />
			<el-table-column label="Game Sort" align="center" prop="sort" />
			<el-table-column label="Process" align="center" width="200">
				<template slot-scope="scope">
					<el-button v-show="scope.row.type != 4" size="mini" type="text" icon="el-icon-plus"
						@click="handleDetail(scope.row)">
						Detail</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete"
						@click="handleDelete(scope.row)">Delete</el-button>
				</template>
			</el-table-column>

		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
			:limit.sync="queryParams.pageSize" @pagination="getList" />

		<el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<el-row type="flex" justify="space-around">
					<el-col :span="24">
						<el-form-item label="Game Type" prop="questionTypeId">
							<el-select style="width: 260px" v-model="form.questionTypeId"
								placeholder="Please select the game type">
								<el-option v-for="item in gameTypesData" :label="item.questionTypeNm"
									:value="item.id" />
							</el-select>
						</el-form-item>
					</el-col>

				</el-row>

				<el-row type="flex" justify="space-around">

					<el-col :span="24">
						<el-form-item label="Qustion Title" prop="questionTitle">
							<el-input type="textarea" :rows="2" v-model="form.questionTitle"
								placeholder="Please enter the Question title" />

						</el-form-item>
					</el-col>
				</el-row>


				<el-row type="flex" justify="space-around">
					<el-col :span="24">
						<el-form-item label="Question Type" prop="type">
							<el-radio-group v-model="form.type">
								<el-radio :label="1">Single Choice</el-radio>
								<el-radio :label="2">Multiple Choice</el-radio>
								<el-radio :label="3">Calculation Questions</el-radio>
								<el-radio :label="4">Q&A Questions</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">SAVE</el-button>
				<el-button @click="cancel">CANCEL</el-button>
			</div>
		</el-dialog>

		<!-- options dialog -->
		<el-dialog :title="title" :visible.sync="optionsOpen" width="700px" append-to-body>
			<el-form ref="optionsForm" :model="optionsForm" :rules="optionsRules" label-width="120px">
				<el-form-item label="Qustion Title" prop="questionTitle">
					<el-input v-model="optionsForm.questionTitle" placeholder="Please enter the Question title" />
				</el-form-item>
				<el-form-item label="Question Type" prop="typeValue">
					<el-input readonly v-model="optionsForm.typeValue" />
				</el-form-item>
				<template v-if="optionsForm.type == 1 || optionsForm.type == 2">
					<el-row type="flex" justify="space-between" v-for="(item, index) in optionsForm.itemArr">
						<el-col :span="18" style="display: flex; align-items: center;">
							<el-form-item label="Options">
								<el-input v-model="optionsForm.itemArr[index1].item">
									<el-button v-show="optionsForm.itemArr.length - 1 == index1" circle
										icon="el-icon-plus" slot="suffix" size="mini"
										@click="addOption(index1)"></el-button>
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :span="4" style="display: flex; align-items: center;">
							<el-checkbox v-model="optionsForm.itemArr[index1].correct
			">Is Answer</el-checkbox>
						</el-col>

					</el-row>

				</template>
				<template v-if="optionsForm.type == 3">
					<el-form-item label="Answer" prop="calResult">
						<el-input v-model="optionsForm.calResult" />
					</el-form-item>
				</template>
				<!-- <template v-if="optionsForm.type == 4">
					<el-form-item label="Answer">
						<el-input type="textarea" autosize placeholder="" v-model="optionsForm.textValue">
						</el-input>
					</el-form-item>
				</template> -->

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitOptionsForm">SAVE</el-button>
				<el-button @click="cancelOptionsForm">CANCEL</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { gameList, allTypeList, addSubject, addOptions, deleteSubject } from '@/api/game/subject';
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
			gameTypesData: [],
			title: "",
			open: false,
			optionsOpen: false,
			queryParams: {
				pageNum: 1,
				pageSize: 10,
			},
			form: {},
			optionsForm: {
				// itemArr: [{ item: '', result: false }]
			},
			rules: {
				questionTypeId: [
					{ required: true, message: "Please select the game type", trigger: "change" }
				],
				questionTitle: [
					{ required: true, message: "Please enter the question title", trigger: "blur" }
				],
				type: [
					{ required: true, message: "Please select the question type", trigger: "change" }
				]
			},
			optionsRules: {
				questionTitle: [
					{ required: true, message: "Please enter the question title", trigger: "blur" }
				],
				typeValue: [
					{ required: true, message: "Please enter the question type", trigger: "change" }
				],
				calResult: [
					{ required: true, message: "Please enter the answer", trigger: "blur" }
				],
			},
			//
			actionUrl: '',
			//
			headers: '',
			//
			imgUrl: '',
			//
			imageUrl: '',
		};
	},
	created() {
		this.getList();
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
		getAllTypes() {
			allTypeList().then(res => {
				if (res.code == 200) {
					this.gameTypesData = res.data;
				}
			})
		},
		handleDetail(row) {
			this.$router.push({ path: "/game/question", query: { gameId: row.id } });
		},
		addOption(index) {
			let obj = {
				item: '',
				correct: false,
			}
			this.optionsForm.itemArr.push(obj);
		},

		//
		cancel() {
			this.open = false;
			this.reset();
		},
		cancelOptionsForm() {
			this.optionsOpen = false;
			this.reset();
		},
		//
		reset() {
			this.form = {
				questionTypeId: undefined,
				questionTitle: undefined,
				type: undefined
			};
			this.optionsForm = {
				questionTitle: undefined,
				typeValue: undefined,
			};
			this.gameTypesData = [];
			this.resetForm("form");
			this.resetForm("optionsForm");
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
			this.$router.push({ path: "/game/question" });
			// this.$router.push({ path: "/game/gameType" });
			// this.reset();
			// this.getAllTypes();
			// this.open = true;
			// this.title = "New qusestion added";
		},
		handleDelete(row) {
			this.$confirm('Do you want to delete this quiz?', "Warning", {
				confirmButtonText: "YES",
				cancelButtonText: "NO",
				type: "warning"
			}).then(function () {
				return deleteSubject(row.id);
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
					addSubject(this.form).then(response => {
						this.msgSuccess("New successfully added");
						this.open = false;
						this.getList();
					});

				}
			});
		},
		submitOptionsForm: function () {
			let flagNum = 0;
			this.optionsForm.itemArr.forEach(item => {
				if (item.correct) {//true
					flagNum++;
				}
			})
			if (this.optionsForm.type == 1) {
				if (!(flagNum == 1)) {
					this.msgError("There can only be one correct answer");
					return;
				}
			} else if (this.optionsForm.type == 2) {
				if (flagNum == 0) {
					this.msgError("Choose at least one correct answer");
					return;
				}
			};
			this.$refs["optionsForm"].validate(valid => {
				if (valid) {
					addOptions(this.optionsForm).then(response => {
						this.msgSuccess("New successfully added");
						this.optionsOpen = false;
						this.getList();
					});

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
