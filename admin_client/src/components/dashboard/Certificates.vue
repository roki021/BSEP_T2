<template>
    <vs-row>
      <vs-col offset="2" w="8">
        <div class="container">
            <vs-input type="password" v-model="password" placeholder="Password">
                <template #icon>
                    <b>PK</b>
                </template>
                </vs-input>
        </div>
      </vs-col>
    </vs-row>
</template>
<script>
export default {
    
}
</script>
<style scoped>

.container {
    padding: 20px;
    border: 1px solid transparent;
    background: #1f253e;
    border-radius: 8px;
}

</style>