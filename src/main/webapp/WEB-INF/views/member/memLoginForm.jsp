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

        function login() {
            var memID = $("#memID").val();
            var memPassword = $("#memPassword").val();

            if (memID == null || memID === "" || memPassword == null || memPassword === "") {

                // 현재 페이지에서 바로 띄우는 모달창 샘플
                $("#checkMessage").html("아이디와 비밀번호를 모두 입력해 주세요.");
                $("#checkType").attr("class", "modal-content panel-warning")
                $("#myModal").modal("show");

                return;
            }
            // 모든 항목에 값이 채워져있으면 서버로 전송
            document.frm.submit();
        }
    </script>

</head>
<body>

<div class="container">

    <h2>로그인 페이지</h2>
    <img src="../../../images/제목%20없음.png" width="100%" height="300px"/>
    <div class="panel panel-default">
        <div class="panel-body">
            <form action="${contextPath}/memLogin.do" method="post" name="frm" id="frm">
                <table class="table table-bordered" style="text-align: center">
                    <tr>
                        <td style="width: 110px; vertical-align: middle">아이디</td>
                        <td>
                            <input type="text" class="form-control" maxlength="20" placeholder="아이디는 20자 내 알파벳 대소문자" id="memID" name="memID">
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 110px; vertical-align: middle">비밀번호</td>
                        <td colspan="2">
                            <input type="password" class="form-control" maxlength="20" placeholder="패스워드는 20자 내 알파벳 대소문자" id="memPassword" name="memPassword" >
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" style="text-align: left;">
                            <input type="button" class="btn btn-danger btn-sm pull-left" value="회원가입" onclick="location.href='${contextPath}/memJoinForm.do'">
                            <input type="button" value="로그인" class="btn btn-info btn-sm pull-right" onclick="login()"></td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="panel-footer">순천대 농업생물정보학 연구실</div>

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
</div>






</body>
</html>
