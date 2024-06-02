<template>
	<div class="app-container">
		<el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange"
			:cell-style="cellStyle">
			<el-table-column label="ID" width="100" align="center" prop="id" />
			<el-table-column label="Game Type" align="center" prop="questionTypeNm" />
			<el-table-column label="Game Title" align="center" prop="gameTitle" />
			<el-table-column label="Game Description" align="center" prop="remark" />
			<el-table-column label="Game Sort" align="center" prop="sort" />
			<el-table-column label="Process" align="center" width="200">
				<template slot-scope="scope">
					<el-button v-show="scope.row.type != 4" size="mini" type="text" icon="el-icon-plus"
						:disabled="scope.row.historyId != 0" @click="handleReply(scope.row)">
						Start</el-button>
				</template>
			</el-table-column>

		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
			:limit.sync="queryParams.pageSize" @pagination="getList" />
		<div class="dialog-container">
			<el-dialog id="mydialog" custom-class="mydialog" ref="mydialog" :title="title + time" :visible.sync="open"
				append-to-body :height="height" class="flip-dialog" width="70%" :close-on-click-modal="false"
				:before-close="handleClose" :show-close="false">
				<el-form class="filp-content" ref="form" :model="form" :rules="rules" v-loading="formLoading"
					v-if="isFlipped">
					<el-row type="flex" justify="center" v-if="form.end == true">
						<el-col :span="24">
							<div>This questionnaire has ended. Thank you for your response</div>
						</el-col>
					</el-row>
					<div v-else>
						<el-row type="flex" justify="center">
							<el-col :span="24">
								<el-form-item>
									<div class="subTitle">{{ form.sort }} : {{ form.title }}</div>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row type="flex" justify="center">
							<el-col :span="24">
								<el-form-item prop="options" v-if="form.type == 'radio'">
									<el-radio-group v-model="form.options">
										<el-row class="myRow" justify="space-around"
											v-for="( item, index ) in  form.questionItem ">
											<el-radio @change="changeRadio" :key="item.id" :label="item.id"
												:disabled="item.disabled">{{
			item.item
		}}</el-radio>
										</el-row>
									</el-radio-group>
								</el-form-item>
								<el-form-item prop="checkbox" v-if="form.type == 'checkbox'">
									<el-checkbox-group v-model="checkboxList" @change="changeCheckbox">
										<el-checkbox v-for="( item, index ) in  form.questionItem " :key="item.id"
											:label="item.id" :disabled="item.disabled">
											{{ item.content }}
										</el-checkbox>
									</el-checkbox-group>
								</el-form-item>
								<el-form-item prop="options" v-if="form.type == 'input'">
									<el-input v-model="form.options" placeholder="Please enter the answer"
										style="min-width:480px"></el-input>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row type="flex" justify="center" v-show="form.hintShow">
							<el-col :span="24" style="margin-top:20px">
								<el-form-item>
									<el-input v-model="form.hint" placeholder="" style="min-width:480px"></el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</div>
				</el-form>
				<el-form class="filp-content" ref="answerForm" :model="answerForm" v-loading="formLoading"
					:rules="answerrules" v-else>
					<el-row type="flex" justify="center">
						<el-col :span="24">
							<el-form-item>
								<div class="subTitle">{{ answerForm.sort }} : {{ answerForm.title }}</div>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row type="flex" justify="center">
						<el-col :span="24">
							<el-form-item>
								<el-input v-model="answerForm.answer" type="textarea" autosize
									placeholder="Please enter the answer" style="min-width:780px" readonly></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					<!-- <el-row type="flex" justify="space-around">
						<el-col :span="24">
							<el-form-item prop="options" v-if="answerForm.type == 'radio'">
								<el-radio-group v-model="answerForm.options">
									<el-row class="myRow" justify="space-around"
										v-for="( item, index ) in  answerForm.questionItem ">
										<el-radio @change="changeRadio" :key="item.id" :label="item.id" disabled>{{
											item.item }}</el-radio>
									</el-row>
								</el-radio-group>
							</el-form-item>
							<el-form-item prop="checkbox" v-if="answerForm.type == 'checkbox'">
								<el-checkbox-group v-model="answercheckboxList">
									<el-checkbox v-for="( item, index ) in  answerForm.questionItem " :key="item.id"
										:label="item.id" disabled>
										{{ item.content }}
									</el-checkbox>
								</el-checkbox-group>
							</el-form-item>
							<el-form-item prop="options" v-if="answerForm.type == 'input'">
								<el-input v-model="answerForm.options" placeholder="Please enter the answer"
									style="width:60%" readonly></el-input>
							</el-form-item>
						</el-col>
					</el-row> -->
				</el-form>
				<div slot="footer" class="dialog-footer" v-if="form.end == true">
					<el-button @click="cancel">CANCEL</el-button>
				</div>
				<div slot="footer" class="dialog-footer" v-else>
					<el-button @click="showHint">Hint</el-button>
					<el-button @click="flipContent">TURN</el-button>
					<el-button type="primary" @click="submitForm">NEXT</el-button>
					<el-button @click="cancel">CANCEL</el-button>
				</div>
			</el-dialog>
			<el-dialog title="Answer Detail" style="color: black;" :close-on-click-modal="false"
				:visible.sync="detailOpen" @close="detailCancel" width="500px" append-to-body>
				<el-form ref="detailForm" :model="detailForm" label-width="120px">

					<el-form-item label="Answer Time" prop="answerTime" style="margin-bottom: 10px;">
						<el-input v-model="detailForm.answerTime" readonly />
					</el-form-item>

					<el-form-item label="Score" prop="total">
						<el-input v-model="detailForm.total" readonly />
					</el-form-item>

				</el-form>
			</el-dialog>
		</div>
	</div>
</template>

<script>
import { gameList, addSubject, getNextQuestion, getScore } from '@/api/game/subject';
import { getToken } from '@/utils/auth'
import store from "@/store";
export default {
	name: "type",
	components: {},
	data() {
		return {
			flag: null,//
			hour: 0,
			minute: 0,
			second: 0,
			time: "00:00:00",
			isFlipped: true,//
			height: "80%",
			formLoading: true,
			loading: true,
			ids: [],
			single: true,
			multiple: true,
			total: 0,
			tableData: [],
			title: "",
			open: false,
			detailOpen: false,
			queryParams: {
				pageNum: 1,
				pageSize: 10,
			},
			form: {},
			detailForm: {},
			checkboxList: [],
			answercheckboxList: [],
			rules: {
				options: [
					{ required: true, message: "Please answer the question", trigger: "blur" }
				],

			},
			answerrules: {
				options: [
					{ required: true, message: "Please answer the question", trigger: "blur" }
				],
			},
			answerForm: {},
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

		cellStyle(row) {//
			if (row.row.questionTypeNm == 'Easy') {
				return 'background:#E1F0DA'
			} else if (row.row.questionTypeNm == 'Medium') {
				return 'background:#D4E7C5'
			} else if (row.row.questionTypeNm == 'Hard') {
				return 'background:#BFD8AF'
			}
		},

		//
		start() {
			this.flag = setInterval(() => {
				this.second = this.second + 1;
				if (this.second >= 60) {
					this.second = 0;
					this.minute = this.minute + 1;
				}

				if (this.minute >= 60) {
					this.minute = 0;
					this.hour = this.hour + 1;
				}
				this.time =
					this.complZero(this.hour) +
					":" +
					this.complZero(this.minute) +
					":" +
					this.complZero(this.second);
			}, 1000);
		},
		//
		reset1() {
			window.clearInterval(this.flag);
			this.hour = 0;
			this.minute = 0;
			this.second = 0;
			this.time = "00:00:00";
		},
		//
		end() {
			this.flag = clearInterval(this.flag);
		},
		//
		complZero(n) {
			return n < 10 ? "0" + n : "" + n;
		},
		handleClose(done) {

			console.log(this.open, 'handleClose');
		},
		showHint() {
			console.log(this.form);
			this.form.hintShow = !this.form.hintShow;
		},
		flipContent() {
			if (this.isFlipped) {
				this.$refs["form"].validate(valid => {
					if (valid) {
						this.isFlipped = !this.isFlipped;
						this.answerForm = JSON.parse(JSON.stringify(this.form));

						this.form.questionItem.forEach(i => {
							i.disabled = true;
						})

						this.$nextTick(() => {

							const dialog = document.querySelector('.flip-dialog');
							dialog.classList.toggle('flipped');


							const dialogBg = document.querySelector('.el-dialog');
							dialogBg.classList.toggle('mydialog');
							dialogBg.classList.toggle('bg2');

						});
					}
				});
			} else {

				this.isFlipped = !this.isFlipped;
				this.answerForm = JSON.parse(JSON.stringify(this.form));

				this.form.questionItem.forEach(i => {
					i.disabled = true;
				})

				this.$nextTick(() => {

					const dialog = document.querySelector('.flip-dialog');
					dialog.classList.toggle('flipped');


					const dialogBg = document.querySelector('.el-dialog');
					dialogBg.classList.toggle('mydialog');
					dialogBg.classList.toggle('bg2');

				});

			}



		},
		/**  */
		getList() {
			this.headers = {
				'Authorization': 'Bearer ' + getToken()
			}
			this.loading = true;
			gameList().then(res => {
				if (res.code == 200) {
					this.tableData = res.rows;
					this.total = res.total;
					this.loading = false;
				}
			})
		},
		changeRadio(e) {
			console.log(e, 'radio');
		},
		changeCheckbox(e) {
			this.form.options = JSON.stringify(this.checkboxList);
			console.log(this.form.options, 'checkboxList');
		},
		handleReply(row) {
			this.start();
			this.formLoading = true;
			let params = {
				gameId: row.id
			}
			this.title = `${row.gameTitle}：`;
			getNextQuestion(params).then(res => {
				if (res.code == 200) {
					if (res.data.type != 'input') {
						res.data.questionItem.forEach(i => {
							if (i.type == 'radio') {
								i.disabled = false;
							}
							if (i.type == 'checkbox') {
								i.disabled = false;
							}
						})
					}

					this.form = res.data;
					this.form.hintShow = false;
					this.isFlipped = true;

					this.open = true;
					this.formLoading = false;
				}
			})
		},
		cancel() {
			this.reset1();
			this.open = false;
			// let gameId = 
			this.getScoreData();
		},
		getScoreData() {
			let params = {
				gameId: this.form.gameId,
				historyId: this.form.historyId
			}
			getScore(params).then(res => {
				if (res.code == 200) {
					if (res.data != null && res.data != undefined && res.data.length > 0) {
						this.detailForm = res.data && res.data[0];
						this.detailOpen = true;
					}
					this.getList();
					this.reset();
				}
			})
		},
		reset() {
			this.form = {};
			this.resetForm("form");
		},
		detailCancel() {
			this.detailCancel = false;
			this.detailForm = {};
			this.resetForm("detailForm");
		},

		handleQuery() {
			this.queryParams.pageNum = 1;
			this.getList();
		},

		resetQuery() {
			this.resetForm("queryForm");
			this.handleQuery();
		},

		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.id)
			this.selection = selection;
			this.single = selection.length != 1
			this.multiple = !selection.length
		},

		submitForm: function () {
			this.form.answerTime = this.time;
			console.log(this.form, '');
			this.form.questionId = this.form.id;
			if (typeof (this.form.options) == "Array") {
				this.form.options = JSON.stringify(this.form.options);
			};

			if (this.isFlipped) {
				this.$refs["form"].validate(valid => {
					if (valid) {
						this.formLoading = true;

						getNextQuestion(this.form).then(res => {
							if (res.code == 200) {
								if (res.data.type != 'input' && res.data.type != null) {
									res.data.questionItem.forEach(i => {
										if (i.type == 'radio') {
											i.disabled = false;
										}
										if (i.type == 'checkbox') {
											i.disabled = false;
										}
									})
								}

								this.form = res.data;
								this.form.hintShow = false;
								this.isFlipped = true; // ，
								console.log(this.form, 'this.form');
								this.formLoading = false;
							}
						});
					}
				});
			} else {
				this.formLoading = true;
				getNextQuestion(this.form).then(res => {
					if (res.code == 200) {
						if (res.data.type != 'input') {
							res.data.questionItem.forEach(i => {
								if (i.type == 'radio') {
									i.disabled = false;
								}
								if (i.type == 'checkbox') {
									i.disabled = false;
								}
							})
						}
						this.form = res.data;
						this.isFlipped = true; // ，
						console.log(this.form, '');
						this.formLoading = false;
					}
				});
			}

		},


	}
};
</script>
<style scoped lang="scss">
/*  */
.dialog-container {
	perspective: 1000px;
}

.flip-dialog {
	transition: transform 0.8s;
	transform-style: preserve-3d;
}

.flip-dialog.flipped {
	transform: rotateX(360deg);
	/*  rotateY  */
}

.myRow {
	margin-top: 20px;
}

.el-form-item.el-form-item--medium {
	margin-bottom: 0;
}


.filp-content {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	height: 100%;
}

// ::v-deep .el-dialog__body {
// 	display: flex;
// 	flex-direction: column;
// 	justify-content: center;
// 	align-items: center;
// 	height: 100%;
// }

.subTitle {
	font-size: 20px;
	// text-align: center;
}



::v-deep .mydialog {
	height: 80%;
	background-color: rgba(255, 0, 0, 0.0);
	color: #FFFFFF;
	background-image: url('../../../assets/images/dialog_bg.jpg');
	background-size: 100% 100%;

}

::v-deep .bg2 {
	height: 80%;
	background-color: rgba(255, 0, 0, 0.0);
	color: #FFFFFF;
	background-image: url('../../../assets/images/dialog_bg.jpg');
	background-size: 100% 100%;

}


::v-deep .el-dialog__header {
	padding-top: 10px !important;
	color: #fff;
	border-radius: 14px 14px 0 0;
}

::v-deep .el-dialog__body {
	border-top: 0 !important;
	background-color: rgb(255, 255, 255, 0);
	color: #FFFFFF;
}



/* Radio */
::v-deep .el-radio__input.is-checked+.el-radio__label {
	color: #000 !important;
}

/* Radio */
::v-deep .el-radio__input+.el-radio__label {
	color: #fff !important;

}

::v-deep .el-radio__input.is-disabled.is-checked .el-radio__inner {
	border-color: #1890ff;
	background: #1890ff;
}

/* Checkbox */
::v-deep .el-checkbox__input.is-checked+.el-checkbox__label {
	color: #000 !important;

}

::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
	background-color: #1890ff;
	border-color: #1890ff;
}

::v-deep .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
	background-color: #1890ff;
	border-color: #1890ff;
}

::v-deep .el-checkbox__input+.el-checkbox__label {
	color: #fff !important;

}

::v-deep .el-dialog__header {
	text-align: center;
}

::v-deep .el-dialog__footer {
	position: absolute;
	bottom: 10px;
	right: 10px;
}

::v-deep .el-dialog__title {
	color: #fff
}

::v-deep .el-form-item__label {
	color: black;
}
</style>
