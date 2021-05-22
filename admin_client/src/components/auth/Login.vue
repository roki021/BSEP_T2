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
              <template v-if="invalidUsernameFlag" #message-danger>
                Username Invalid
              </template>
            </vs-input>
          </div>
          <div class="form-input">
            <vs-input type="password" v-model="password" placeholder="Password">
              <template #icon>
                <i class="bx bx-lock-open-alt"></i>
              </template>
              <template v-if="invalidPasswordFlag" #message-danger>
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
    usernameEdit: false,
    passwordEdit: false
  }),
  computed: {
    invalidUsernameFlag: function () {
      return this.usernameEdit && this.username.length == 0
    },
    invalidPasswordFlag: function () {
      return this.passwordEdit && this.password.length < 8
    },
  },
  methods: {
    login() {
      this.usernameEdit = true
      this.passwordEdit = true

      if (this.invalidUsernameFlag || this.invalidPasswordFlag)
        return

      this.$store.dispatch('requestAuth', {
        username: this.username,
        password: this.password
      }).then(() => {
        this.$router.push('/')
      }).catch(() => {
        this.$vs.notification({
          color: 'danger',
          title: 'Login failed',
          text: 'Invalid username or password.',
        })
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
</style>
