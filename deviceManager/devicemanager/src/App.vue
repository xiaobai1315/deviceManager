<template>
  <div id="app">
    <el-container class="home-container">
      <el-header>Header</el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu default-active=“1” class="el-menu-vertical-demo" background-color="#232323" :router="true" text-color="#fff">
            <el-menu-item v-for="menu in menuTitle" :key="menu.index" :index="menu.link">
              <i :class="menu.icon"></i>
              <span slot="title">{{menu.title}}</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      menuTitle: [
        {
          title: "自动化",
          icon: "el-icon-location",
          link: "autotest"
        },
        {
          title: "设备管理",
          icon: "el-icon-menu",
          link: "device"
        },
        {
          title: "连接终端",
          icon: "el-icon-document",
          link: "terminal"
        },
        {
          title: "查看日志",
          icon: "el-icon-document",
          link: "logbook"
        }
      ]
    };
  },
  methods: {
  
  }
};
</script>

<style>
.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}
.home-container {
  height: 100%;
}
.el-menu {
  height: 100%;
}
html,
body,
#app {
  height: 100%;
  margin: 0px;
}
</style>

