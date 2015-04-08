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
	$scope.isActive = function(state) {
		return state === $state.current.name;
	};
  }
]);