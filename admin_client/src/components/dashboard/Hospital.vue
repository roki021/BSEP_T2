<template>
  <div>
    <vs-row>
      <vs-col offset="2" w="8">
          <div class="container">
            <h1>{{ organization }}</h1>
            <br />
            <vs-button style="float: right; margin-bottom: 10px;" v-on:click="activeNew = true"
              >
                Create new member
            </vs-button>
            <vs-table>
              <template #thead>
                <vs-tr>
                  <vs-th> # </vs-th>
                  <vs-th> First name </vs-th>
                  <vs-th> Last name </vs-th>
                  <vs-th> Username </vs-th>
                  <vs-th> Role </vs-th>
                  <vs-th> &nbsp; </vs-th>
                </vs-tr>
              </template>
              <template #tbody>
                <vs-tr :key="i" v-for="(tr, i) in hospitalMembers" :data="tr">
                  <vs-td>
                    {{ i + 1 }}
                  </vs-td>
                  <vs-td>
                    {{ tr.firstName }}
                  </vs-td>
                  <vs-td>
                    {{ tr.lastName }}
                  </vs-td>
                  <vs-td>
                    {{ tr.username }}
                  </vs-td>
                  <vs-td>
                    {{ tr.role }}
                  </vs-td>
                  <vs-td>
                    <vs-button transparent
                      >Details</vs-button
                    >
                  </vs-td>
                </vs-tr>
              </template>
            </vs-table>
          </div>
      </vs-col>
    </vs-row>
    <vs-dialog v-model="activeNew">
      <template #header>
        <h4 class="not-margin">Create new hospital member</h4>
      </template>

      <div class="con-form">
        <div class="center grid">
          <vs-row>
            <vs-col w="6"><div class="wrapper"><b>First name</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.firstName" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="6"><div class="wrapper"><b>Last name</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.lastName" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="6"><div class="wrapper"><b>Username</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.username" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="6"><div class="wrapper"><b>Email</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.email" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="6"><div class="wrapper"><b>Role</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper">
                <vs-select placeholder="Select" v-model="hospitalMember.role">
                  <vs-option label="DOCTOR" value="doctor">
                    DOCTOR
                  </vs-option>
                  <vs-option label="ADMIN" value="admin">
                    ADMIN
                  </vs-option>
                </vs-select>
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="6"><div class="wrapper"><b>Password</b></div></vs-col>
            <vs-col w="6">
              <div class="wrapper">
                <vs-input type="password" v-model="hospitalMember.password" />
              </div>
            </vs-col>
          </vs-row>
        </div>
      </div>
      <template #footer>
        <div class="footer-dialog">
          <vs-button :loading="waitingResponse" block v-on:click="createMember()"> Create </vs-button>
        </div>
      </template>
    </vs-dialog>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data: () => ({
    organization: 'Organization name',
    hospitalMembers: [],
    hospitalId: null,
    activeNew: false,
    role: 'doctor',
    waitingResponse: false,
    hospitalMember: {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      role: 'doctor',
      password: ''
    }
  }),
  mounted() {
    // console.log('fetch data hospital ' + this.$route.params.id)
    /*axios
      .get(`${process.env.VUE_APP_ADMIN_API}/hospitals`)
      .then((response) => {
        this.hospitalMembers = response.data
      })*/
      this.getMembers()
  },
  methods: {
    createMember() {
      this.waitingResponse = true
      axios
        .post(`${process.env.VUE_APP_ADMIN_API}/hospitals/${this.$route.params.id}/members`, this.hospitalMember)
        .then(() => {
          this.waitingResponse = false
          this.activeNew = false
          this.hospitalMembers.firstName = ''
          this.hospitalMembers.lastName = ''
          this.hospitalMembers.email = ''
          this.hospitalMembers.username = ''
          this.hospitalMembers.role = 'doctor'
          this.hospitalMembers.password = ''
          this.getMembers()
        })
    },
    getMembers() {
      axios
        .get(`${process.env.VUE_APP_ADMIN_API}/hospitals/${this.$route.params.id}`)
        .then((response) => {
          this.hospitalMembers = response.data
        })
    }
  }
}
</script>

<style>

</style>