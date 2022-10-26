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
    <h2>랩노트 수정</h2>
    <div class="panel panel-default">
        <div class="panel-heading" style="color: green">${m.memName}님의 '${subject.subjectTitle}'프로젝트</div>
        <div class="panel-body">
            <form class="form-horizontal" action="${contextPath}/labnoteUpdate.do" method="post" >

                <!-- SubjectIdx, labnoteTitle, labnoteComment, labnote.labnoteWriter, subject.memIdx 넘겨줘야 함 -->
                <input type="hidden" name="subjectIdx" value="${labnote.subjectIdx}">
                <input type="hidden" name="labnoteWriter" value="${labnote.labnoteWriter}">
                <input type="hidden" name="labnoteIdx" value="${labnote.labnoteIdx}">
                <div class="form-group">
                    <label class="control-label col-sm-2">제목 : </label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="labnoteTitle" name="labnoteTitle" value="${labnote.labnoteTitle}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">내용 : </label>
                    <div class="col-sm-10">
                        <textarea name="labnoteComment" id="labnoteComment" class="form-control" rows="20">${labnote.labnoteComment}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10" style="text-align: center">
                        <button type="submit" class="btn btn-warning btn-middle">수정</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer">순천대 농업생물정보학 연구실</div>
    </div>
</div>

</body>
</html>
