<template>
  <div class="dashboard">
    <div class="center examplex">
      <vs-navbar center-collapsed v-model="active">
        <template #left>
          <i style="font-size: 25px" class='bx bx-plus-medical'></i>
        </template>
        <vs-navbar-item
          v-if="this.$store.getters.getRole === 'ROLE_ADMIN'"
          :active="active == 'certificate_request'"
          v-on:click="$router.push('certificate_request')"
          id="certificate_request"
        >
          Certificate Request
        </vs-navbar-item>
        <vs-navbar-item
          :active="active == 'devices'"
          v-on:click="$router.push('devices')"
          id="devices"
        >
          Devices
        </vs-navbar-item>
        <vs-navbar-item
          :active="active == 'alarms'"
          v-on:click="$router.push('alarms')"
          id="alarms"
        >
          Alarms
        </vs-navbar-item>
        <vs-navbar-item
          v-if="this.$store.getters.getRole === 'ROLE_ADMIN'"
          :active="active == 'logs'"
          v-on:click="$router.push('logs')"
          id="logs"
        >
          Logs
        </vs-navbar-item>
        <vs-navbar-item
          v-if="this.$store.getters.getRole === 'ROLE_ADMIN'"
          :active="active == 'security'"
          v-on:click="$router.push('security')"
          id="security"
        >
          Security
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
        this.$router.push({path: 'login'})
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
    console.log(this.$store.getters.getRole);
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