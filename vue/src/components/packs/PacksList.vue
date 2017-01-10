<template>
  <div>
    <PackItem
      v-for="pack in packs"
      :pack="pack"
      class="pack-item"
      @deletePack="deletePack(pack)"
    />

    <button type="button" class="btn btn-default new-pack-button" @click="addNewPack">
      <span class="glyphicon glyphicon-plus"></span>
      Добавить упаковку
    </button>
  </div>
</template>

<script>
  import PackItem from 'components/packs/PackItem'

  export default {
    props: ['packs'],
    components: {
      PackItem
    },
    methods: {
      addNewPack() {
        this.packs.push({});

        this.$nextTick(() => {
          const inputs = document.querySelectorAll('.pack-item input');
          inputs[inputs.length - 1].focus();
        });
      },
      deletePack(packs) {
        this.packs.splice(this.packs.indexOf(packs), 1);
      }
    }
  }
</script>

<style>
  .pack-item {
    margin-bottom: 1em;
  }

  .new-pack-button {
    margin-bottom: 1em;
  }
</style>
