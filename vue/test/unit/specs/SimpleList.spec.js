import Vue from "vue";
import SimpleList from "src/components/SimpleList";

// describe('SimpleList.vue', () => {
//   const newItemLabel = 'test new item';
//   const newItemPlaceholder = 'test new item placeholder';
//   const originalItems = [
//     {name: 'test 1'},
//     {name: 'test 2'},
//     {name: 'test 3'}
//   ];
//
//   let vm;
//
//   beforeEach(() => {
//     vm = createList();
//   });
//   function createList() {
//     const Ctor = Vue.extend(SimpleList);
//     return new Ctor({
//       propsData: {
//         newItemLabel: newItemLabel,
//         newItemPlaceholder: newItemPlaceholder,
//         items: originalItems.slice()
//       }
//     }).$mount();
//   }
//
//   it('should render correct items', () => {
//     const items = vm.$refs.items;
//
//     expect(items).to.have.lengthOf(3);
//
//     for (let i = 0; i < items.length; i++) {
//       checkItem(items[i], originalItems[i]);
//     }
//
//     function checkItem(item, originalItem) {
//       expect(item.item).to.eql(originalItem);
//       expect(item.placeholder).to.equal(newItemPlaceholder);
//       expect(item.$el).to.have.class('simple-item');
//     }
//   });
//
//   it('should render correct new item button', () => {
//     const newItemButton = vm.$refs.addNew;
//     expect(newItemButton.label).to.equal(newItemLabel);
//   });
//
//   it('should add new item', () => {
//     vm.$refs.addNew.$el.click();
//     expect(vm.items).to.have.lengthOf(4);
//   });
//
//   it('should delete items', () => {
//     vm.$refs.items[1].$refs.deleteButton.$el.click();
//     expect(vm.items).to.eql([originalItems[0], originalItems[2]]);
//   });
// });
