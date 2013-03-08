'use strict';

afterwords.controller('RootCtrl', function($scope, $window, $http){
  $scope.updateLang = function(language) {
    $.i18n.setLng(language, function(){
//      $scope.$apply();
      $window.location.reload(); //this event should be rare so a reload is ok??
    });
  }

  $scope.login = {};
  $scope.login.user = null;

  $scope.login.connect = function() {
    var config = {"withCredentials": true};
    $http.get('http://localhost:9000/api/users/' + $scope.login.login, config).success(function(data, status) {
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
});

