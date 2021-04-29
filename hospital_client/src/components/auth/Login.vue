<template>
  <div class="login-container">
    <vs-row>
      <vs-col offset="2" w="8">
        <div class="content-inputs login-box">
          <div class="form-input">
            <vs-input v-model="username" minlength="10" placeholder="User name">
              <template #icon>
                <i class="bx bx-user"></i>
              </template>
              <template v-if="invalidUsername" #message-danger>
                Username Invalid
              </template>
            </vs-input>
          </div>
          <div class="form-input">
            <vs-input type="password" v-model="password" placeholder="Password">
              <template #icon>
                <i class="bx bx-lock-open-alt"></i>
              </template>
              <template v-if="invalidPassword" #message-danger>
                Password Invalid
              </template>
            </vs-input>
          </div>
          <div class="form-input">
                <vs-button v-on:click="login()" style="width: 100%">
                  Login
                </vs-button>
          </div>
        </div>
      </vs-col>
    </vs-row>
  </div>
</template>

<script>
export default {
  data: () => ({
    username: '',
    password: '',
    invalidUsername: false,
    invalidPassword: false
  }),
  methods: {
    login() {
      if (this.username.length == 0) {
        this.invalidUsername = true
        return
      }

      if (this.password.length == 0) {
        this.invalidPassword = true
        return
      }

      /*this.$store.commit('login', {
        username: this.username,
        password: this.password
      })*/
      this.$store.dispatch('requestAuth', {
        username: this.username,
        password: this.password
      }).then(() => {
        this.$router.push('/')
      })
    }
  }
};
</script>

<style>
.login-box {
  margin-top: 150px;
  padding: 0 100px 0 100px;
}

.form-input {
  margin-top: 20px;
}

input.vs-input {
  width: 100% !important;
}

.login-container {
  width: 800px; 
  margin: 0 auto;
}

.vs-input__message {
  margin-top: 5px;
}
</style>
