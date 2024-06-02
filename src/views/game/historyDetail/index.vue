<template>
	<div class="mod-menu-home app-container">
		<el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="200px" class="demo-ruleForm">
			<el-form-item label="Game name：" prop="gameTitle">
				<el-input v-model="ruleForm.gameTitle" style="width:60%" placeholder="Please enter the game name"
					readonly=""></el-input>
			</el-form-item>
			<el-form-item label="Game type：" prop="questionTypeId">
				<el-select v-model="ruleForm.questionTypeId" placeholder="Please select a game type" style="width:60%;"
					disabled>
					<el-option :label="item.questionTypeNm" :value="item.id" v-for="item in typeList"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="Game content：">
				<ol>
					<li v-for="(item, index) in questionList" :key="item.id">
						<dl v-if="item.type == 'radio'">
							<dt>{{ item.title }}</dt>
							<el-radio v-model="item.choice" :label="ele.id" v-for="(ele, ind) in item.questionItem"
								@change="changeRadio" checked="true" :key="ele.id" disabled>{{ ele.content
								}}</el-radio>
						</dl>
						<dl v-if="item.type == 'checkbox'">
							<dt>{{ item.title }}</dt>
							<el-checkbox-group v-model="item.choice" v-for="(ele, ind) in item.questionItem"
								:key="ele.id">
								<el-checkbox :label="ele.id" disabled>{{ ele.content }}</el-checkbox>
							</el-checkbox-group>
						</dl>

						<dl v-if="item.type == 'input'">
							<dt>{{ item.title }}</dt>
							<el-input readonly v-model="item.choice" placeholder="Please enter reference content"
								style="width:60%"></el-input>
						</dl>
					</li>
				</ol>
			</el-form-item>
			<el-form-item>
				<el-button @click="cancelPage">Return</el-button>
			</el-form-item>
		</el-form>

		<el-backtop></el-backtop>
	</div>
</template>
<script>
import { historyDeatail } from '@/api/game/history';
import { allTypeList } from '@/api/game/subject';
import { getTemplateById } from '@/api/game/question';
import { getToken } from "@/utils/auth";
export default {
	data() {
		return {
			id: undefined,
			historyId: undefined,
			headers: '',
			typeList: [],
			pageQuery: {
				pageSize: 10,
				pageNum: 1
			},

			title: "New",///
			ruleForm: {
				isDisabled: false
			},
			rules: {
				gameTitle: [
					{ required: true, message: 'Please enter the game name', trigger: 'blur' },
				],
				dynamicformTypeId: [
					{ required: true, message: 'Please select a game type', trigger: 'change' }
				],
				remark: [
					{ max: 200, message: 'Word limit: 200 or less', trigger: 'blur' }
				]
			},
			questionList: []
		};
	},
	mounted() {
		this.headers = {
			'Authorization': 'Bearer ' + getToken()
		}
		this.historyId = this.$route.query.historyId;
		this.id = this.$route.query.id;
		this.getRouterValue()
		this.getTypePage()
	},
	methods: {
		changeRadio(val) {
			console.log(val);
		},
		getTypePage() {
			allTypeList().then(res => {
				if (res.code == 200) {
					this.typeList = res.data;
				}
			})
		},


		//
		cancelPage() {
			this.$refs.ruleForm.resetFields();
			this.$router.go(-1)
		},
		//
		getRouterValue() {
			let params = {
				historyId: this.historyId,
				id: this.id
			}
			historyDeatail(params).then(res => {
				res.data.fields = JSON.parse(res.data.fields);
				console.log(res.data);
				res.data.fields.forEach(item => {
					if (item.type == "radio") {
						item.choice = Number(item.choice);
					} else if (item.type == "checkbox") {
						item.choice = JSON.parse(item.choice);
					}

				})
				this.ruleForm = res.data;
				this.questionList = res.data.fields
				console.log(this.questionList, '');

			})

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


::v-deep .el-radio__input.is-disabled+span.el-radio__label {
	color: #000;
}


::v-deep .el-radio__input.is-disabled.is-checked .el-radio__inner {
	border-color: #1890ff;
	background: #1890ff;
}

::v-deep .el-checkbox__input.is-disabled+span.el-checkbox__label {
	color: #000;
}

::v-deep .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
	background-color: #1890ff;
	border-color: #1890ff;
}
</style>
