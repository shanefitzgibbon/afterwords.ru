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

  it('should have a process set on the scope', function () {
    expect(scope.process).not.toBeNull();
  });

  it('should default the current step in the process to "step1"', function () {
    expect(scope.process.currentStep).toEqual('step1')
  });

  it('should default the "include analysis" in the document to false', function () {
    expect(scope.awdoc.includeAnalysis).toBe(false);
  });

  it('should revert the current step in the process to "step1" when the cancel method is called', function () {
    scope.process.currentStep = 'step2';
    expect(scope.process.currentStep).toEqual('step2');
    scope.process.cancel();
    expect(scope.process.currentStep).toEqual('step1');
    expect(scope.awdoc.includeAnalysis).toBe(false)
  });

  it('should calculate the number of pages of text', function () {
    scope.awdoc.text = "this is a very short text";
    expect(scope.awdoc.pageCount()).toBe(1);
    scope.awdoc.text = testText599Words;
    expect(scope.awdoc.pageCount()).toBe(2);
    scope.awdoc.text = testText601Words;
    expect(scope.awdoc.pageCount()).toBe(3);

  });

  it('should calculate the cost of a job with text < 300 words to cost 250 rubles', function () {
    scope.awdoc.text = "this is a very short text";
    scope.awdoc.includeAnalysis = false;
    expect(scope.awdoc.cost()).toBe(250);
    scope.awdoc.text = testText599Words;
    expect(scope.awdoc.cost()).toBe(500);
    scope.awdoc.text = testText601Words;
    expect(scope.awdoc.cost()).toBe(750);
  });

  it('should calculate the cost of a job with 1 page + analysis to cost 950 rubles', function () {
    scope.awdoc.text = "this is a very short text";
    scope.awdoc.includeAnalysis = true;
    expect(scope.awdoc.cost()).toBe(950);
  });

  it('should calculate the cost of a job with 2 pagea + analysis to cost 1900 rubles', function () {
    scope.awdoc.text = testText599Words;
    scope.awdoc.includeAnalysis = true;
    expect(scope.awdoc.cost()).toBe(1900);
  });

  it('should calculate the cost of a job with 3 page + analysis to cost 2850 rubles', function () {
    scope.awdoc.text = testText601Words;
    scope.awdoc.includeAnalysis = true;
    expect(scope.awdoc.cost()).toBe(2850);
  });

  var testText599Words = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ante orci, ornare at laoreet non, aliquet in turpis. Ut a pellentesque libero. Ut varius rutrum nunc. Nulla non est metus, at mollis tortor. Vestibulum eget velit vel massa mollis tempus in auctor ante. Vivamus pharetra, mauris in hendrerit mattis, sapien lorem ornare arcu, eget volutpat nisl nulla at nisi. Nam gravida ipsum nec nibh vehicula pharetra. Fusce sed tempus leo. Nullam rutrum neque dui, vel dapibus magna. Aenean eros massa, placerat vitae feugiat in, venenatis ut est. Duis quis augue ante, rutrum vulputate neque.\n\nInteger semper augue nec est pulvinar ut adipiscing massa porttitor. Duis nisi dolor, imperdiet vel dapibus eu, tincidunt ac enim. Mauris nec lacus sed nibh sollicitudin adipiscing. Quisque volutpat leo dapibus justo aliquet blandit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam dignissim dignissim mi, eu condimentum diam accumsan ut. Sed arcu dui, ornare id aliquam ac, venenatis vel nisl.\n\nPhasellus interdum lobortis justo, sit amet facilisis mauris porta a. Nam mattis viverra odio quis cursus. Nullam ac ultrices diam. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras pulvinar ullamcorper magna, malesuada consectetur arcu tempor a. Vivamus eget dui eget nibh posuere egestas. Morbi vel arcu rutrum diam tincidunt ultricies fringilla at risus. Nullam tempor facilisis ante.\n\nPhasellus vitae est at arcu scelerisque dapibus. Etiam placerat ligula id diam sagittis ac iaculis dolor volutpat. Phasellus nibh felis, mollis sed lacinia ut, convallis vitae erat. Sed mi tellus, rutrum vel vehicula a, mollis vel nibh. Donec pharetra nisl sed augue tempus tincidunt. Pellentesque ac elementum sapien. Aliquam nec leo sed augue aliquam tristique. Ut varius nisi nec mauris facilisis fringilla. Praesent blandit iaculis mi at hendrerit. Proin consectetur magna tempor est dapibus et placerat dui ullamcorper. Quisque imperdiet cursus fermentum. Fusce metus dolor, condimentum eu rhoncus quis, placerat in mi. Proin hendrerit bibendum orci et dictum. Aliquam pellentesque, justo placerat ultricies egestas, ipsum orci sollicitudin neque, tincidunt tristique nibh mauris quis dolor. Nullam faucibus sollicitudin tortor eget condimentum.\n\nCurabitur non enim elit. Duis non posuere enim. Sed et ligula mi, at sollicitudin est. Suspendisse sollicitudin enim diam. Suspendisse sit amet eros vitae lorem faucibus tincidunt eget at sapien. Donec a mi nec mi dapibus placerat vitae eu metus. Sed porta tincidunt viverra. Integer mattis eros interdum sapien consequat eget iaculis arcu ultricies. Nullam at varius arcu.\n\nSed quis dui ac felis ornare accumsan. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi luctus sapien quis augue tempor ac luctus sapien auctor. Morbi eget nulla odio, at volutpat est. Nulla eu euismod ante. Proin feugiat scelerisque vehicula. Phasellus suscipit ullamcorper ligula id sollicitudin. Donec at leo libero, eget pellentesque erat. Phasellus sed nulla erat. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tempor pellentesque. Mauris ullamcorper mi at metus pharetra lacinia. Aliquam pretium, ipsum vulputate aliquam adipiscing, nisi mauris accumsan urna, eu scelerisque diam tellus in nisi. Nullam odio metus, scelerisque et porta nec, tristique semper eros. Donec eu nibh in est mollis bibendum.\n\nFusce metus turpis, congue et dignissim sit amet, eleifend non tortor. Suspendisse potenti. Nam dui diam, congue nec faucibus vitae, convallis sit amet risus. Donec at lorem massa, id rhoncus eros. Nam pellentesque, massa vel auctor tempus, dui elit adipiscing nisl, vitae volutpat tortor velit nec augue. Aliquam vel dui ut arcu feugiat consectetur. Ut vitae urna nibh. Phasellus nisi purus, porttitor id tincidunt eget, cursus sed odio. Curabitur lacus lacus, scelerisque vitae semper vel, semper sit amet ante. Sed sagittis tortor tortor, a tincidunt tellus. Integer.";

  var testText601Words = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam interdum dolor ac mi tempus pharetra. Nullam et magna mauris, ac lacinia dui. Nulla facilisi. Donec tellus nibh, malesuada id sagittis vitae, imperdiet et felis. Integer at aliquet diam. Vestibulum ornare placerat odio, viverra congue augue congue nec. Suspendisse potenti. Donec tempus urna nec nisi gravida eget iaculis sapien ultrices. Suspendisse consectetur suscipit elementum. Cras ac massa vel elit tempus semper sed sed enim. Nullam at est justo, quis mattis ante. Donec sapien dolor, fermentum at posuere vel, tempus sit amet augue. Morbi purus turpis, ullamcorper et aliquam ut, fermentum sit amet risus.\n\nProin dapibus pharetra dolor non molestie. Mauris eleifend porta dolor, vitae viverra dui ultricies a. Nunc quis ligula felis. Etiam in ipsum erat, nec mollis quam. Duis cursus elementum ligula ut congue. Etiam aliquam, ipsum et accumsan congue, mi lectus euismod velit, in cursus sapien justo sit amet lorem. Sed et lorem massa, vitae rhoncus ipsum. Quisque id tellus purus. Aenean elit elit, molestie ut convallis vel, aliquet ut orci. Morbi id quam a nisl cursus pretium. Nulla quis tellus vitae ante lacinia euismod. Aliquam erat volutpat. Maecenas eu magna a orci adipiscing pellentesque nec ut nunc. Morbi ut mauris vitae turpis mattis egestas.\n\nAenean cursus mattis pellentesque. Morbi imperdiet, orci a luctus porttitor, neque velit porta enim, a dictum turpis metus ut urna. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In rutrum fermentum libero a facilisis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean vitae odio magna. Proin consequat accumsan lacus, quis sagittis orci mollis molestie. Curabitur hendrerit leo ut nunc vulputate ut aliquam nunc congue. Vivamus ullamcorper aliquam purus, eget viverra quam pulvinar eget. Quisque pulvinar elementum metus id congue. Nam sit amet hendrerit nunc. Donec lectus nisl, facilisis et venenatis eu, rutrum sed erat.\n\nNulla eget laoreet orci. Nulla eget felis eros, a fermentum sapien. Etiam semper molestie ullamcorper. Curabitur neque risus, volutpat a laoreet faucibus, volutpat et elit. Nunc elementum, neque sed congue tempus, enim est euismod quam, vel tempus lacus libero nec elit. Ut augue purus, ornare eu eleifend congue, accumsan nec libero. Pellentesque laoreet, nulla eu pellentesque posuere, enim urna porttitor metus, nec ultrices augue ipsum ac sem.\n\nCurabitur sed tristique sem. Fusce vitae nisl id turpis hendrerit sollicitudin. Nulla facilisi. Vestibulum consectetur sodales convallis. Sed eget lectus metus. Nulla aliquam tristique enim sit amet sollicitudin. Donec neque sem, rutrum et tempus eu, posuere quis nulla. Vestibulum sollicitudin eleifend consequat. Curabitur lacus enim, fringilla sit amet tempus a, tincidunt non nibh. Duis purus lacus, suscipit quis facilisis quis, placerat eu sapien. Fusce molestie nibh id ante semper eget ultrices nisl tincidunt. Nunc nec tortor massa. Quisque tincidunt hendrerit dapibus.\n\nPhasellus ac iaculis libero. Morbi ut massa eu mauris pulvinar ullamcorper. Suspendisse ullamcorper leo eget ipsum molestie a mattis nibh eleifend. Sed vel orci a erat venenatis vestibulum vel eu dolor. Integer arcu lacus, porttitor sit amet sagittis eu, rhoncus et sem. Ut vestibulum urna sit amet nulla sagittis blandit. Pellentesque diam dolor, pharetra blandit tristique eu, vestibulum vitae nibh. Donec quis commodo arcu. Vivamus ac tortor quis neque auctor convallis at quis mi.\n\nSed semper mauris eu nibh convallis lacinia. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec rhoncus sem ac arcu iaculis et blandit risus fermentum. Quisque blandit tellus nec purus ornare at pharetra nisi euismod. Mauris lobortis lorem dictum neque mollis id imperdiet lacus ultricies. Nam ut tempus metus. Etiam metus mauris, tempor eget pellentesque quis, posuere sit amet est. Curabitur vitae lacus odio, sed lobortis ipsum. Suspendisse.";
});
