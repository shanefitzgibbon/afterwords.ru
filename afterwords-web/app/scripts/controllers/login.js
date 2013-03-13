'use strict';

afterwords.controller('LoginCtrl', function($scope, $rootScope, $http, dialog, authService, $log) {
  $rootScope.login = {};
  $rootScope.login.email = '';
  $rootScope.login.password = '';
  $rootScope.login.user = null;

  $rootScope.login.connect = function() {
    var config = {"withCredentials": true};
    $http.get('http://localhost:9000/api/users/' + $rootScope.login.email, config).success(function(data, status) {
      // despite its name, this callback is triggered even in case of an error, so we have to take care
      if (status < 200 || status >= 300){
        dialog.close();
        $log.error("Error getting user from server. Server status code: " + status);
        return;
      }
      $rootScope.login.user = data;
      authService.loginConfirmed();
      dialog.close();
    });
  };

  $rootScope.login.cancel = function() {
    dialog.close();
  }

  $rootScope.login.disconnect = function() {
    $rootScope.login.email = '';
    $rootScope.login.password = '';
    $rootScope.login = {};
    $rootScope.login.user = null;
  };

  $rootScope.$watch('login.email + login.password', function() {
    if ($rootScope.login.email && $rootScope.login.password){
      $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($rootScope.login.email + ':' + $rootScope.login.password);
    }
    else {
      delete $http.defaults.headers.common['Authorization'];
    }
  });
});