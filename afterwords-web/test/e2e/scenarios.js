'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('afterwords app', function() {

//  beforeEach(function() {
//    browser().navigateTo('../../app/index.html');
//  });


//  xit('should automatically redirect to /view1 when location hash/fragment is empty', function() {
//    expect(browser().location().url()).toBe("/");
//  });

  describe('main', function() {
    it('should update the word count when text is entered into the textarea', function() {
      browser().navigateTo('/index.html');
      input('awdoc.text').enter('Here is some test text.');
      expect(element('#word-count-value').text()).toEqual('5');
      //pause();
    });

    it('should enable the continue button when the "agree to terms and conditions" box is checked', function(){
      browser().navigateTo('/index.html');
      expect(element('#continue').attr('disabled')).toEqual('disabled');
      input('awdoc.agreeTerms').check();
      expect(element('#continue').attr('disabled')).not().toBeDefined();
    });

    it('should enable the cancel button when the "agree to terms and conditions" box is checked', function(){
      browser().navigateTo('/index.html');
      expect(element('#cancel').attr('disabled')).toEqual('disabled');
      input('awdoc.agreeTerms').check();
      expect(element('#cancel').attr('disabled')).not().toBeDefined();
    });

    it('should transition to the "step 2 panel" when the continue button is pressed', function(){
      browser().navigateTo('index.html');
      input('#continue').click();
    });
  });

});
