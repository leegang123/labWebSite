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
<!-- Subject -> shop 로, List<Labnote> -> costList 로 넘어온다 -->
<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <div class="panel panel-default">
        <div class="panel-heading" style="color: green">'${shop.shopTitle}' 구매목록</div>
        <div class="panel-body">
            <table class="table table-bordered table-hover">
                <tr align="center" style="vertical-align: middle">
                    <td style="width: 50px">번호</td>
                    <td>물품</td>
                    <td style="width: 100px">금액</td>
                    <td style="width: 300px">구매자</td>
                    <td style="width: 70px">일시</td>
                    <td style="width: 130px">삭제</td>
                </tr>
                <c:forEach varStatus="st" var="cost" items="${costList}">
                    <tr style="text-align: center;">
                        <td>${totalListSize - ((cPage - 1) * 10 + st.index)}</td>
                        <td>${cost.costTitle}</td>
                        <td>${cost.costBalance}</td>
                        <td>${cost.costWriter}</td>
                        <td>${cost.costIndate}</td>
                        <c:if test="${cost.costTitle == '입금'}">
                            <td>
                                <a onclick="return confirm('정말로 삭제하시겠습니까? 되돌릴 수 없습니다.')" href="${contextPath}/costDelete.do?cost=deposit,${cost.costIdx}"
                                   class="btn btn-danger btn-sm">삭제</a>
                            </td>
                        </c:if>
                        <c:if test="${cost.costTitle != '입금'}">
                            <td>
                                <a onclick="return confirm('정말로 삭제하시겠습니까? 되돌릴 수 없습니다.')" href="${contextPath}/costDelete.do?cost=withdraw,${cost.costIdx}"
                                   class="btn btn-danger btn-sm">삭제</a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                <tr style="height: 30px;">
                    <!-- 이제 페이징 기능 구현해야 함 !!!! -->
                    <td colspan="6" style="text-align: center; border: none">
                        <input type="button" class="btn btn-success btn-middle pull-left" value="목록" onclick="location.href='${contextPath}/shopList.do'">
                        <c:if test="${startPage <= 1}">
                            <button onclick="location.href='${contextPath}/costList.do?page=${startPage - 1}'" class="btn btn-danger" disabled>이전</button>
                        </c:if>
                        <c:if test="${startPage > 1}">
                            <button onclick="location.href='${contextPath}/costList.do?page=${startPage - 1}'" class="btn btn-danger">이전</button>
                        </c:if>
                        <c:forEach var="page" begin="${startPage}" end="${endPage}">
                            <a href="${contextPath}/costList.do?page=${page}&shopIdx=${shop.shopIdx}">${page}</a>
                        </c:forEach>
                        <c:if test="${startPage + 10 >= totalPage}">
                            <button onclick="location.href='${contextPath}/costList.do?page=${endPage + 1}'" class="btn btn-danger" disabled>다음</button>
                        </c:if>
                        <c:if test="${startPage + 10 < totalPage}">
                            <button onclick="location.href='${contextPath}/costList.do?page=${endPage + 1}'" class="btn btn-danger">다음</button>
                        </c:if>
                        <input type="button" value="입금" class="btn btn-info btn-sm pull-right" onclick="location.href='${contextPath}/costDepositForm.do?shopIdx=${shop.shopIdx}'">
                        <input type="button" value="구매" class="btn btn-info btn-sm pull-right"
                               onclick="location.href='${contextPath}/costWithdrawForm.do?shopIdx=${shop.shopIdx}'" style="margin-right: 10px">
                    </td>
                </tr>
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





















