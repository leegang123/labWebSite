<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%request.setCharacterEncoding("UTF-8");%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="msgtype" value="${msgType}"/>
<html>
<head>

    <!-- '교수' 나 '관리자' 이면 어드민 페이지에 들어올 수 있는데, 다른 사람의 권한을 지정해 줄 수 있어야 하므로 권한수정 로직을 작성해야 한다. -->

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

        function memAuth(memName) {
            $.ajax({
                url : "/memAuth.do",
                type : "get",
                data : {"memName" : memName},
                success : function () {location.reload()},
                error : function () {alert('서버 에러')}
            })
        }

        function memNoAuthDelete(memName) {
            $.ajax({
                url: "/memNoAuthDelete.do",
                type: "get",
                data: {"memName" : memName},
                success : function () {location.reload()},
                error : function () {alert('서버 에러')}
            })
        }
    </script>

</head>
<body>

<div class="container">
    <jsp:include page="../common/header.jsp"/>
    <h3 style="color: red">승인 대기중 목록</h3>
    <p>교수님께서 승인을 해주셔야 회원 등급이 '학생'이 됩니다.</p>
    <table class="table table-bordered">
        <tr align="center" style="vertical-align: middle">
            <td>아이디</td>
            <td>이름</td>
            <td>나이</td>
            <td>이메일</td>
            <td style="width: 50px; color: green">승인</td>
            <td style="width: 50px; color: red">삭제</td>
        </tr>
        <c:forEach var="noAuthMember" items="${noAuthList}">

            <tr style="text-align: center;">
                <td>${noAuthMember.memID}</td>
                <td>${noAuthMember.memName}</td>
                <td>${noAuthMember.memAge}</td>
                <td>${noAuthMember.memEmail}</td>
                <td>
                    <button class="btn btn-success btn-sm" onclick="memAuth('${noAuthMember.memName}')">승인</button>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="memNoAuthDelete('${noAuthMember.memName}')">삭제</button>
                </td>
            </tr>
        </c:forEach>
    </table>

    <h3 style="color: orange">현재 학생 목록</h3>
    <p>우리 연구실의 전체 구성원 목록입니다.</p>
    <table class="table table-bordered">
        <tr align="center" style="vertical-align: middle">
            <td style="width: 50px;">번호</td>
            <td>아이디</td>
            <td>이름</td>
            <td>나이</td>
            <td>이메일</td>
            <td style="width: 50px">등급</td>
            <td style="width: 50px">관리</td>
        </tr>
        <c:forEach var="authMember" items="${authList}" varStatus="st">
            <tr style="text-align: center;">
                <td>${st.index + 1}</td>
                <td>${authMember.memID}</td>
                <td>${authMember.memName}</td>
                <td>${authMember.memAge}</td>
                <td>${authMember.memEmail}</td>
                <td>${authMember.memAuth}</td>
                <td><input type="button" onclick="location.href='${contextPath}/memChange.do?memIdx=${authMember.memIdx}'" class="btn btn-danger btn-sm" value="전환"></td>
            </tr>
        </c:forEach>
    </table>

    <h3 style="color: blue">전직 학생 목록</h3>
    <p>우리 연구실의 전직 구성원 목록입니다.</p>
    <table class="table table-bordered">
        <tr align="center" style="vertical-align: middle">
            <td style="width: 50px;">번호</td>
            <td>아이디</td>
            <td>이름</td>
            <td>나이</td>
            <td>이메일</td>
            <td style="width: 50px">등급</td>
            <td style="width: 50px">관리</td>
        </tr>
        <c:forEach var="oldMember" items="${oldMemberList}" varStatus="st">
            <tr style="text-align: center;">
                <td>${st.index + 1}</td>
                <td>${oldMember.memID}</td>
                <td>${oldMember.memName}</td>
                <td>${oldMember.memAge}</td>
                <td>${oldMember.memEmail}</td>
                <td>${oldMember.memAuth}</td>
                <td><input type="button" onclick="location.href='${contextPath}/memChange.do?oldMemIdx=${oldMember.memIdx}'" class="btn btn-danger btn-sm" value="전환"></td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
