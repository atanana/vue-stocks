<template>
  <SimplePage
    :items="packs"
    @saveItems="save"
    newItemPlaceholder="Название упаковки"
    newItemLabel="Добавить упаковку"/>
</template>

<script>
  import SimplePage from "components/SimplePage";

  export default {
    components: {SimplePage},
    computed: {
      packs() {
        return this.$store.state.packs;
      }
    },
    created() {
      this.$store.dispatch('loadPacks');
    },
    methods: {
      save(newPacks) {
        this.$store.dispatch('updatePacks', newPacks);
      }
    }
  }
</script>
