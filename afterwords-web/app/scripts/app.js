'use strict';

function valueFn(value) {return function() {return value;};}

var afterwords = angular.module('afterwords', ['http-auth-interceptor','ngRoute', 'ngResource', 'ngCookies', 'ui.bootstrap', 'placeholders.img'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about-us', {
        templateUrl: function() {
          return 'views/about-' + $.i18n.lng() + '.html';
        },
        controller: StaticCntl
      })
      .when('/faq', {
          templateUrl: function() {
          return 'views/faq-' + $.i18n.lng() + '.html';
        },
        controller: StaticCntl
      })
      .when('/editors', {
        templateUrl: function() {
          return 'views/editors-' + $.i18n.lng() + '.html';
        },
        controller: StaticCntl
      })
      .when('/prices', {
        templateUrl: function() {
          return 'views/prices-' + $.i18n.lng() + '.html';
        },
        controller: StaticCntl
      })
      .when('/contacts', {
        templateUrl: function() {
          return 'views/contacts-' + $.i18n.lng() + '.html';
        },
        controller: StaticCntl
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
  })
  .filter('i18n', valueFn(function(key, options) { return $.t(key, options);}))
  .service('Job', function ($resource) {
    return $resource('http://afterwords.console.smfsoftware.com.au/jobs/:jobId', {}, {
      update: {method:'PUT'}
    });
  })
  .config(function($locationProvider) {
    $locationProvider.html5Mode(true);
  })
  .config(function($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
  })
  .config(function($dialogProvider){
    $dialogProvider.options({dialogFade: true, backdropFade: false})
  });

function StaticCntl($scope, $routeParams) {
  $scope.name = "StaticCntl";
  $scope.params = $routeParams;
}

function HeaderController($scope, $location)
{
  $scope.isActive = function (viewLocation) {
    return viewLocation === $location.path();
  };
}

