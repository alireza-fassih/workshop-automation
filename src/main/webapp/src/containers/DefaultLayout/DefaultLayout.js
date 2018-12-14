import React, { Component } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { Container } from 'reactstrap';

import {
  AppBreadcrumb,
  AppFooter,
  AppHeader,
  AppSidebar,
  AppSidebarHeader,
  AppSidebarNav,
} from '@coreui/react';

import navigation from '../../_nav';


import routes from '../../routes';
import DefaultFooter from './DefaultFooter';
import DefaultHeader from './DefaultHeader';
import RestProvider from '../../views/Core/RestProvider';

class DefaultLayout extends Component {

  constructor(props) {
    super(props)
    this.state = {};
    this.rest = new RestProvider("customer");

    this.getNavigationBasedOnRoles = this.getNavigationBasedOnRoles.bind(this);
  }

  componentDidMount() {
    this.rest.getCustom("roles")
      .then( resp => this.setState({user: resp.data}));
  }

  getNavigationBasedOnRoles(items) {
    let roles = ( this.state.user ? this.state.user.roles : [] );
    let newItems = [];
    items.forEach(element => {
      if( (!element.role) || roles.includes(element.role)) {
        if( element.children ) {
          element.children = this.getNavigationBasedOnRoles(element.children);
          newItems.push( element );
        } else {
          newItems.push( element );
        }
      }
    });
    return newItems;
  }

  render() {
    let roles = ( this.state.user ? this.state.user.roles : [] );
    let nav = { items: this.getNavigationBasedOnRoles(navigation.items) };

    return (
      <div className="app">
        <AppHeader fixed>
          <DefaultHeader />
        </AppHeader>
        <div className="app-body">
          <div className="rtl-dir">
            <AppSidebar fixed display="lg">
              <AppSidebarHeader />
              <AppSidebarNav navConfig={nav} {...this.props} />
            </AppSidebar>
          </div>
          <main className="main">
            <AppBreadcrumb appRoutes={routes}/>
            <Container fluid>
              <Switch>
                {routes.map((route, idx) => {
                    return (route.component && ( !route.role || roles.includes(route.role) ) )? (<Route key={idx} path={route.path} exact={route.exact} name={route.name} render={props => (
                        <route.component {...props} />
                      )} />)
                      : (null);
                  },
                )}
                <Redirect from="/" to="/dashboard" />
              </Switch>
            </Container>
          </main>
        </div>
        <AppFooter>
          <DefaultFooter />
        </AppFooter>
      </div>
    );
  }
}

export default DefaultLayout;
