angular.module('acm', ['ui.router', 'ngCookies'])
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
  '$window',
  function($scope, $state, $window, $cookies){
	function checkCookie() {
		console.log("Here");
		console.log($cookies);
		return false;
	}
	  
	$scope.loggedIn = checkCookie(); 
	$scope.isActive = function(state) {
		return state === $state.current.name;
	};
	$scope.isLoggedIn = function() {
		return $scope.loggedIn;
	};
	$scope.logIn = function() {
		// check for cookie first
		var window = $window.open(
			'https://cas.byu.edu/cas/login?service=http://localhost:9000/login/cas',
			'targetWindow',
			'toolbar=no,' +
			'location=no,' +
			'status=no,' +
			'menubar=no,' +
			'scrollbars=yes,' +
			'resizeable=yes,' +
			'width=600,' +
			'height=650');
			
		window.onbeforeunload = function(event) {
			
		};
		console.log(window);
		$scope.loggedIn = true;
	};
	$scope.register = function() {
		// check for cookie
		var window = $window.open(
			'https://cas.byu.edu/cas/login?service=http://localhost:9000/register/cas',
			'targetWindow',
			'toolbar=no,' +
			'location=no,' +
			'status=no,' +
			'menubar=no,' +
			'scrollbars=yes,' +
			'resizeable=yes,' +
			'width=600,' +
			'height=650');
			
		window.onbeforeunload = function(event) {
			
		};
		$scope.loggedIn = true;
	};
	$scope.checkIn = function() {
		// Check in to the current meeting - only if there is one
	};
	$scope.logOut = function() {
		// check for NO cookie
		var window = $window.open('https://cas.byu.edu/cas/logout');
		
		window.onbeforeunload = function(event) {
			
		};
		$scope.loggedIn = false;
	};
  }
]);