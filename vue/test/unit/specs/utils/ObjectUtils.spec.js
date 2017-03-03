import * as utils from "src/utility/objectUtils";

describe('objectUtils.js', () => {
  it('should map objects by id', () => {
    const items = [
      {id: 1, name: 'item 1'},
      {id: 2, name: 'item 2'},
      {id: 3, name: 'item 3'}
    ];

    expect(utils.toMap(items)).to.eql({
      1: {id: 1, name: 'item 1'},
      2: {id: 2, name: 'item 2'},
      3: {id: 3, name: 'item 3'}
    })
  });

  it('should copy object data', () => {
    const data = {
      a: 'a',
      b: 'b',
      c: 'c'
    };
    const newDate = utils.copyData(data);

    expect(newDate).to.eql(data);

    data.b = 'a';

    expect(newDate).to.not.eql(data);
  });

  it('should copy array data', () => {
    const data = [1, 2, 3];
    const newDate = utils.copyData(data);

    expect(newDate).to.eql(data);

    data.push(4);

    expect(newDate).to.not.eql(data);
  });
});
