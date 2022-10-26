<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
            noticeNullCheck();
        });
        var cnt = 1;
        function notice_file_add() {
            $("#passMessage").html("첨부파일은 수정할 수 없습니다. 신중하게 등록해주세요!");
            $("#d_file").append("<br>" + "<input type='file' name='file"+cnt+"'/>");
            cnt++;
        }

        function noticeNullCheck() {
            var noticeTitle = $("#noticeTitle").val();
            const target = document.getElementById("submitButton");
            if (noticeTitle === '') {
                target.disabled= true;
            } else {
                target.disabled= false;
            }
        }

        function submitLock() {
            const target = document.getElementById("submitButton");
            target.disabled = true;
            $("#frm").submit();
        }
    </script>

</head>
<body>
<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h2>공지사항 작성</h2>
    <div class="panel panel-default">
        <div class="panel-heading"></div>
        <div class="panel-body">
            <form class="form-horizontal" action="${contextPath}/noticeWrite.do" enctype="multipart/form-data" method="post" id="frm">
                <div class="form-group">
                    <label class="control-label col-sm-2">글 제목 : </label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="noticeTitle" name="noticeTitle" onkeyup="noticeNullCheck()"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">내용 : </label>
                    <div class="col-sm-10">
                        <textarea name="noticeContent" id="noticeContent" class="form-control" rows="20"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2"></label>
                    <div class="col-sm-10">
                        <span style="color: red;" id="passMessage"></span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">파일추가 :</label>
                    <div class="col-sm-10">
                        <input type="button" value="파일 추가" class="btn btn-primary btn-sm" onclick="notice_file_add()"><br>
                        <div id="d_file"></div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">최상단 고정 :</label>
                    <div class="checkbox col-sm-10">
                        <label><input type="checkbox" name="noticeTop" value="1">최상단 고정</label>
                    </div>
                </div>
                <div class="form-group">

                    <div class="col-sm-offset-2 col-sm-10" style="text-align: center">
                        <button type="submit" class="btn btn-warning btn-middle" id="submitButton" onclick="submitLock()">등록</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer">순천대 농업생물정보학 연구실</div>

    </div>
</div>

</body>
</html>