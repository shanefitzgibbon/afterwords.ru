'use strict';

function valueFn(value) {return function() {return value;};}

var afterwords = angular.module('afterwords', ['http-auth-interceptor', 'ngResource', 'ngCookies', 'ui.bootstrap', 'ngGrid', 'placeholders.img'])
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
      .when('/jobs', {
        templateUrl: 'views/jobs.html',
        controller: 'JobsCtrl'
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
    return $resource('http://afterwords.local\\:9000/jobs/:jobId', {}, {
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

