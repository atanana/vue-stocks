<template>
  <div>
    <PacksList
      :packs="packs"
    />
    <SaveButton @save="save"/>
  </div>
</template>

<script>
  import PacksList from 'components/packs/PacksList'
  import SaveButton from 'components/buttons/SaveButton'

  export default {
    name: 'app',
    components: {
      PacksList,
      SaveButton
    },
    computed: {
      packs() {
        return this.$store.state.packs;
      }
    },
    created() {
      this.$store.dispatch('loadPacks');
    },
    methods: {
      save() {
        this.$store.dispatch('updatePacks', this.packs);
      }
    }
  }
</script>
