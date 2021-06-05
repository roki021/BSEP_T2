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
    document.title = 'Admin Platform'

    var token = localStorage.getItem('user-token')
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + token

    axios.interceptors.response.use(undefined, (err) => {
      let store = this.$store
      let router = this.$router

      return new Promise(function () {
        if (err.response.status == 401) {
          store.dispatch('requestLogout').then(() => {
            router.push({path: 'login'})
          })
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
  padding: 0;
  font-family: Avenir, Helvetica, Arial, sans-serif;
}

#app {
  font-size: 12px;
  color: #2c3e50;
  background-color: #31374d;
  width: 100%;
  height: 100%;
}

</style>
