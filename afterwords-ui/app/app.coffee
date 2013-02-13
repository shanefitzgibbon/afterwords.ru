'use strict'

# Declare app level module which depends on filters, and services
App = angular.module('app', [
  'ngCookies'
  'ngResource'
  'app.controllers'
  'app.directives'
  'app.filters'
  'app.services'
])

App.config([
  '$routeProvider'
  '$locationProvider'

  ($routeProvider, $locationProvider, config) ->

    $routeProvider

      .when('/todo', {templateUrl: '/partials/todo.html',
      controller: 'TodoCtrl'})

      .when('/view1', {templateUrl: '/partials/partial1.html',
      controller: 'AppCtrl'})

      .when('/view2', {templateUrl: '/partials/partial2.html',
      controller: 'AppCtrl'})

    # Catch all
      .otherwise({redirectTo: '/todo'})

    # Without server side support html5 must be disabled.
    $locationProvider.html5Mode(true)
])