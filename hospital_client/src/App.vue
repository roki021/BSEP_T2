<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'App',
  created() {
    document.title = 'Hospital Platform'
  }
}
</script>

<style lang="stylus">
html, body {
  width: 100%;
  height: 100%;
  margin: 0 !important;
  background-color: #31374d;
  padding: 0;
  font-family: Avenir, Helvetica, Arial, sans-serif;
}

#app {
  font-size: 12px;
  color: #2c3e50;
  width: 100%;
  height: 100%;
}

</style>
