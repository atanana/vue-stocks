<template>
  <div>
    <FloatingButton @addNew="showAddProductPopup = true"/>
    <Popup v-if="showAddProductPopup" @okPressed="addProduct" @close="showAddProductPopup = false"
           buttonLabel="Добавить">
      <div slot="header">
        <h2>Добавьте продукт</h2>
      </div>

      <div slot="body" class="control">
        <label class="label">Категория</label>
        <span class="select">
          <!--suppress HtmlFormInputWithoutLabel -->
          <select v-model="categoryId" class="form-control">
            <option
              v-for="category in availableCategories"
              :value="category.id"
            >{{category.name}}
            </option>
          </select>
        </span>

        <label class="label">Тип</label>
        <span class="select">
          <!--suppress HtmlFormInputWithoutLabel -->
          <select v-model="productTypeId" class="form-control">
            <option
              v-for="productType in availableProductTypes"
              :value="productType.id"
            >{{productType.name}}
            </option>
          </select>
        </span>

        <label class="label">Упаковка</label>
        <span class="select">
          <!--suppress HtmlFormInputWithoutLabel -->
          <select v-model="packId" class="form-control">
            <option
              v-for="pack in packs"
              :value="pack.id"
            >{{pack.name}}
            </option>
          </select>
        </span>
      </div>
    </Popup>
  </div>
</template>

<script>
  import Popup from 'components/Popup';
  import AddNewButton from 'components/buttons/AddNewButton';
  import FloatingButton from 'components/buttons/FloatingButton';

  export default {
    props: ['categories', 'productTypes', 'packs'],
    components: {
      Popup,
      AddNewButton,
      FloatingButton
    },
    data() {
      return {
        showAddProductPopup: false,
        categoryId: 0,
        productTypeId: 0,
        packId: 0
      }
    },
    computed: {
      availableCategories() {
        return [{name: 'Без категории'}].concat(this.categories);
      },
      availableProductTypes() {
        if (this.categoryId) {
          return this.productTypes.filter(productType => productType.categoryId === this.categoryId || !productType.categoryId);
        } else {
          return this.productTypes;
        }
      }
    },
    methods: {
      addProduct() {
        this.showAddProductPopup = false;
        this.$store.dispatch('addProduct', {
          categoryId: this.categoryId,
          productTypeId: this.productTypeId,
          packId: this.packId
        });

        this.categoryId = null;
        this.productTypeId = null;
        this.packId = null;
      }
    },
    watch: {
      productTypeId() {
        for (let productType of this.productTypes) {
          if (productType.id === this.productTypeId && productType.categoryId) {
            this.categoryId = productType.categoryId;
            break;
          }
        }
      }
    }
  }
</script>
