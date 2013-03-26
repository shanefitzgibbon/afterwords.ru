'use strict';

afterwords.controller('RegisterCtrl', function($scope, $rootScope, userService, dialog, $log) {
  $scope.registration = {};
  $scope.registration.firstName = '';
  $scope.registration.lastName = '';
  $scope.registration.email = '';
  $scope.registration.password = '';
  $scope.registration.confirm = '';

  $scope.register = function() {
    userService.register($scope.registration,
      function(data, status) { $rootScope.login.user = data; dialog.close(); },
      function(data, status) { dialog.close(); }
    );
  };

  $scope.cancel = function() {
    dialog.close();
  }

  $scope.switch = function() {
    dialog.close();
    $rootScope.openLoginDialog();
  }

});