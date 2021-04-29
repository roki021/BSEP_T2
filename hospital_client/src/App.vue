<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'App',
  created() {
    document.title = 'Hospital Platform'

    var token = localStorage.getItem('user-token')
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + token

    axios.interceptors.response.use(undefined, function (err) {
      return new Promise(function () {
        if (err.status === 401 && err.config && !err.config.__isRetryRequest) {
        // if you ever get an unauthorized, logout the user
          this.$store.dispatch('requestLogout')
        // you can also redirect to /login if needed !
        }
        throw err
      })
    })
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
