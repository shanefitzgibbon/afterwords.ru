'use strict';

var afterwords = angular.module('afterwords', ['ui.bootstrap', 'placeholders.img'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about-us', {
        redirectTo: function() {
          return 'about-' + $.i18n.lng();
        }
      })
      .when('/about-ru', {
        templateUrl: 'views/about-ru.html'
      })
      .when('/about-en', {
        templateUrl: 'views/about-en.html'
      })
      .otherwise({
        redirectTo: '/'
      });
  }])
  .run(function(){
    $.i18n.init({
      //lng: 'en',
      detectLngQS: 'lang',
      fallbackLng: 'en',
      debug: true,
      getAsync: false,
      selectorAttr: 'i18n',
      cookieName: 'aw-lang'
    }, function() {
      //callback after i18next init complete
    })
  })
  .directive('i18n', function () {
    return {
      restrict:'A',
      link:function postLink(scope, iElement, iAttrs) {
        iElement.i18n();
      }
    }
  });
