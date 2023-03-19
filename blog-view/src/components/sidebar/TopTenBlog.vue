<template>
	<!--常看笔记-->
	<div class="ui segments m-box">
		<div class="ui secondary segment"><i class="bookmark icon"></i>常看笔记</div>
		<div class="ui yellow segment">
			<div class="ui divided items">
				<div class="m-item" v-for="blog in topTenBlogList" :key="blog.id" @click.prevent="toBlog(blog)">
					<div class="info">
						<div class="date">{{ blog.createTime | dateFormat('YYYY-MM-DD') }}</div>
						<div class="title">{{ blog.title }}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "TopTenBlog",
		props: {
      topTenBlogList: {
				type: Array,
				required: true
			},
		},
		methods: {
			toBlog(blog) {
				this.$store.dispatch('goBlogPage', blog)
			}
		}
	}
</script>

<style scoped>
	.secondary.segment {
		padding: 10px;
	}

	.ui.divided.items .m-item:first-child {
		margin-top: 0;
	}

	.ui.divided.items .m-item {
		margin-top: .5rem;
		height: 4rem;
		position: relative;
		overflow: hidden;
		border-radius: 5px;
		cursor: pointer;
		user-select: none;
    border-style: solid;
	}

	.ui.divided.items .m-item .info {
		position: absolute;
		left: 0;
		right: 0;
		top: 0;
		padding: .5rem !important;
		font-size: 12px;
		color: black;
	}

	.ui.divided.items .m-item .info .title {
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		word-break: break-word;
	}
</style>
