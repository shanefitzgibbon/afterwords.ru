'use strict';

afterwords..controller('LoginCtrl', function($scope, $http) {
  $scope.login = {};
  $scope.login.user = null;

  $scope.login.connect = function() {
    $http.get('http://localhost\\:9000/api/users/' + $scope.login.login).success(function(data, status) {
      // despite its name, this callback is triggered even in case of an error, so we have to take care
      if (status < 200 || status >= 300)
        return;
      $scope.login.user = data;
    });
  };

  $scope.login.disconnect = function() {
    $scope.login.user = null;
  };

  $scope.$watch('login.login + login.password', function() {
    $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.login.login + ':' + $scope.login.password);
  });
};