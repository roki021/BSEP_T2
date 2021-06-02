<template>
  <div>
    <vs-row>
      <vs-col offset="2" w="8">
        <div class="container">
          <vs-table>
            <template #thead>
              <vs-tr>
                <vs-th> # </vs-th>
                <vs-th> Organization </vs-th>
                <vs-th> Organization unit </vs-th>
                <vs-th> &nbsp; </vs-th>
              </vs-tr>
            </template>
            <template #tbody>
              <vs-tr :key="i" v-for="(tr, i) in hospitals" :data="tr">
                <vs-td>
                  {{ i + 1 }}
                </vs-td>
                <vs-td>
                  {{ tr.organization }}
                </vs-td>
                <vs-td>
                  {{ tr.organizationUnit }}
                </vs-td>
                <vs-td>
                  <vs-button transparent v-on:click="$router.push('/hospitals/' + tr.id)"
                    >Explore</vs-button
                  >
                </vs-td>
              </vs-tr>
            </template>
          </vs-table>
        </div>
      </vs-col>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data: () => ({
    hospitals: []
  }),
  mounted() {
    axios
      .get(`${process.env.VUE_APP_ADMIN_API}/hospitals`)
      .then((response) => {
        this.hospitals = response.data
      })
  }
}
</script>

<style>

</style>