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

        // 아이디 중복체크(ajax 이용)
        function memIdCheck() {
            var memID = $("#memID").val();

            if (memID == null || memID === '') {
                $("#checkMessage").html("아이디를 먼저 입력해 주세요.");
                $("#checkType").attr("class", "modal-content panel-danger")
                $("#myModal").modal("show");

                return;
            }

            $.ajax({
                url : "${contextPath}/memIdCheck.do",
                type : "get",
                data : {"memID":memID},
                success : function (result) {

                    // 서버에서 @ResponseBody를 통해 ajax통신을 하고, 성공 -> 1, 실패 -> 0을 리턴
                    if (result === 1) {
                        $("#checkMessage").html("사용할 수 있는 아이디 입니다.");
                        $("#checkType").attr("class", "modal-content panel-success");
                    } else {
                        $("#checkMessage").html("사용할 수 없는 아이디 입니다.");
                        $("#checkType").attr("class", "modal-content panel-warning");
                    }
                    $("#myModal").modal("show");
                },
                error : function () {alert('아이디 체크 에러');}
            })
        }

        function passwordCheck() {
            var memPassword1 = $("#memPassword1").val();
            var memPassword2 = $("#memPassword2").val();

            if (memPassword1 !== memPassword2) {
                $("#passMessage").html("비밀번호가 서로 일치하지 않습니다.");
                $("#submitButton").attr("disabled", "disabled");
            } else {
                $("#passMessage").html("");
                $("#memPassword1").val(memPassword2);
                $("#submitButton").attr("disabled", false);
            }
        }

        function memJoin() {
            var memAge = $("#memAge").val();
            if (memAge == null || memAge === "" || memAge === 0) {
                alert('나이를 입력하세요.');
                return false;
            }

            document.noImageFrm.submit();
        }

        function memImageUpdate() {
            document.imageFrm.submit();
        }
    </script>
</head>
<body>

<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h2>회원정보 수정</h2>
    <div class="panel panel-default">
        <div class="panel-heading"></div>
        <div class="panel-body">

            <form action="${contextPath}/memUpdate.do" method="post" name="noImageFrm">
                <input type="hidden" name="memName" value="${mvo.memName}">
                <input type="hidden" name="memProfile" value="${mvo.memProfile}">
                <input type="hidden" name="memAuth" value="${mvo.memAuth}">
                <input type="hidden" name="memID" value="${mvo.memID}">
                <table class="table table-bordered" style="text-align: center; border: 1px solid #dddddd; margin-bottom: 50px">
                    <tr>
                        <td style="width: 120px; vertical-align: middle">아이디</td>
                        <td colspan="2">${mvo.memID}</td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">비밀번호</td>
                        <td colspan="2"><input type="password" class="form-control" maxlength="20"
                                               placeholder="비밀번호는 20자 내 알파벳 대소문자" id="memPassword1" name="memPassword1"  onkeyup="passwordCheck()"></td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">비밀번호 확인</td>
                        <td colspan="2"><input type="password" class="form-control" maxlength="20"
                                               placeholder="비밀번호 다시 입력" id="memPassword2" name="memPassword2" onkeyup="passwordCheck()"></td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">이름</td>
                        <td colspan="2">${mvo.memName}</td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">생년월일</td>
                        <td colspan="2"><input type="text" class="form-control" placeholder="나이를 입력하세요." id="memAge" name="memAge" value="${mvo.memAge}"></td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">이메일</td>
                        <td colspan="2"><input type="text" class="form-control" placeholder="이메일을 입력하세요" id="memEmail" name="memEmail" value="${mvo.memEmail}"></td>
                    </tr>
                    <tr>
                        <td style="width: 120px; vertical-align: middle">전화번호</td>
                        <td colspan="2"><input type="text" class="form-control" placeholder="ex) 010-1111-2222" id="memPhone" name="memPhone" value="${mvo.memPhone}"></td>
                    </tr>
                    <tr>
                        <!-- passMessage 는 패스워드가 일치하는지를 확인해주는 역할 -->
                        <td colspan="3" style="text-align: left;"><span id="passMessage" style="color: red"></span> <input type="button" value="등록" class="btn btn-info btn-middle pull-right" onclick="memJoin()" id="submitButton"></td>
                    </Tr>
                </table>
            </form>
                    <h2>프로필 사진 수정</h2>
            <form action="${contextPath}/memImageUpdate.do" method="post" name="imageFrm" enctype="multipart/form-data">
                <input type="hidden" name="memName" value="${mvo.memName}">
                <table class="table table-bordered" style="text-align: center; border: 1px solid #dddddd">
                    <tr>
                        <td style="width: 110px; vertical-align: middle">사진 업로드</td>
                        <td colspan="2">
                            <span class="btn btn-default">
                                이미지를 업로드하세요.<input type="file" name="memProfile">
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <Td colspan="3"><input type="button" value="사진 수정" class="btn btn-danger btn-middle pull-right" id="imageSubmitButton" onclick="memImageUpdate()"></Td>
                    </tr>
                </table>
            </form>



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





























