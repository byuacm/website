angular.module('acm', ['ui.router'])
.config([
	'$stateProvider',
	'$urlRouterProvider',
	function($stateProvider, $urlRouterProvider) {
		$stateProvider.state('home', {
			url: '/',
			templateUrl: '/html/home.html',
			//controller: 'HomeCtrl'
		})
		.state('challenges', {
			url: '/challenges',
			templateUrl: '/html/challenges.html',
			//controller: 'ChallengesCtrl'
		})
		.state('partner', {
			url: '/partner',
			templateUrl: '/html/partner.html',
			//controller: 'PartnerCtrl'
		})
		.state('roster', {
			url: '/roster',
			templateUrl: '/html/roster.html',
			//controller: 'RosterCtrl'
		});
		$urlRouterProvider.otherwise('/');
	}
])
.controller('MainCtrl', [
  '$scope',
  '$state',
  function($scope, $state){
	var st = $state;
	$scope.state = st;
	$scope.isActive = function(state) {
		console.log("passed in: " + state + " | curr state: " + st.current.name);
		return state === st.current.name;
	};
  }
]);