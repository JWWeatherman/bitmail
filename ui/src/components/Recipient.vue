<template>
    <div>
        <h1>Recipient</h1>
        <hr />
        <div>{{ publicKeyAddress }}</div>
    </div>
</template>
<script>
    export default {
      name: 'Recipient',
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
