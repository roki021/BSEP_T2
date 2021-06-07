<template>
  <div>
    <vs-row>
      <vs-col offset="2" w="8">
          <div class="container">
            <h1>{{ organization }}</h1>
            <br />
            <vs-button style="float: right; margin-bottom: 10px;" v-on:click="showCreate()"
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
                    <vs-button transparent v-on:click="showDetails(i)"
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
            <vs-col w="5"><div class="wrapper"><b>First name</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.firstName" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Last name</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.lastName" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Username</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.username" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Email</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMember.email" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Password</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input type="password" v-model="hospitalMember.password" />
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Role</b></div></vs-col>
            <vs-col w="7">
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
            <vs-col w="5"><div class="wrapper"><b>Privileges</b></div></vs-col>
            <vs-col w="7">
              <template v-if="hospitalMember.role == 'admin'">
                <div class="wrapper" v-for="p in privileges.admin" :key="p.privilege">
                  <vs-switch v-model="p.value">
                    {{ p.privilege }}
                  </vs-switch>
                </div>
              </template>
              <template v-else>
                <div class="wrapper" v-for="p in privileges.doctor" :key="p.privilege">
                  <vs-switch v-model="p.value">
                    {{ p.privilege }}
                  </vs-switch>
                </div>
              </template>
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

    <vs-dialog v-model="activeEdit">
      <template #header>
        <h4 class="not-margin">Hospital member details</h4>
      </template>

      <div class="con-form">
        <div class="center grid">
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>First name</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMemberDetails.firstName" disabled="true"/>
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Last name</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMemberDetails.lastName" disabled="true"/>
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Username</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMemberDetails.username" disabled="true"/>
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Email</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-input v-model="hospitalMemberDetails.email" disabled="true"/>
              </div>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="5"><div class="wrapper"><b>Role</b></div></vs-col>
            <vs-col w="7">
              <div class="wrapper">
                <vs-select placeholder="Select" v-model="hospitalMemberDetails.role">
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
            <vs-col w="5"><div class="wrapper"><b>Privileges</b></div></vs-col>
            <vs-col w="7">
              <template v-if="hospitalMemberDetails.role == 'admin'">
                <div class="wrapper" v-for="p in privileges.admin" :key="p.privilege">
                  <vs-switch v-model="p.value">
                    {{ p.privilege }}
                  </vs-switch>
                </div>
              </template>
              <template v-else>
                <div class="wrapper" v-for="p in privileges.doctor" :key="p.privilege">
                  <vs-switch v-model="p.value">
                    {{ p.privilege }}
                  </vs-switch>
                </div>
              </template>
            </vs-col>
          </vs-row>
        </div>
      </div>
      <template #footer>
        <div class="footer-dialog">
          <vs-row>
            <vs-col offset="6" w="3">
              <vs-button style="margin-left: -5px" :loading="waitingDeleteResponse" danger block v-on:click="deleteMember()"> Delete </vs-button>
            </vs-col>
            <vs-col w="3">
              <vs-button :loading="waitingUpdateResponse" block v-on:click="updateMember()"> Update </vs-button>
            </vs-col>
          </vs-row>
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
    activeEdit: false,
    role: 'doctor',
    waitingResponse: false,
    waitingDeleteResponse: false,
    waitingUpdateResponse: false,
    hospitalMember: {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      role: 'doctor',
      password: ''
    },
    hospitalMemberDetails: {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      role: 'doctor',
    },
    selected: null,
    privileges: {
      admin: [
        {
          privilege: 'READ_DEVICES',
          value: false

        },
        {
          privilege: 'WRITE_DEVICES',
          value: false
        },
        {
          privilege: 'CREATE_CSR',
          value: false
        },
        {
          privilege: 'CHANGE_TOKEN_STATUS',
          value: false
        },
        {
          privilege: 'READ_TOKEN_STATUS',
          value: false
        }
      ],
      doctor: [
        {
          privilege: 'READ_DEVICES',
          value: false
        }
      ]
    }
  }),
  mounted() {
      this.getMembers()
  },
  methods: {
    showCreate() {
      this.activeNew = true

      for (let p of this.privileges.admin) 
        p.value = false
      for (let p of this.privileges.doctor)
        p.value = false

      
    },
    createMember() {
      this.waitingResponse = true
      let payload = {...this.hospitalMember}
      let privileges = []
      if (payload.role == 'admin')
        privileges = this.privileges.admin.filter(x => x.value).map(x => x.privilege + '_PRIVILEGE')
      else if (payload.role == 'doctor')
        privileges = this.privileges.doctor.filter(x => x.value).map(x => x.privilege + '_PRIVILEGE')
      payload.privileges = privileges

      axios
        .post(`${process.env.VUE_APP_ADMIN_API}/hospitals/${this.$route.params.id}/members`, payload)
        .then(() => {
          this.waitingResponse = false
          this.activeNew = false
          this.hospitalMember.firstName = ''
          this.hospitalMember.lastName = ''
          this.hospitalMember.email = ''
          this.hospitalMember.username = ''
          this.hospitalMember.role = 'doctor'
          this.hospitalMember.password = ''
          this.getMembers()
        })
        .catch(() => {
          this.waitingResponse = false
          this.$vs.notification({
              color: 'danger',
              position: null,
              title: 'Oups!',
              text: 'Something went wrong, please try again.'
          })
        })
    },
    getMembers() {
      axios
        .get(`${process.env.VUE_APP_ADMIN_API}/hospitals/${this.$route.params.id}`)
        .then((response) => {
          this.hospitalMembers = response.data
        })
        .catch(() => {
            this.$vs.notification({
                color: 'danger',
                position: null,
                title: 'Not permitted',
                text: 'Hospital admin should allow you to access hospital resources.'
            })
        })
    },
    deleteMember() {
      this.waitingDeleteResponse = true
      axios
        .delete(`${process.env.VUE_APP_ADMIN_API}/hospitals/${this.$route.params.id}/members/${this.selected.id}`)
        .then(() => {
          this.waitingDeleteResponse = false
          this.activeEdit = false
          this.selected = null
          this.getMembers()
        })
        .catch(() => {
          this.waitingDeleteResponse = false
          this.$vs.notification({
              color: 'danger',
              position: null,
              title: 'Oups!',
              text: 'Something went wrong, please try again.'
          })
        })
    },
    updateMember() {
      this.waitingUpdateResponse = true
      let privileges = []
      if (this.hospitalMemberDetails.role == 'admin')
        privileges = this.privileges.admin.filter(x => x.value).map(x => x.privilege + '_PRIVILEGE')
      else if (this.hospitalMemberDetails.role == 'doctor')
        privileges = this.privileges.doctor.filter(x => x.value).map(x => x.privilege + '_PRIVILEGE')

      axios
        .put(`${process.env.VUE_APP_ADMIN_API}/hospitals/${this.$route.params.id}/members/${this.selected.id}/roles`, { "role": this.hospitalMemberDetails.role, "privileges": privileges })
        .then(() => {
          this.waitingUpdateResponse = false
          this.activeEdit = false
          this.selected = null
          this.getMembers()
        })
        .catch(() => {
          this.waitingUpdateResponse = false
          this.$vs.notification({
              color: 'danger',
              position: null,
              title: 'Oups!',
              text: 'Something went wrong, please try again.'
          })
        })
    },
    showDetails(selected) {
      for (let p of this.privileges.admin) 
        p.value = false
      for (let p of this.privileges.doctor)
        p.value = false

      this.selected = this.hospitalMembers[selected]
      this.hospitalMemberDetails.firstName = this.selected.firstName
      this.hospitalMemberDetails.lastName = this.selected.lastName
      this.hospitalMemberDetails.email = this.selected.email
      this.hospitalMemberDetails.username = this.selected.username
      this.hospitalMemberDetails.role = this.selected.role

      let tmp = null
      if (this.selected.role == 'admin')
        tmp = this.privileges.admin
      else
        tmp = this.privileges.doctor

      for (let p of tmp)
        if (this.selected.privileges.filter(pr => p.privilege == pr.substr(0, pr.indexOf('_PRIVILEGE'))).length > 0)
          p.value = true
        else
          p.value = false

      this.activeEdit = true
    }
  }
}
</script>

<style>

</style>