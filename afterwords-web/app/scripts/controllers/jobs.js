/**
 * Created with IntelliJ IDEA.
 * User: shane
 * Date: 26/03/13
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
afterwords.controller('JobsCtrl', function($scope, $resource, jobService, $log) {

  $scope.pending = jobService.pendingJobs();

  $scope.completed = jobService.completedJobs();

  var columns = [
    {field:'created', displayName:$.t('jobs.created'), width: '*'},
    {field:'completed', displayName:$.t('jobs.completed'), width: '*'},
    {field:'dueDate', displayName:$.t('jobs.due-date'), width: '*'},
    {field:'originalDocument.text', displayName:$.t('jobs.original-text'), width: '**'}
  ];

  $scope.pendingGridOptions = {
    data: 'pending',
    columnDefs: columns
  };

  $scope.completedGridOptions = {
    data: 'completed',
    columnDefs: columns
  };
});

