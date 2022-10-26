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

<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h2 style="color: orange">비용 관리</h2>
    <table class="table">
        <tr align="center" style="vertical-align: middle">
            <td style="width: 50px">번호</td>
            <td>가게명</td>
            <td>코멘트</td>
            <td>잔액</td>
            <td style="width: 150px">삭제</td>
        </tr>
        <c:forEach varStatus="st" var="shop" items="${shopList}">
            <tr style="text-align: center;">
                <td>${st.index + 1}</td>
                <td style="width: 300px"><a href="${contextPath}/costList.do?shopIdx=${shop.shopIdx}">${shop.shopTitle}</a></td>
                <td>${fn:replace(shop.shopComment, cn, br)}</td>
                <td>${shop.shopBalance}</td>
                <td>
                    <a onclick="return confirm('정말로 삭제하시겠습니까? 되돌릴 수 없습니다.')" href="${contextPath}/shopDelete.do?shopIdx=${shop.shopIdx}"
                       class="btn btn-danger btn-sm">삭제</a>
                </td>
            </tr>
        </c:forEach>
        <tr style="height: 30px;">
            <!-- 이제 페이징 기능 구현해야 함 !!!! -->
            <td colspan="5">
                <input type="button" value="새 프로젝트" class="btn btn-success btn-sm pull-right" onclick="location.href='${contextPath}/shopWriteForm.do'">
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

</body>
</html>
