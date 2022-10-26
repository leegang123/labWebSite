<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%request.setCharacterEncoding("UTF-8");%>
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
        $(document).ready(function() {
            $(".dropdown-toggle").dropdown();
            if (${!empty msgType}) {
                // 관리자의 승인을 받지 않은 경우나 아이디 비번이 다른 경우를 구분해주어야 함.
                if (${msgtype == '실패 메세지'}) {
                    $("#messageType").attr("class", "modal-content panel-danger")
                    $("#myMessage").modal("show");
                } else if (${msgtype == '성공 메세지'}) {
                    $("#messageType").attr("class", "modal-content panel-success")
                    $("#myMessage").modal("show");
                } else {
                    $("#messageType").attr("class", "modal-content panel-warning")
                    $("#myMessage").modal("show");
                }
            }
        });


    </script>

</head>
<body>
<!-- Subject -> subject 로, List<Labnote> -> labnoteList 로 넘어온다 -->
<div class="container">
    <jsp:include page="common/header.jsp"/>
    <div class="panel panel-default">
        <div class="panel-heading" style="color: red">순천대 농업생물정보학 연구실에 오신것을 환영합니다!</div>
        <div class="panel-body">
            <h3>공지사항 최신글</h3>
            <table class="table table-bordered table-hover" style="margin-bottom: 50px">
                <tr align="center" style="vertical-align: middle">
                    <td>제목</td>
                    <td style="width: 100px">작성자</td>
                    <td style="width: 300px">작성일</td>
                    <td style="width: 70px">조회수</td>
                </tr>
                <c:forEach varStatus="st" var="notice" items="${noticeList}">
                    <c:if test="${notice.noticeTop == 1}">
                        <tr style="text-align: center;background-color: #dcdcdc">
                            <td><a href="${contextPath}/noticeDetail.do?noticeIdx=${notice.noticeIdx}">${notice.noticeTitle}</a></td>
                            <td>${notice.noticeWriter}</td>
                            <td>${notice.noticeIndate}</td>
                            <td>${notice.noticeCount}</td>
                        </tr>
                    </c:if>
                    <c:if test="${notice.noticeTop != 1}">
                        <tr style="text-align: center;">
                            <td><a href="${contextPath}/noticeDetail.do?noticeIdx=${notice.noticeIdx}">${notice.noticeTitle}</a></td>
                            <td>${notice.noticeWriter}</td>
                            <td>${notice.noticeIndate}</td>
                            <td>${notice.noticeCount}</td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>

            <h3>랩노트 최신글</h3>
            <table class="table table-bordered table-hover">
                <tr align="center" style="vertical-align: middle">
                    <td>제목</td>
                    <td style="width: 100px">작성자</td>
                    <td style="width: 300px">작성일</td>
                    <td style="width: 70px">조회수</td>
                </tr>
                <c:forEach varStatus="st" var="labnote" items="${labnoteList}">
                    <tr style="text-align: center;">
                        <td><a href="${contextPath}/labnoteDetail.do?labnoteIdx=${labnote.labnoteIdx}">${labnote.labnoteTitle}</a></td>
                        <td>${labnote.labnoteWriter}</td>
                        <td>${labnote.labnoteIndate}</td>
                        <td>${labnote.labnoteHit}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content" id="checkType">
                    <div class="modal-header panel-heading">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">메세지 확인</h4>
                    </div>
                    <div class="modal-body">
                        <p id="checkMessage"></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>


            </div>
        </div>

        <!-- 실패 메시지 출력 -->
        <div id="myMessage" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content panel-info" id="messageType">
                    <div class="modal-header panel-heading">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">${msgType}</h4>
                    </div>
                    <div class="modal-body">
                        <p>${msg}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
        <div class="panel-footer">순천대 농업생물정보학 연구실</div>
    </div>
</div>



</body>
</html>
