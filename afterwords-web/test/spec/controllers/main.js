'use strict';

describe('Controller: MainCtrl', function() {

  // load the controller's module
  beforeEach(module('afterwords'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function($controller) {
    scope = {};
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('should have an document (awdoc) set on the scope', function(){
     expect(scope.awdoc).not.toBeNull();
  });

  it('should calculate the word count of the text set in awdoc', function(){
    scope.awdoc.text = "This is our test text.";
    expect(scope.awdoc.wordCount()).toEqual(5);

  });

  it('should calculate a word count of 0 when text in awdoc is empty or undefined', function(){
    scope.awdoc.text = "";
    expect(scope.awdoc.wordCount()).toEqual(0);
    scope.awdoc.text = undefined;
    expect(scope.awdoc.wordCount()).toEqual(0);
  });

  xit('')
});
