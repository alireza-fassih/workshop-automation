
import currency from './currency'



it('convert value to currency', () => {
    let val = currency.format(2000);
    expect(val).toEqual('2,000');
})


it('tousends camma', () => {
    let val = currency.format(2000000000);
    expect(val).toEqual('2,000,000,000');
})


it('low carrency', () => {
    let val = currency.format(200);
    expect(val).toEqual('200');
})


it('low carrency with floating point', () => {
    let val = currency.format(2000.1);
    expect(val).toEqual('2,000.1');
})

it('convert string value to currency', () => {
    let val = currency.format('2000');
    expect(val).toEqual('2,000');
})

it('convert to int', () => {
    let val = currency.parse("2,000");
    expect(val).toEqual(2000);
})