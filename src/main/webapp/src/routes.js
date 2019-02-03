import React from 'react';
import Loadable from 'react-loadable'

import DefaultLayout from './containers/DefaultLayout';

function Loading() {
  return <div>Loading...</div>;
}

const UserList = Loadable({
  loader: () => import('./views/User/UserList'),
  loading: Loading,
});

const RawMaterialCategory = Loadable({
  loader: () => import('./views/RawMaterialCategory/RawMaterialCategory'),
  loading: Loading,
});

const GoodsCategory = Loadable({
  loader: () => import('./views/GoodsCategory/GoodsCategory'),
  loading: Loading,
});


const Goods = Loadable({
  loader: () => import('./views/Goods/Goods'),
  loading: Loading,
});

const GoodsRawMaterial = Loadable({
  loader: () => import('./views/GoodsRawMaterial/GoodsRawMaterial'),
  loading: Loading,
});


const RawMaterial = Loadable({
  loader: () => import('./views/RawMaterial/RawMaterial'),
  loading: Loading,
});


const GoodsListForOrder = Loadable({
  loader: () => import('./views/CreateOrder/GoodsListForOrder'),
  loading: Loading,
});

const CreateOrder = Loadable({
  loader: () => import('./views/CreateOrder/CreateOrder'),
  loading: Loading,
});

const MyOrderList = Loadable({
  loader: () => import('./views/MyOrderList/MyOrderList'),
  loading: Loading,
});

const AllOrderList = Loadable({
  loader: () => import('./views/AllOrderList/AllOrderList'),
  loading: Loading,
});

const OrderStateList = Loadable({
  loader: () => import('./views/OrderState/OrderState'),
  loading: Loading,
});

const NotificationList = Loadable({
  loader: () => import('./views/Notification/Notification'),
  loading: Loading,
});

const Dashboard = Loadable({
  loader: () => import('./views/Dashboard/Dashboard'),
  loading: Loading,
});

const Logout = Loadable({
  loader: () => import('./views/Logout/Logout'),
  loading: Loading,
});

const UnverifierList = Loadable({
  loader: () => import('./views/AllOrderList/NewOrdersList'),
  loading: Loading,
});

const EditOrder = Loadable({
  loader: () => import('./views/MyOrderList/EditOrder'),
  loading: Loading,
});

const PriceList = Loadable({
  loader: () => import('./views/PriceList/PriceList'),
  loading: Loading,
});

const PriceListDownload  = Loadable({
  loader: () => import('./views/PriceList/PriceListDl'),
  loading: Loading,
});


const routes = [
  { path: '/', exact: true, name: 'خانه', component: DefaultLayout , role: "USER" },
  { path: '/dashboard', name: 'داشبورد', component: Dashboard , role: "USER" },
  { path: '/logout', name: 'خروج', component: Logout },
  { path: '/dl-price-list', name: 'لیست قیمت', component: PriceListDownload },
  { path: '/user/list', name: 'لیست کاربران', component: UserList , role: "ADMIN"},
  { path: '/rawMaterialCategory/list', name: 'لیست نوع مواداولیه', component: RawMaterialCategory, role: "ADMIN"},
  { path: '/rawMaterial/list', name: 'لیست مواداولیه', component: RawMaterial, role: "ADMIN"},
  { path: '/goodsCategory/list', name: 'لیست دستبندی کالا', component: GoodsCategory, role: "ADMIN"},
  { path: '/goods/list', name: 'لیست کالا', component: Goods, role: "ADMIN"},
  { path: '/goodsRawMaterial/list', name: 'لیست مواد کالا', component: GoodsRawMaterial, role: "ADMIN"},
  { path: '/create-order-list', name: 'ثبت درخواست', component: GoodsListForOrder, role: "USER"},
  { path: '/create-order/:id', name: 'ثبت درخواست', component: CreateOrder, role: "USER"},
  { path: '/edit-order/:id', name: 'ویرایش درخواست', component: EditOrder, role: "USER"},
  { path: '/my-orders', name: 'لیست درخواست ها', component: MyOrderList, role: "USER"},
  { path: '/unverifier-orders', name: 'سفارشات تایید نشده', component: UnverifierList, role: "VERIFIER"},
  { path: '/all-orders', name: 'لیست تمام درخواست ها', component: AllOrderList, role: "ADMIN"},
  { path: '/orderState', name: 'لیست مراحل', component: OrderStateList, role: "ADMIN"},
  { path: '/notifications', name: 'لیست اعلامیه ها', component: NotificationList, role: "ADMIN"},
  { path: '/price-list/list', name: 'لیست قیمت ها', component: PriceList, role: "ADMIN"}
];

export default routes;
