<template>
	<div class="app-container">

		<!-- <el-divider /> -->

	</div>
</template>

<script>
export default {
	name: "Index",
	data() {
		return {
			//
			version: "3.8.7"
		};
	},
	methods: {
		goTarget(href) {
			window.open(href, "_blank");
		}
	}
};
</script>


<style scoped lang="scss">
.app-container {
	height: 100vh;
	background-image: url("../assets/images/home_Bg.jpg");
	background-size: 100% 100%;
}
</style>
