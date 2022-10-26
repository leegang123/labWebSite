<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%request.setCharacterEncoding("UTF-8");%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="msgtype" value="${msgType}"/>
<c:set var="linkContent" value="${link.linkContent}"/>
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

<!-- 그냥 테이블 복붙해놨는데 이거 수정부터 시작해야 함!!!!!!!!! -->


<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h1 style="color: blueviolet">링크</h1>
    <table class="table">
        <tr align="center" style="vertical-align: middle">
            <td style="width: 50px">번호</td>
            <td>제목</td>
            <td style="width: 100px">작성자</td>
            <td style="width: 150px">작성일</td>
            <td style="width: 100px">관리</td>
            </tr>
        <c:forEach varStatus="st" var="link" items="${linkList}">
            <tr style="text-align: center;">
                <td>${totalListSize - ((cPage - 1) * 10 + st.index)}</td>
                <td><a href="${link.linkContent}" target="_blank" onclick="console.log('${link.linkContent}')">${link.linkTitle}</a></td>
                <td>${link.linkWriter}</td>
                <td>${fn:split(link.linkIndate, " " )[0]}</td>
                <td>
                    <input type="button" onclick="location.href='linkDelete.do?linkIdx=${link.linkIdx}'" class="btn btn-danger btn-sm" value="삭제">
                </td>
            </tr>
        </c:forEach>
        <tr style="height: 30px; ">
            <!-- 이제 페이징 기능 구현해야 함 !!!! -->
            <td colspan="4" style="text-align: center">
                <c:if test="${startPage <= 1}">
                    <button onclick="location.href='${contextPath}/linkList.do?page=${startPage - 1}'" class="btn btn-danger" disabled>이전</button>
                </c:if>
                <c:if test="${startPage > 1}">
                    <button onclick="location.href='${contextPath}/linkList.do?page=${startPage - 1}'" class="btn btn-danger">이전</button>
                </c:if>
                <c:forEach var="page" begin="${startPage}" end="${endPage}">
                    <a href="${contextPath}/linkList.do?page=${page}">${page}</a>
                </c:forEach>
                <c:if test="${startPage + 10 >= totalPage}">
                    <button onclick="location.href='${contextPath}/linkList.do?page=${endPage + 1}'" class="btn btn-danger" disabled>다음</button>
                </c:if>
                <c:if test="${startPage + 10 < totalPage}">
                    <button onclick="location.href='${contextPath}/linkList.do?page=${endPage + 1}'" class="btn btn-danger">다음</button>
                </c:if>
            </td>
            <td>
                <input type="button" value="글쓰기" class="btn btn-info btn-sm pull-right" onclick="location.href='${contextPath}/linkWriteForm.do'">
            </td>
        </tr>
    </table>
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
</div>
</body>
</html>
