<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%request.setCharacterEncoding("UTF-8");%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
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
        });
    </script>

</head>
<body>
<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h2>프로젝트 생성</h2>
    <div class="panel panel-default">
        <div class="panel-heading"></div>
        <div class="panel-body">

            <form action="${contextPath}/subjectWrite.do" method="post">
                <input type="hidden" name="memIdx" value="${memIdx}">
                <table class="table table-bordered" style="text-align: center; margin-bottom: 50px">
                    <tr>
                        <td style="width: 120px; vertical-align: middle">프로젝트명</td>
                        <td colspan="2"><input type="text" class="form-control" name="subjectTitle"></td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">코멘트</td>
                        <td colspan="2"><textarea rows="3" name="subjectComment" class="form-control"></textarea></td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <input type="submit" class="btn btn-success btn-middle" value="플젝생성">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="panel-footer">순천대 농업생물정보학 연구실</div>
    </div>
</div>

</body>
</html>
