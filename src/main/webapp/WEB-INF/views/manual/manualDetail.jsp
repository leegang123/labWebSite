<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%request.setCharacterEncoding("UTF-8");%>
<c:set var="msgtype" value="${msgType}"/>
<%
    pageContext.setAttribute("br", "<br/>");
    pageContext.setAttribute("cn", "\n");
%>
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

        function getmanualFile(filename) {
            location.href = "${contextPath}/manualFileDownload.do?filename="+ encodeURI(filename);
        }

        function manualReplWrite(manualIdx) {
            var manualReplContent = $("#manualReplContent").val();

            location.href="${contextPath}/manualReplWrite.do?manualIdx="+manualIdx+"&manualReplContent="+encodeURI(manualReplContent);

        }


    </script>

</head>
<body>
<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <div class="panel panel-default">
        <div class="panel-heading">${manual.manualIdx}번째 메뉴얼</div>
        <div class="panel-body">
            <table class="table" style="text-align: center">
                <tr>
                    <td style="width: 150px">제목 : </td>
                    <td id="manualTitle" style="text-align: left">${manual.manualTitle}</td>
                </tr>
                <tr>
                    <td style="width: 150px">작성자 : </td>
                    <td style="text-align: left">${manual.manualWriter}</td>
                </tr>
                <tr>
                    <td>내용 : </td>
                    <td id="manualContent" style="text-align: left">${fn:replace(manual.manualContent, cn, br)} </td>
                </tr>
                <tr>
                    <td>작성일 : </td>
                    <td style="text-align: left">${fn:split(manual.manualIndate, " " )[0]} </td>
                </tr>

                <!-- 파일을 업로드하지 않았는데 파일 아이콘이 뜨는 오류를 없애기 위해 파일을 업로드 했을때만 아이콘이 보이도록 함 -->
                <c:if test="${!empty manual.manualFile}">
                    <c:forEach var="fName" items="${manualFile}" varStatus="st">
                        <tr>
                            <td>첨부파일${st.index + 1}</td>
                            <td><a href="javascript:getmanualFile('${fName}')" ><span class="glyphicon glyphicon-file"></span>${fn:split(fName, "`")[1]}</a></td>
                        </tr>
                    </c:forEach>
                </c:if>
                <tr>
                    <td colspan="2"><textarea rows="4" style="width: 100%" name="manualReplContent" id="manualReplContent"></textarea> </td>

                </tr>
                <!-- 메뉴얼 작성자와 세션에 등록된 회원이름과 같을 경우에만 글삭제와 글수정을 할 수 있도록 한다. -->
                <tr align="center" style="height: 80px;">
                    <td colspan="2" >
                        <input type="button" value="목록 보기" class="btn btn-success btn-sm pull-left" onclick="location.href='${contextPath}/manualList.do'"/>
                        <c:if test="${mvo.memName == manual.manualWriter}">
                            <input type="button" value="글수정" class="btn btn-primary btn-middle" onclick="location.href='${contextPath}/manualUpdateForm.do?manualIdx=${manual.manualIdx}'"/>
                            <a onclick="return confirm('정말로 삭제하시겠습니까? 되돌릴 수 없습니다.')" href="${contextPath}/manualDelete.do?manualIdx=${manual.manualIdx}"
                               class="btn btn-danger">글삭제</a>
                        <input type="button" value="댓글 작성" class="btn btn-warning pull-right" onclick="manualReplWrite('${manual.manualIdx}')">
                        </c:if>
                        <c:if test="${!(mvo.memName == manual.manualWriter)}">
                            <input type="button" value="글수정" class="btn btn-primary btn-middle" disabled/>
                            <input type="button" value="글삭제" class="btn btn-danger btn-middle" disabled/>
                            <input type="button" value="댓글 작성" class="btn btn-warning btn-sm pull-right" onclick="manualReplWrite('${manual.manualIdx}')">
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table class="table">
                            <c:if test="${!empty manualReplList}">
                                <tr>
                                    <td style="width: 150px">이름</td>
                                    <td>댓글 내용</td>
                                    <td style="width: 200px">작성일</td>
                                    <td style="width: 200px">삭제</td>
                                </tr>
                                <c:forEach var="manualReplVO" items="${manualReplList}">
                                    <tr>
                                        <td>${manualReplVO.manualReplWriter}</td>
                                        <td>${fn:replace(manualReplVO.manualReplContent, cn, br)}</td>
                                        <td>${fn:split(manualReplVO.manualReplIndate, " ")[0]}</td>
                                        <td>
                                            <input type="button" class="btn btn-danger btn-sm" value="삭제" onclick="location.href='${contextPath}/replDelete.do?repl=manual,${manualReplVO.manualReplIdx}'">
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
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
