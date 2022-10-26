<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%request.setCharacterEncoding("UTF-8");%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="msgtype" value="${msgType}"/>
<html>
<head>
    <title>Spring MVC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".dropdown-toggle").dropdown();
            linkNullCheck();
        });

        function linkNullCheck() {
            var linkTitle = $("#linkTitle").val();
            const target = document.getElementById("submitButton");
            if (linkTitle == null || linkTitle.length === 0) {
                target.disabled = true;
            } else {
                target.disabled = false;
            }
        }
    </script>

</head>
<body>
<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h2>링크 작성</h2>
    <div class="panel panel-default">
        <div class="panel-heading">(링크작성은 꼭 url로 해주세요) ex -> https://blog.naver.com/naturelove87</div>
        <div class="panel-body">
            <form class="form-horizontal" action="${contextPath}/linkWrite.do" enctype="multipart/form-data" method="post">
                <div class="form-group">
                    <label class="control-label col-sm-2">글 제목 : </label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="linkTitle" name="linkTitle" onkeyup="linkNullCheck()"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">링크 : </label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="linkContent" name="linkContent"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2"></label>
                    <div class="col-sm-10">
                        <span style="color: red;" id="passMessage"></span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10" style="text-align: center">
                        <button type="submit" class="btn btn-warning btn-middle" id="submitButton">등록</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer">순천대 농업생물정보학 연구실</div>

    </div>
</div>

</body>
</html>
