<template>
	<div class="app-container">

		<el-table v-loading="loading" border :data="tableData">
			<!-- <el-table-column type="selection" width="55" align="center" /> -->
			<!-- <el-table-column label="ID" width="100" align="center" prop="userId" /> -->
			<el-table-column label="Sort" width="100" align="center" prop="index" />
			<el-table-column label="Nick Name" align="center" prop="nickName" />
			<el-table-column label="Score" align="center" prop="total" />
		</el-table>

		<!-- <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
			:limit.sync="queryParams.pageSize" @pagination="getList" /> -->

	</div>
</template>

<script>
import { rankingList } from '@/api/game/ranking';
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
			rankingList(this.queryParams).then(res => {
				if (res.code == 200) {
					res.rows.forEach((item, index) => {
						item.index = index + 1;
					})
					this.tableData = res.rows;
					// this.total = res.total;
					this.loading = false;
				}
			})
		},

	}
};
</script>
<style scoped lang="scss">
.el-col {
	margin-bottom: 10px;
}
</style>
