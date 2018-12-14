import React, {Component} from 'react';
import RestProvider from '../Core/RestProvider';
import Axios from 'axios';


export default class Logout extends Component {


    componentDidMount() {
        Axios.get("/logout", { withCredentials: true })
            .then( resp => window.location.assign("/login") );
    }



	render() {
        return( <div></div> )
    }

}
