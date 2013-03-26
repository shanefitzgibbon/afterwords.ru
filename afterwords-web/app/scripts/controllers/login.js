'use strict';

afterwords.controller('LoginCtrl', function($scope, $rootScope, $http, dialog, userService, $log) {

  $scope.connect = function() {
    userService.connect(
      function(data, status) {
          $rootScope.login.user = data;
          dialog.close();
      },
      function(data, status) {
          $log.error("Could not complete login request.");
          $log.info("Failed Login. status: " + status + " data: " + data);
          dialog.close();
      }
    );
  };

  $scope.cancel = function() {
    dialog.close();
    $rootScope.login.alerts = [];
  }

  $scope.switch = function() {
    dialog.close();
    $rootScope.login.alerts = [];
    $rootScope.openRegistrationDialog();
  }

});