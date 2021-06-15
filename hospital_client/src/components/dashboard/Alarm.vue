<template>
  <div>
    <vs-row>
      <vs-col offset="3" w="6">
        <div class="container">
          <vs-table>
            <template #thead>
              <vs-tr>
                <vs-th> Name </vs-th>
                <vs-th> Message format </vs-th>
                <vs-th> Notification </vs-th>
              </vs-tr>
            </template>
            <template #tbody>
              <vs-tr :key="i" v-for="(tr, i) in alarms" :data="tr">
                <vs-td>
                  {{ tr.name }}
                </vs-td>
                <vs-td>
                  {{ tr.alarmMessage }}
                </vs-td>
                <vs-td>
                  <vs-button transparent><i class="bx bxs-bell"></i></vs-button>
                </vs-td>
              </vs-tr>
            </template>
          </vs-table>
          <vs-col offset="10">
            <vs-button
              transparent
              @click="activateAdding()"
              >Add new</vs-button
            >
          </vs-col>
        </div>
      </vs-col>
    </vs-row>
    <vs-dialog v-model="addingActive" @close="handleCloseAdding" width="600px">
      <template #header>
        <h4 class="not-margin">Alarm addition form</h4>
      </template>

      <div class="con-form">
        <div class="grid horizontal-pos">
          <vs-row>
            <vs-col w="12">
              <vs-input
                label="Name"
                v-model="newAlarm.name"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px">
              <vs-input
                label="Alarm message"
                v-model="newAlarm.alarmMessage"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px; padding-bottom: 10px">
              <vs-input
                label="Activation threshhold"
                v-model="newAlarm.activationThreshold"
                style="padding: 0 10px 0 0"
                type="number"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px; padding-bottom: 10px">
              <vs-input
                label="Message tracker"
                v-model="newAlarm.messageTracker"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px; padding-bottom: 10px">
              <vs-input
                label="Fact wait"
                v-model="newAlarm.factWait"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
        </div>
        <div class="grid horizontal-pos" style="padding-bottom: ">
          <vs-row>
            <vs-col w="12">
              <JqxListBox :width="'100%'" :height="100" ref="myListBox">
              </JqxListBox>
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px">
              <vs-input
                label="Trigger field"
                v-model="newTrigger.field"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="12" style="padding-top: 20px; padding-bottom: 10px">
              <vs-input
                label="Trigger value"
                v-model="newTrigger.value"
                style="padding: 0 10px 0 0"
              />
            </vs-col>
          </vs-row>
          <vs-row>
            <vs-col w="3" style="padding-top: 20px; padding-bottom: 10px">
              <vs-radio v-model="newTrigger.relation" val="0"> &lt; </vs-radio>
            </vs-col>
            <vs-col w="3" style="padding-top: 20px; padding-bottom: 10px">
              <vs-radio v-model="newTrigger.relation" val="1"> &gt; </vs-radio>
            </vs-col>
            <vs-col w="3" style="padding-top: 20px; padding-bottom: 10px">
              <vs-radio v-model="newTrigger.relation" val="2"> == </vs-radio>
            </vs-col>
            <vs-col w="3" style="padding-top: 20px; padding-bottom: 10px">
              <vs-radio v-model="newTrigger.relation" val="3"> in </vs-radio>
              <br />
            </vs-col>
          </vs-row>
          <div>
            <vs-button
              style="display: inline-block; float: left"
              v-on:click="addTrigger()"
            >
              Add trigger
            </vs-button>
            <vs-button
              style="display: inline-block; float: right"
              v-on:click="clearTriggers()"
            >
              Clear triggers
            </vs-button>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="footer-dialog">
          <vs-button block v-on:click="addNewAlarm()">
            Add new alarm
          </vs-button>
        </div>
      </template>
    </vs-dialog>
  </div>
</template>
<script>
import JqxListBox from "jqwidgets-scripts/jqwidgets-vue/vue_jqxlistbox.vue";

export default {
  components: {
    JqxListBox,
  },
  data: () => ({
    alarms: [],
    newAlarm: {
      name: "",
      alarmMessage: "",
      activationThreshold: null,
      messageTracker: "",
      triggers: {},
      factWait: null,
    },
    newTrigger: {
      value: "",
      relation: null,
      field: "",
    },
    addingActive: false,
  }),
  methods: {
    activateAdding() {
      this.addingActive = true;
    },
    handleCloseAdding() {
      this.addingActive = false;
      this.newAlarm = {
        name: "",
        alarmMessage: "",
        activationThreshold: null,
        messageTracker: "",
        triggers: [],
        factWait: null,
      };
      this.newTrigger = {
        value: "",
        relation: null,
        field: "",
      };
    },
    addNewAlarm() {
      
      if(!this.newAlarm.factWait) {
        this.newAlarm.factWait = 1;
      }
      if(!this.newAlarm.activationThreshold) {
        this.newAlarm.activationThreshold = 1;
      }
      console.log(this.newAlarm);
      this.$store
        .dispatch("addNewAlarm", this.newAlarm)
        .then(() => {
          this.$vs.notification({
            color: "success",
            title: "Request sent",
            text: "Alarm added successfully.",
          });
          this.$store.dispatch("getAlarms").then(() => {
            this.alarms = this.$store.state.alarms;
          });
        })
        .catch((err) => {
          if (err.response.status === 403)
            this.$vs.notification({
              color: "danger",
              title: "Forbidden",
              text: "Your account doesn't have appropriate privilege",
            });
          else
            this.$vs.notification({
              color: "danger",
              title: "Something went wrong",
              text: "Already added alarm.",
            });
        });
      this.handleCloseAdding();
    },
    addTrigger() {
      this.newAlarm.triggers[this.newTrigger.field] = {
        value: this.newTrigger.value,
        relation: parseInt(this.newTrigger.relation, 10)
      };
      this.$refs.myListBox.addItem(this.newTrigger.field);
      this.newTrigger = {
        value: "",
        relation: null,
        field: "",
      };
    },
    clearTriggers() {
      this.$refs.myListBox.clear();
      this.newAlarm.triggers = {};
    },
  },
  mounted() {
    this.$store
      .dispatch("getAlarms")
      .then(() => {
        this.alarms = this.$store.state.alarms;
      })
      .catch((err) => {
        if (err.response.status === 403)
          this.$vs.notification({
            color: "danger",
            title: "Forbidden",
            text: "Your account doesn't have appropriate privilege",
          });
        else
          this.$vs.notification({
            color: "danger",
            title: "Something went wrong",
            text: "Unknown error",
          });
      });
  },
};
</script>
<style scoped>
.horizontal-pos {
  display: inline-block;
  width: 50%;
}
</style>