angular.module('acm', ['ui.router', 'ngCookies', 'ui.bootstrap'])
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
			controller: 'ChallengesCtrl'
		})
		.state('partner', {
			url: '/partner',
			templateUrl: '/html/partner.html',
			//controller: 'PartnerCtrl'
		})
		.state('roster', {
			url: '/roster',
			templateUrl: '/html/roster.html',
			controller: 'RosterCtrl'
		})
		.state('profile', {
			url: '/profile',
			templateUrl: '/html/profile.html',
			controller: 'ProfileCtrl'
		});
		$urlRouterProvider.otherwise('/');
	}
])
.controller('MainCtrl', [
	'$scope',
	'$state',
	'$window',
	'$cookieStore',
	'$http',
	function($scope, $state, $window, $cookieStore, $http){

	$scope.checkLoggedIn = function() {
  		$http.get('/profiles/getcurrent').success(function(data) {
  			$scope.isLoggedIn = true;
  		}).error(function(data) {
  			$scope.isLoggedIn = false;
  		});
  	};

  	$scope.testCookie = function() {
  		var interval = setInterval(function() {
			if ($cookieStore.get('loggedIn') === "true") {
				clearInterval(interval);
				$scope.isLoggedIn = true;
				$scope.$apply();
			}
		}, 500);
  	};

  	$scope.isLoggedIn = $scope.checkLoggedIn();

	$scope.isActive = function(state) {
		return state === $state.current.name;
	};

	$scope.logIn = function() {
		// check for cookie first
		var popup = $window.open(
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

		$scope.testCookie();
	};

	$scope.register = function() {
		// check for cookie
		var popup = $window.open(
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

		$scope.testCookie();
	};

	$scope.checkIn = function() {
		// Check in to the current meeting - only if there is one
	};

	$scope.logOut = function() {
		// check for NO cookie
		var window = $window.open(
			'https://cas.byu.edu/cas/logout',
			'targetWindow',
			'toolbar=no,' +
			'location=no,' +
			'status=no,' +
			'menubar=no,' +
			'scrollbars=yes,' +
			'resizeable=yes,' +
			'width=600,' +
			'height=650');

		setTimeout(function() {
			window.close();
		}, 200);

		$http.get('/logout/cas').success(function(data) {
  			$scope.checkLoggedIn();
  			$cookieStore.remove('loggedIn');
  		});
	};

	/* ------------- MODAL JAVASCRIPT ------------- */

	// Add HTML files for the modal to this array.
	$scope.modalHTMLFiles =
    ['html/profile.html',
    'html/email-list.html'];

    var profile = {
    	"username": "kpontius",
    	"firstName": "Kyle",
    	"lastName": "Pontius",
    	"email": "kylepontius@example.com"
    }

	$scope.viewProfile = function() {
		$scope.modalTitle = "Edit Your Profile";
		$scope.modalCancelText = "Cancel";
		$scope.modalSubmitText = "Update Profile";
		$scope.modalHTML = $scope.modalHTMLFiles[0];
		$scope.profile = profile;
	}

	$scope.emailSignup = function() {
		$scope.modalTitle = "Join ACM Mailing List";
		$scope.modalCancelText = "Cancel";
		$scope.modalSubmitText = "Submit";
		$scope.modalHTML = $scope.modalHTMLFiles[1];
	}
  }
]);