<template>
  <div class="login-container">
    <vs-row>
      <vs-col offset="2" w="8">
        <div class="content-inputs login-box">
          <div class="form-input">
            <vs-input v-model="username" placeholder="User name">
              <template #icon>
                <i class="bx bx-user"></i>
              </template>
            </vs-input>
          </div>
          <div class="form-input">
            <vs-input type="password" v-model="password" placeholder="Password">
              <template #icon>
                <i class="bx bx-lock-open-alt"></i>
              </template>
            </vs-input>
          </div>
          <div class="form-input">
                <vs-button style="width: 100%">
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
    username: "",
    password: "",
  }),
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
