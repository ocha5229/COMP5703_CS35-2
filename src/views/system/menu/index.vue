<template>
	<div class="app-container">


		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
					v-hasPermi="['system:menu:add']">new</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="info" plain icon="el-icon-sort" size="mini"
					@click="toggleExpandAll">Unfold/fold</el-button>
			</el-col>
		</el-row>

		<el-table v-if="refreshTable" v-loading="loading" :data="menuList" row-key="menuId"
			:default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
			<el-table-column prop="menuName" label="Menu name" :show-overflow-tooltip="true"
				width="260"></el-table-column>
			<el-table-column prop="icon" label="icon" align="center" width="100">
				<template slot-scope="scope">
					<svg-icon :icon-class="scope.row.icon" />
				</template>
			</el-table-column>
			<el-table-column prop="orderNum" label="sort" width="60"></el-table-column>
			<el-table-column prop="perms" label="Permission identification"
				:show-overflow-tooltip="true"></el-table-column>
			<el-table-column prop="component" label="Component path" :show-overflow-tooltip="true"></el-table-column>
			<el-table-column prop="status" label="status" width="80">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
				</template>
			</el-table-column>
			<!-- <el-table-column label="Creation time" align="center" prop="createTime">
				<template slot-scope="scope">
					<span>{{ parseTime(scope.row.createTime) }}</span>
				</template>
			</el-table-column> -->
			<el-table-column label="operate" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
						v-hasPermi="['system:menu:edit']">edit</el-button>
					<el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
						v-hasPermi="['system:menu:add']">new</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
						v-hasPermi="['system:menu:remove']">delete</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--  -->
		<el-dialog :title="title" :visible.sync="open" width="880px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="200px">
				<el-row>
					<el-col :span="24">
						<el-form-item label="Parent menu" prop="parentId">
							<treeselect v-model="form.parentId" :options="menuOptions" :normalizer="normalizer"
								:show-count="true" placeholder="Select upper menu" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="Menu type" prop="menuType">
							<el-radio-group v-model="form.menuType">
								<el-radio label="M">catalogue</el-radio>
								<el-radio label="C">menu</el-radio>
								<el-radio label="F">Button</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="24" v-if="form.menuType != 'F'">
						<el-form-item label="Menu icon" prop="icon">
							<el-popover placement="bottom-start" width="460" trigger="click"
								@show="$refs['iconSelect'].reset()">
								<IconSelect ref="iconSelect" @selected="selected" :active-icon="form.icon" />
								<el-input slot="reference" v-model="form.icon" placeholder="Click Select icon" readonly>
									<svg-icon v-if="form.icon" slot="prefix" :icon-class="form.icon"
										style="width: 25px;" />
									<i v-else slot="prefix" class="el-icon-search el-input__icon" />
								</el-input>
							</el-popover>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="Menu name" prop="menuName">
							<el-input v-model="form.menuName" placeholder="Please enter a menu name" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="Display sort" prop="orderNum">
							<el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType != 'F'">
						<el-form-item prop="isFrame">
							<span slot="label">
								<el-tooltip
									content="If external link is selected, the routing address needs to start with 'http(s)://'"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Outside chain or not
							</span>
							<el-radio-group v-model="form.isFrame">
								<el-radio label="0">yes</el-radio>
								<el-radio label="1">no</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType != 'F'">
						<el-form-item prop="path">
							<span slot="label">
								<el-tooltip
									content="The routing address of the access, such as: 'user', if the external network address requires internal link access, start with 'http(s)://'"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Routing address
							</span>
							<el-input v-model="form.path" placeholder="Please enter the routing address" />
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType == 'C'">
						<el-form-item prop="component">
							<span slot="label">
								<el-tooltip
									content="The access component path, such as' system/user/index ', defaults to the 'views' directory"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Component path
							</span>
							<el-input v-model="form.component" placeholder="Please enter the component path" />
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType != 'M'">
						<el-form-item prop="perms">
							<el-input v-model="form.perms" placeholder="Please enter a permission identifier"
								maxlength="100" />
							<span slot="label">
								<el-tooltip
									content="Permission characters defined in the controller, such as: @PreAuthorize(' @ss.hasPermi('system:user:list') ')"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Permission character
							</span>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType == 'C'">
						<el-form-item prop="query">
							<el-input v-model="form.query" placeholder="Please enter route parameters"
								maxlength="255" />
							<span slot="label">
								<el-tooltip
									content='Default pass parameters for access routes, such as` {"id": 1, "name": "ry"} `'
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Route parameter
							</span>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType == 'C'">
						<el-form-item prop="isCache">
							<span slot="label">
								<el-tooltip
									content="If yes is selected, it will be cached by 'keep-alive', and the name and address of the matching component must be consistent"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Cache or not
							</span>
							<el-radio-group v-model="form.isCache">
								<el-radio label="0">yes</el-radio>
								<el-radio label="1">no</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType != 'F'">
						<el-form-item prop="visible">
							<span slot="label">
								<el-tooltip
									content="Select Hide and the route will not appear in the sidebar, but will still be accessible"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Display status
							</span>
							<el-radio-group v-model="form.visible">
								<el-radio v-for="dict in dict.type.sys_show_hide" :key="dict.value"
									:label="dict.value">{{
			dict.label }}</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item prop="status">
							<span slot="label">
								<el-tooltip
									content="If disabled is selected, the route will not appear in the sidebar and cannot be accessed"
									placement="top">
									<i class="el-icon-question"></i>
								</el-tooltip>
								Menu status
							</span>
							<el-radio-group v-model="form.status">
								<el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value"
									:label="dict.value">{{
			dict.label }}</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">OK</el-button>
				<el-button @click="cancel">Cancel</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { listMenu, getMenu, delMenu, addMenu, updateMenu } from "@/api/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/IconSelect";

export default {
	name: "Menu",
	dicts: ['sys_show_hide', 'sys_normal_disable'],
	components: { Treeselect, IconSelect },
	data() {
		return {
			//
			loading: true,
			//
			showSearch: true,
			//
			menuList: [],
			//
			menuOptions: [],
			//
			title: "",
			//
			open: false,
			// ，
			isExpandAll: false,
			//
			refreshTable: true,
			//
			queryParams: {
				menuName: undefined,
				visible: undefined
			},
			//
			form: {},
			//
			rules: {
				menuName: [
					{ required: true, message: "The menu name cannot be empty", trigger: "blur" }
				],
				orderNum: [
					{ required: true, message: "The menu order cannot be empty", trigger: "blur" }
				],
				path: [
					{ required: true, message: "The route address cannot be empty", trigger: "blur" }
				]
			}
		};
	},
	created() {
		this.getList();
	},
	methods: {
		//
		selected(name) {
			this.form.icon = name;
		},
		/**  */
		getList() {
			this.loading = true;
			listMenu(this.queryParams).then(response => {
				this.menuList = this.handleTree(response.data, "menuId");
				this.loading = false;
			});
		},
		/**  */
		normalizer(node) {
			if (node.children && !node.children.length) {
				delete node.children;
			}
			return {
				id: node.menuId,
				label: node.menuName,
				children: node.children
			};
		},
		/**  */
		getTreeselect() {
			listMenu().then(response => {
				this.menuOptions = [];
				const menu = { menuId: 0, menuName: 'Main category', children: [] };
				menu.children = this.handleTree(response.data, "menuId");
				this.menuOptions.push(menu);
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
				menuId: undefined,
				parentId: 0,
				menuName: undefined,
				icon: undefined,
				menuType: "M",
				orderNum: undefined,
				isFrame: "1",
				isCache: "0",
				visible: "0",
				status: "0"
			};
			this.resetForm("form");
		},
		/**  */
		handleQuery() {
			this.getList();
		},
		/**  */
		resetQuery() {
			this.resetForm("queryForm");
			this.handleQuery();
		},
		/**  */
		handleAdd(row) {
			this.reset();
			this.getTreeselect();
			if (row != null && row.menuId) {
				this.form.parentId = row.menuId;
			} else {
				this.form.parentId = 0;
			}
			this.open = true;
			this.title = "Add menu";
		},
		/** / */
		toggleExpandAll() {
			this.refreshTable = false;
			this.isExpandAll = !this.isExpandAll;
			this.$nextTick(() => {
				this.refreshTable = true;
			});
		},
		/**  */
		handleUpdate(row) {
			this.reset();
			this.getTreeselect();
			getMenu(row.menuId).then(response => {
				this.form = response.data;
				this.open = true;
				this.title = "Modify menu";
			});
		},
		/**  */
		submitForm: function () {
			this.$refs["form"].validate(valid => {
				if (valid) {
					if (this.form.menuId != undefined) {
						updateMenu(this.form).then(response => {
							this.$modal.msgSuccess("Modified successfully");
							this.open = false;
							this.getList();
						});
					} else {
						addMenu(this.form).then(response => {
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
			this.$modal.confirm('Do you want to delete the data item named "' + row.menuName + '"? ').then(function () {
				return delMenu(row.menuId);
			}).then(() => {
				this.getList();
				this.$modal.msgSuccess("Successfully deleted");
			}).catch(() => { });
		}
	}
};
</script>
