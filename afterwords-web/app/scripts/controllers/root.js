'use strict';

afterwords.controller('RootCtrl', function($scope, $window){
  $scope.updateLang = function(language) {
    $.i18n.setLng(language, function(){
//      $scope.$apply();
      $window.location.reload(); //this event should be rare so a reload is ok??
    });
  }
});