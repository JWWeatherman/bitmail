<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>
<script>
  export default {
    mounted () {
      fetch(`/v1/triggerCompile`)
        .then(blob => blob.json())
        .then(data => console.log(data.compile_message))
    }
  }
</script>
<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
