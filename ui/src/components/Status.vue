<template>
  <div>
    <h1>Status</h1>
    <hr />
    <div>{{ publicKeyAddress }}</div>
  </div>
</template>
<script>
  export default {
    name: 'Status',
    mounted () {
      this.publicKeyAddress = location.href
    },
    data () {
      return {
        publicKeyAddress: ''
      }
    }
  }
</script>
