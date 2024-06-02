<template>
	<div class="mod-menu-home app-container">
		<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="200px" class="demo-ruleForm">
			<el-form-item label="Game name：" prop="gameTitle">
				<el-input v-model="ruleForm.gameTitle" style="width:60%"
					placeholder="Please enter the game name"></el-input>
			</el-form-item>
			<el-form-item label="Game type：" prop="questionTypeId">
				<el-select v-model="ruleForm.questionTypeId" placeholder="Please select a game type" style="width:60%;">
					<el-option :label="item.questionTypeNm" :value="item.id" v-for="item in typeList"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="Game sort：" prop="sort">
				<el-input v-model="ruleForm.sort" style="width:60%" placeholder="Please enter the game sort"></el-input>
			</el-form-item>
			<el-form-item label="Game description：" prop="remark">
				<el-input v-model="ruleForm.remark" style="width:60%"
					placeholder="Please fill in the game description (within 500 words)" type="textarea" autosize
					maxlength="500" show-word-limit></el-input>
			</el-form-item>
			<el-form-item label="Game content：">
				<el-button plain @click="handleAdd">+New content</el-button>
				<ol>
					<li v-for="(item, index) in questionList" :key="item.id">
						<dl v-if="item.type == 'radio'">
							<dt :class="item.require ? 'require' : ''">{{ item.title }}</dt>
							<el-radio v-model="ele.v_model" :label="ele.content" v-for="(ele, ind) in item.questionItem"
								checked="true" :key="ele.id">{{ ele.content
								}}</el-radio>
						</dl>
						<dl v-if="item.type == 'checkbox'">
							<dt :class="item.require ? 'require' : ''">{{ item.title }}</dt>
							<el-checkbox-group v-model="ele.checkList" v-for="(ele, ind) in item.questionItem"
								:key="ele.id">
								<el-checkbox :label="ele.content"></el-checkbox>
							</el-checkbox-group>
						</dl>

						<dl v-if="item.type == 'input'">
							<dt :class="item.require ? 'require' : ''">{{ item.title }}</dt>
							<el-input v-model="input" placeholder="Please enter reference content"
								style="width:60%"></el-input>
						</dl>
						<div class="operation">
							<el-button v-show="item.gameId && item.type != 'input'" size="mini"
								@click="editRelation(item)">relation</el-button>
							<el-button size="mini" @click="editContent(item)">edit</el-button>
							<el-popconfirm title="Are you sure you want to delete it" @confirm="deleteItem(index)"
								style="margin-left:10px;margin-right: 10px;">
								<span slot="reference" style="cursor: pointer;color:#1890FF">
									<el-button size="mini" slot="reference">delete</el-button>
								</span>
							</el-popconfirm>
							<el-button size="mini" :disabled="index === 0" @click="moveUp(index)">Move up</el-button>
							<el-button size="mini" :disabled="index === questionList.length - 1"
								@click="moveDown(index)">Move down</el-button>
						</div>
					</li>
				</ol>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="dataFormSubmit">{{ updateTitle }}</el-button>
				<el-button @click="cancelPage">Cancel</el-button>
			</el-form-item>
		</el-form>
		<!--  -->
		<el-dialog title="New content" :visible.sync="dialogVisible" width="60%" @close="cancelDialog">
			<div>
				<el-form :model="contentForm" :rules="contentrules" ref="contentRuleForm" label-width="100px"
					class="demo-ruleForm">
					<el-form-item label="Title：" prop="title">
						<el-input v-model="contentForm.title"></el-input>
					</el-form-item>
					<el-form-item label="Type：" prop="type">
						<el-select v-model="contentForm.type" placeholder="Please select a type" style="width:100%"
							@change="getMarkChange">
							<el-option label="Single Choice" value="radio"></el-option>
							<el-option label="Multiple Choice" value="checkbox"></el-option>
							<el-option label="Q&A Questions" value="input"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="Hint：" prop="hint">
						<el-input v-model="contentForm.hint"></el-input>
					</el-form-item>
					<el-form-item label="Answer：" prop="answer">
						<el-input v-model="contentForm.answer" type="textarea" autosize maxlength="500"
							show-word-limit></el-input>
					</el-form-item>
					<el-form-item label="Options：" prop="questionItem"
						v-if="contentForm.type == 'checkbox' || contentForm.type == 'radio'">
						<el-button plain size="mini" @click="addLine()">+New option</el-button>
						<el-table :data="contentForm.questionItem" style="width: 100%" border class="tableBB">
							<el-table-column label="sequence" type="index" width="85" align="center">
							</el-table-column>
							<el-table-column label="Option content" prop="content" align="center">
								<template slot-scope="scope">
									<span v-show="scope.row.isShow">{{ scope.row.content }}</span>
									<el-input type="text" :placeholder="scope.row.content" v-model="scope.row.content"
										v-show="!scope.row.isShow" palceholder="Please enter" />
								</template>
							</el-table-column>
							<el-table-column label="Option score" prop="score" align="center">
								<template slot-scope="scope">
									<span v-show="scope.row.isShow">{{ scope.row.score }}</span>
									<el-input type="text" :placeholder="scope.row.score" v-model="scope.row.score"
										v-show="!scope.row.isShow" palceholder="Please enter" />
								</template>
							</el-table-column>

							<el-table-column align="center" label="Controls" width="200">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" plain
										@click="handleEdit(scope.$index, scope.row)"
										v-show="scope.row.isShow">edit</el-button>
									<el-button @click="hold(scope.$index, scope.row)" size="mini" type="success" plain
										v-show="!scope.row.isShow">save</el-button>
									<el-button size="mini" type="danger" plain
										@click="handleDelete(scope.$index, scope.row)">delete</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-form-item>
				</el-form>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="cancelDialog">Cancel</el-button>
				<el-button type="primary" @click="submitContentForm">OK</el-button>
			</span>
		</el-dialog>
		<!--  -->
		<el-dialog title="Edit Relationship" :visible.sync="relationVisible" width="40%" @close="cancelRelation">
			<div>
				<el-form :model="relationForm" :rules="relationrules" ref="relationForm" label-width="110px"
					class="demo-ruleForm">
					<el-form-item label="Title：" prop="title">
						<el-input v-model="relationForm.title" readonly></el-input>
					</el-form-item>
					<el-form-item label="Type：" prop="type">
						<el-select disabled v-model="relationForm.type" placeholder="Please select a type"
							style="width:100%">
							<el-option label="Single Choice" value="radio"></el-option>
							<el-option label="Multiple Choice" value="checkbox"></el-option>
							<el-option label="Q&A Questions" value="input"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="Relationship：" v-if="relationForm.type == 'checkbox'">
						<el-button plain size="mini" @click="addRelationTable()">+New relation</el-button>
						<el-table :data="relationForm.relationItems" border class="tableBB">
							<el-table-column label="Choose Option" align="center">
								<template slot-scope="scope">
									<el-select v-model="scope.row.option" multiple placeholder="plesase select option"
										@change="changeOption($event)">
										<el-option v-for="item in relationForm.questionItem" :label="item.item"
											:key="item.id" :value="item.id"></el-option>
									</el-select>
								</template>
							</el-table-column>

							<el-table-column align="center" label="Choose Subject111">
								<template slot-scope="scope">
									<el-select v-model="scope.row.subject" @change="changeSubject($event)"
										placeholder="plesase select option">
										<el-option v-for="item in relationForm.fields   " :key="item.id"
											:label="item.title" :value="item.id" />
									</el-select>
								</template>
							</el-table-column>
							<el-table-column align="center" label="Controls" width="200">
								<template slot-scope="scope">
									<el-button size="mini" type="danger" plain
										@click="handleDeleteRelation(scope.$index, scope.row)">delete</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-form-item>
					<el-form-item label="Relationship：" v-if="relationForm.type == 'radio'">
						<el-button plain size="mini" @click="addRelationTable()">+New relation</el-button>
						<el-table :data="relationForm.relationItems" border class="tableBB">
							<el-table-column label="Choose Option" align="center">
								<template slot-scope="scope">
									<el-select v-model="scope.row.option" placeholder="plesase select option"
										@change="changeOption($event)">
										<el-option v-for="item in relationForm.questionItem" :label="item.item"
											:key="item.id" :value="item.id"></el-option>
									</el-select>
								</template>
							</el-table-column>

							<el-table-column align="center" label="Choose Subject">
								<template slot-scope="scope">
									<el-select v-model="scope.row.subject" @change="changeSubject($event)"
										placeholder="plesase select option">
										<el-option v-for="item in relationForm.fields   " :key="item.id"
											:label="item.title" :value="item.id" />
									</el-select>
								</template>
							</el-table-column>
							<el-table-column align="center" label="Controls" width="200">
								<template slot-scope="scope">
									<el-button size="mini" type="danger" plain
										@click="handleDeleteRelation(scope.$index, scope.row)">delete</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-form-item>
				</el-form>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="cancelRelation">Cancel</el-button>
				<el-button type="primary" @click="submitRelationForm">OK</el-button>
			</span>
		</el-dialog>
		<el-backtop></el-backtop>
	</div>
</template>
<script>

import { createGame, allTypeList } from '@/api/game/subject';
import { getTemplateById } from '@/api/game/question';
import { getToken } from "@/utils/auth";
export default {
	components: {
		// WangEditor,
	},
	data() {
		return {
			gameId: undefined,
			headers: '',
			typeList: [],
			pageQuery: {
				pageSize: 10,
				pageNum: 1
			},
			//
			searchParam: {
			},
			//
			pageVO: {
				list: [],
				total: 0,
				pages: 0
			},
			markData: '',
			DelVisible: false,
			title: "New",///
			updateTitle: 'Create',
			dialogVisible: false,
			input: '',//
			value1: '',//
			radio: '1',
			checkList: ['Checked and disabled', 'checkbox A'],
			ruleForm: {
				isDisabled: false
			},
			contentForm: {
				questionItem: []
			},
			relationForm: {
				questionItem: []
			},
			relationVisible: false,
			rules: {
				dynamicformTypeId: [
					{ required: true, message: 'Please select a game type', trigger: 'change' }
				],
				sort: [{
					required: true, message: 'Sorting cannot be empty', trigger: 'blur'
				}],
				remark: [
					{ max: 500, message: 'Word limit: 500 or less', trigger: 'blur' }
				]
			},
			contentrules: {
				title: [
					{ required: true, message: 'Please enter the game name', trigger: 'blur' }
				],
				type: [
					{ required: true, message: 'Please select a game type', trigger: 'change' }
				],
				hint: [
					{ required: true, message: 'Please enter the hint', trigger: 'blur' }
				],
				answer: [
					{ required: true, message: 'Please enter the answer', trigger: 'blur' }
				],
				require: [
					{ required: true, message: 'Please select whether this field is required', trigger: 'change' }
				],
				questionItem: [
					{ required: true, message: 'Please enter options', trigger: 'change' }
				]
			},
			relationrules: {
				title: [
					{ required: true, message: 'Please enter the game name', trigger: 'blur' }
				],
				type: [
					{ required: true, message: 'Please select a game type', trigger: 'change' }
				],
			},
			questionList: [
			]
		};
	},
	mounted() {
		this.headers = {
			'Authorization': 'Bearer ' + getToken()
		}
		this.gameId = this.$route.query.gameId;
		this.getRouterValue()
		this.getTypePage()
	},
	methods: {
		handleAdd() {
			this.dialogVisible = true
			this.contentForm = {
				title: '',
				type: '',
				require: false,
				questionItem: []
			}
		},
		//
		deleteItem(index) {
			this.questionList.splice(index, 1);
		},
		/*  */
		handleEdit(index, row) {
			row.isShow = false;
		},
		/*  */
		hold(index, row) {
			row.isShow = true;
		},
		/*  */
		handleDelete(index, row) {
			this.contentForm.questionItem.splice(index, 1);
		},
		handleDeleteRelation(index, row) {
			this.relationForm.relationItems.splice(index, 1);
			console.log(this.relationForm.relationItems);
		},
		/*  ()*/
		addLine() {
			let obj = {
				content: "",
				isShow: false,
				isRepulsion: false
			};
			this.contentForm.questionItem.push(Object.assign({}, obj));
		},
		addRelationTable() {
			let obj = {
				option: undefined,
				subject: undefined
			};
			this.relationForm.relationItems.push(Object.assign({}, obj));
			console.log(this.relationForm.relationItems);
		},
		changeOption(event) {
			console.log(event, '')
			// this.relationForm.questionItem.forEach(item => {
			// 	if (item.id == event) {
			// 		item.disabled = true;
			// 	}
			// })
		},
		changeSubject(event) {
			console.log(event, '')
		},
		getTypePage() {
			allTypeList().then(res => {
				if (res.code == 200) {
					this.typeList = res.data;
				}
			})
		},
		//
		getMarkChange(value) {
			console.log("value", value);
			this.markData = value
			this.contentForm.questionItem = []
		},
		//
		moveUp(index) {
			let questionList = this.questionList
			questionList.splice(index - 1, 1, ...questionList.splice(index, 1, questionList[index - 1]))
		},
		//
		moveDown(index) {
			let questionList = this.questionList
			questionList.splice(index, 1, ...questionList.splice(index + 1, 1, questionList[index]))
		},
		//
		editContent(item) {
			this.contentForm = { ...item }
			console.log(item, '')
			// if (this.contentForm.require == true) {
			//   this.contentForm.require = ''
			// } else {
			//   this.contentForm.require = ''
			// }
			// console.log(this.contentForm, '')
			this.dialogVisible = true

		},
		editRelation(item) {
			console.log(item, '')
			this.optionItems = item.questionItem;
			// item.relationItems = [];
			let fields = JSON.parse(JSON.stringify(this.ruleForm.fields));
			item.fields = fields;
			this.relationForm = { ...item };
			this.relationVisible = true;
		},
		/*    */
		dataFormSubmit() {
			console.log(this.questionList, '');

			this.$refs.ruleForm.validate((valid) => {
				if (valid) {
					console.log("console.log(this.ruleForm.fields)", this.ruleForm)
					this.ruleForm.fields = JSON.stringify(this.questionList)
					createGame(this.ruleForm).then(res => {
						if (res.code == 200) {
							this.msgSuccess("Created successfully")
							this.$router.go(-1);
						}
					})
					// const request = this.$route.query.id ? api.update(this.ruleForm) : api.save(this.ruleForm)
					// request.then(data => {
					//   this.$message({
					//     message: this.$t('table.actionSuccess'),
					//     type: 'success',
					//     duration: 1500,
					//     onClose: () => {
					//       this.$refs.ruleForm.resetFields()
					//       this.$router.go(-1)
					//     }
					//   })
					// })
				} else {
					this.$nextTick(() => {
						let isError = document.getElementsByClassName('is-error')
						isError[0].scrollIntoView({
							block: 'center',
							behavior: 'smooth',
						})
					})
				}
			});
		},
		//
		submitContentForm() {
			if (this.contentForm.type == 'radio') {
				this.contentForm.questionItem.forEach(ele => {
					if (ele.score && ele.score > 0) {
						ele.v_model = ele.content;
					} else {
						ele.score = 0;
						ele.v_model = "";
					}
				});
			}
			if (this.contentForm.type == 'checkbox') {
				this.contentForm.questionItem.forEach(ele => {
					ele.checkList = [];
					if (ele.score && ele.score > 0) {
						ele.checkList.push(ele.content);
					} else {
						ele.checkList = [];
					}
				});
			}

			console.log(this.contentForm, '')
			this.$refs.contentRuleForm.validate((valid) => {
				if (valid) {
					this.contentForm.require = false
					let Obj = { ...this.contentForm }
					if (Obj.id) {
						this.questionList = this.questionList.map(ele => {
							return ele.id == Obj.id ? Obj : ele;
						})
					} else {
						Obj.id = Date.now()
						this.questionList = [...this.questionList, (Object.assign({}, Obj))]
					}
					console.log(this.questionList, '')
					this.dialogVisible = false
					this.$refs.contentRuleForm.resetFields();
				} else {
					return false;
				}
			});
		},
		submitRelationForm() {

			let Obj = { ...this.relationForm }
			console.log(Obj);
			if (Obj.id) {
				this.questionList = this.questionList.map(ele => {
					return ele.id == Obj.id ? Obj : ele;
				})
			}
			this.relationVisible = false
			this.$refs.relationForm.resetFields();

		},
		//
		cancelPage() {
			this.$refs.ruleForm.resetFields();
			this.$router.go(-1)
		},
		//
		cancelDialog() {
			this.$refs.contentRuleForm.resetFields();
			this.dialogVisible = false
		},
		//
		cancelRelation() {
			this.$refs.relationForm.resetFields();
			this.relationVisible = false
		},
		//
		getRouterValue() {
			if (this.$route.query.gameId) {
				this.title = 'edit';
				this.updateTitle = 'save';
				getTemplateById(this.$route.query.gameId).then(res => {
					res.data.fields = JSON.parse(res.data.fields);
					res.data.fields.forEach(item => {
						item.relationItems.forEach(ele => {
							if (ele.option) {
								if (ele.option.indexOf(']') > 0) {
									ele.option = ele.option.substring(1);
									ele.option = ele.option.substring(0, ele.option.length - 1)
									ele.option = ele.option.split(',')
									let arr = []
									ele.option.forEach(i => {
										arr.push(Number(i));
									})
									ele.option = arr;
								} else {
									ele.option = Number(ele.option)
								}

							}
							if (ele.subject) {
								ele.subject = Number(ele.subject)
							}
						})

						if (item.type == "radio") {
							item.questionItem.forEach(ele => {
								if (ele.score && ele.score > 0) {
									ele.v_model = ele.content;
								} else {
									ele.score = 0;
									ele.v_model = "";
								}
							})
						}

						if (item.type == "checkbox") {
							item.questionItem.forEach(ele => {
								ele.checkList = [];
								if (ele.score && ele.score > 0) {
									ele.checkList.push(ele.content);
								} else {
									ele.checkList = [];
								}
							})
						}


					})
					this.ruleForm = res.data;
					console.log(this.ruleForm, '');
					this.questionList = res.data.fields
				})
			}
		}
	}
}
</script>

<style scoped lang="scss">
.require:before {
	content: '* ';
	color: red;
}

::v-deep .tableBB .el-input__inner {
	border: none !important;
}

.operation {
	width: 60%;
	text-align: right;
}

.header {
	padding: 24px;
	font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
	font-weight: 400;
	-webkit-font-smoothing: antialiased;
	-webkit-tap-highlight-color: transparent;
	font-size: 18px;
	font-weight: bolder;
}
</style>
