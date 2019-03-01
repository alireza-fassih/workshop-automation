const formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0
});

export default {
    format : function( val ) {
        return formatter.format(val).substring(1);
    } , 
    parse : function( val ) {
        return parseInt( val.replace(",", "") );
    }
};