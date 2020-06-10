var app = angular.module('vacApp', []);
app.controller('writeCtrl', function ($scope, $http, $window) {
    $scope.days = 1;
    $scope.reason = "有紧急事情";

    $scope.applyVac = function () {
        $http.post(
            "/startVac",
            {
                "days" : $scope.days,
                "reason" : $scope.reason
            }
        ).then(function (response) {
            if (response.data == true) {
                alert("操作成功！");
                $window.location.reload();
            }
        })
    }
});
app.controller('myVacCtrl', function ($scope, $http) {

    $scope.vacList = [];

    $scope.myVac = function () {
        $http.get(
            "/myVac"
        ).then(function (response) {
            $scope.vacList = response.data;
        })
    }
});

app.controller('myVacRecord', function ($scope, $http) {

    $scope.vacList = [];

    $scope.myVacRecord = function () {
        $http.get(
            "/myVacRecord"
        ).then(function (response) {
            $scope.vacRecordList = response.data;
        })
    }
});

app.controller("myAudit", function ($scope, $http, $window) {
    $scope.vacTaskList = [];

    $scope.myAudit = function () {
        $http.get(
            "/myAudit"
        ).then(function (response) {
            $scope.vacTaskList = response.data;
        })
    };

    $scope.passAudit = function (taskId, result) {
        $http.post(
            "/passAudit",
            {
                "id" : taskId,
                "vac" : {
                    "result" : result >= 1 ? "审核通过" : "审核拒绝"
                }
            }
        ).then(function (response) {
            if (response.data == true) {
                alert("操作成功！");
                $window.location.reload();
            } else {
                alert("操作失败！");
            }
        })
    }
});

app.controller('myAuditRecord', function ($scope, $http) {

    $scope.auditVacRecordList = [];

    $scope.myAuditRecord = function () {
        $http.get(
            "/myAuditRecord"
        ).then(function (response) {
            $scope.auditVacRecordList = response.data;
        })
    }
});



