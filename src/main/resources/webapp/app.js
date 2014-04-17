var fApp = angular.module('fApp', [
		'ngRoute',
		'fControllers'
	]);

fApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider
			.when('/', {
				templateUrl: 'partials/index.html',
				controller: 'StartController'
			})

			.when('/brews/:id', {
				templateUrl: 'partials/brewDetails.html',
				controller: 'BrewController'
			})

			.when('/brews', {
				templateUrl: 'partials/brews.html',
				controller: 'ListBrewsController'
			})

			.otherwise({
				redirectTo: '/'
			});
	}]);
