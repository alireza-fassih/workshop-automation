export default {
	items: [
		{
			name: 'لیست کاربران',
			icon: 'fa fa-user',
			url: '/user/list',
			role: "ADMIN"
		},
		{
			name: 'اطلاعات پایه',
			icon: 'fa fa-code',
			role: "ADMIN",
			children: [
				{
					name: 'لیست نوع مواداولیه',
					url: '/rawMaterialCategory/list',
					icon: 'icon-puzzle',
					role: "ADMIN"
				},
				{
					name: 'لیست مواداولیه',
					url: '/rawMaterial/list',
					icon: 'icon-puzzle',
					role: "ADMIN"
				},
				{
					name: 'لیست دستبندی کالا',
					url: '/goodsCategory/list',
					icon: 'icon-puzzle',
					role: "ADMIN"
				},
				{
					name: 'لیست کالا',
					url: '/goods/list',
					icon: 'icon-puzzle',
					role: "ADMIN"
				},
				{
					name: 'لیست مراحل',
					url: '/orderState/list',
					icon: 'fas fa-angle-double-left',
					role: "ADMIN"
				},
				{
					name: 'لیست مواد کالا',
					url: '/goodsRawMaterial/list',
					icon: 'icon-puzzle',
					role: "ADMIN"
				},
				{
					name: 'لیست قیمت ها',
					url: '/price-list/list',
					icon: 'icon-puzzle',
					role: "ADMIN"
				},

			]
		},
		{
			name: 'لیست اطلاعیه ها',
			url: '/notifications',
			icon: 'fas fa-bullhorn',
			role: "ADMIN"
		},
		{
			name: 'ثبت درخواست',
			icon: 'fas fa-clipboard-check',
			url: '/create-order-list',
			role: "USER"
		},
		{
			name: 'لیست درخواست ها',
			icon: 'fas fa-clipboard-list',
			url: '/my-orders',
			role: "USER"
		},
		{
			name: 'سفارشات',
			icon: 'fas fa-calendar-times',
			url: '/unverifier-orders',
			role: "VERIFIER"
		},
		{
			name: 'لیست تمام درخواست ها',
			icon: 'fas fa-cubes',
			url: '/all-orders',
			role: "ADMIN"
		},
		{
			name: 'لیست قیمت',
			icon: 'icon-puzzle',
			url: '/dl-price-list'
		},
		{
			name: 'خروج',
			icon: 'fas fa-sign-out-alt',
			url: '/logout'
		}
	],
};
