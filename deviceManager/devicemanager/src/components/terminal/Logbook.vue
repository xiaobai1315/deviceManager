<template>
  <div class="container-box">
    <div class="container-header">
      <h2>日志</h2>
    </div>
  </div>
</template>
<script>

export default {
    created() {
        this.readlog()
    },
    data() {
        return {
        
        };
    },
    
    methods: {
        readlog() {
            this.$http.get("http://localhost:8081/static/platform_info.log").then(
                function(response) {
                    console.log(response)
                    console.log(response.data)
                }
            )
        }
    }
};
</script>
 
<style scoped>
@import "xterm/css/xterm.css";
</style>