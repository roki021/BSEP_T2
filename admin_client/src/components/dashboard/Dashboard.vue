<template>
  <div class="dashboard">
    <div class="center examplex">
      <vs-navbar center-collapsed v-model="active">
        <template #left>
          <i style="font-size: 25px" class="bx bxs-dashboard"></i>
        </template>
        <vs-navbar-item :active="active == 'accounts'" v-on:click="$router.push('accounts')" id="accounts">
          Accounts
        </vs-navbar-item>
        <vs-navbar-item
          :active="active == 'certificates'"
          v-on:click="$router.push('/certificates')"
          id="certificates"
        >
          Certificates
        </vs-navbar-item>
        <vs-navbar-item
          :active="active == 'requests'"
          v-on:click="$router.push('/requests')"
          id="requests"
        >
          Requests
        </vs-navbar-item>
        <vs-navbar-item :active="active == 'hospitals'" v-on:click="$router.push('/hospitals')" id="hospitals">
          Hospitals
        </vs-navbar-item>
        <template #right>
          <vs-button v-on:click="logout" flat>Logout</vs-button>
        </template>
      </vs-navbar>
    </div>

    <div class="general">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    active: '',
  }),
  methods: {
    logout: function () {
      this.$store.dispatch('requestLogout').then(() => {
        this.$router.push({path: '/login'})
        this.$vs.notification({
            color: null,
            position: "top-right",
            title: "Logout successful",
            text: "You have successfully logged out.",
        });
      })
    }
  },
  mounted: function () {
    var raw = this.$route.path
    this.active = raw.substring(1, raw.length)
  }
};
</script>

<style>
.dashboard {
  width: 100%;
  height: 100%;
}

.general {
  padding-top: 100px;
}

.container {
    padding: 20px;
    border: 1px solid transparent;
    background: #ffffff;
    border-radius: 8px;
}
</style>