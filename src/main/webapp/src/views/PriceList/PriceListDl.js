import React, {Component} from 'react';


export default class PriceListDl extends Component {

  constructor(props) {
    super(props)
  }

  componentDidMount() {
    window.open('/rest/customer/price-list');
  }



	render() {
        return( <div></div> )
    }

}
