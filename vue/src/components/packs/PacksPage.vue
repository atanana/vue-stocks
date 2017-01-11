<template>
  <div>
    <SimpleList
      :items="packs"
      newItemPlaceholder="Название упаковки"
      newItemLabel="Добавить упаковку"
    />
    <SaveButton @save="save"/>
  </div>
</template>

<script>
  import SimpleList from 'components/SimpleList'
  import SaveButton from 'components/buttons/SaveButton'

  export default {
    name: 'app',
    components: {
      SimpleList,
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
