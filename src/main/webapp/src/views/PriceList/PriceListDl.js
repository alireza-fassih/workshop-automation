import React, {Component} from 'react';
import RestProvider from '../Core/RestProvider';
import Axios from 'axios';


export default class PriceListDl extends Component {

  constructor(props) {
    super(props)
    this.rest = new RestProvider("customer");
  }

  componentDidMount() {
    this.rest.getCustom("price-list")
      .then(response => {
        let url = window.URL.createObjectURL(new Blob([response.data]));
        let link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'PricelList.pdf');
        document.body.appendChild(link);
        link.click();
      });
  }



	render() {
        return( <div></div> )
    }

}
