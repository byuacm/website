var app = angular.module('acm');

app.factory('profileFactory', ['$http', function($http) {
	var o = {
		profile: {}
	};
	o.getProfile = function() {
		return $http.get('/profile').success(function(data) {
			angular.copy(data, o.profile);
		});
	};
	o.editProfile = function() {
		return $http.post('/profile/edit', o.profile).success(function(data) {

		});
	};
	return o;
}]);

app.controller('ProfileCtrl', [
	'$scope',
	'profileFactory',
	function($scope, profileFactory) {
		profileFactory.getProfile();
		$scope.profile = profileFactory.profile;
		$scope.editProfile = profileFactory.editProfile;
	}
]);