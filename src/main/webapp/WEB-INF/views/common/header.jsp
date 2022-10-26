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
            memberList();
            searchForm();
            oldMemberList();
        });

        function memberList() {
            $.ajax({
                url : "/memberList.do",
                type : "GET",
                dataType : "json",
                success : makeMemberList,
                error : function () {alert('error');}
            });
        }
        function makeMemberList(data) {
            var memberListHtml = "";
            $.each(data, function (index, obj) {
                memberListHtml += "<li>";
                memberListHtml += "<a style=' height: 30px; font-size: 15px; text-align: center' href='javascript:subjectList("+obj.memIdx+")'>" + obj.memName + "</a>";
                memberListHtml += "</li>";
            })
            $("#memberList").html(memberListHtml);
        }

        function oldMemberList() {
            $.ajax({
                url : "/oldMemberList.do",
                type : "GET",
                dataType : "json",
                success : makeOldMemberList,
                error : function () {alert('error');}
            });
        }
        function makeOldMemberList(data) {
            var oldMemberListHtml = "";
            $.each(data, function (index, obj) {
                oldMemberListHtml += "<li>";
                oldMemberListHtml += "<a style=' height: 30px; font-size: 15px; text-align: center' href='javascript:subjectList("+obj.memIdx+")'>" + obj.memName + "</a>";
                oldMemberListHtml += "</li>";
            })
            $("#oldMemberList").html(oldMemberListHtml);
        }
        function subjectList(memIdx) {
            location.href="${contextPath}/subjectList.do?memIdx="+memIdx;
        }

        function allLabnoteList() {
            location.href="${contextPath}/allLabnoteList.do";
        }

        function searchForm() {
            var searchFormHtml = "";

            // MainController에다가 allSearchList.do 구현해줘야 함
            searchFormHtml += "<li style='height: 45px'>";
            searchFormHtml += '<form action="/allSearchList.do" method="post" >';
            searchFormHtml += '<table class="table table-hover">';
            searchFormHtml += '<tr>';
            searchFormHtml += '<td>';
            searchFormHtml += '<input type="text" name="searchContent" class="form-control" style="width: 200px">';
            searchFormHtml += '</td>';
            searchFormHtml += '<td>';
            searchFormHtml += '<input type="submit" class="btn btn-danger" value="검색">';
            searchFormHtml += '</td>';
            searchFormHtml += '</tr>';
            searchFormHtml += '</table>';
            searchFormHtml += '</form>';
            searchFormHtml += "</li>";

            $("#searchForm").html(searchFormHtml);
        }
    </script>

    <style>
        .dropdown:hover .dropdown-menu {
            display: block;
        }
    </style>

</head>
<body>

<nav class="navbar" style="background-color: black">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${contextPath}/">연구실 홈</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown"> 게시판 <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="${contextPath}/noticeList.do">공지사항</a> </li>
                    <li><a href="${contextPath}/shopList.do">비용 관리</a> </li>
                    <li><a href="${contextPath}/freeList.do">자유게시판</a> </li>
                    <li><a href="${contextPath}/manualList.do">메뉴얼</a> </li>
                    <li><a href="${contextPath}/linkList.do">링크</a></li>
                    <li><a href="${contextPath}/allMemberList.do">구성원</a></li>
                </ul>
            </li>
            <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" onclick="allLabnoteList()"> 랩노트 <span class="caret"></span></a>
                <ul class="dropdown-menu" id="memberList">
                </ul>
            </li>
            <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" onclick="oldMemberList()"> 전멤버 <span class="caret"></span></a>
                <ul class="dropdown-menu" id="oldMemberList">
                </ul>
            </li>
            <!-- 권한이 교수인 경우에만 이용할 수 있는 회원관리 페이지 -->
            <c:if test="${!empty mvo}">
                <c:if test="${mvo.memAuth eq '교수' || mvo.memAuth eq '관리자'}">
                    <li><a href="${contextPath}/memAdmin.do"><span class="glyphicon glyphicon-user"></span> 회원관리 </a></li>
                </c:if>
            </c:if>
            <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" onclick="searchForm()"> <span class="glyphicon glyphicon-search"></span> 검색 </a>
                <ul class="dropdown-menu" id="searchForm">
                </ul>
            </li>
        </ul>

        <!-- 로그인을 하지 않을 경우 ui -->
        <c:if test="${empty mvo}">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${contextPath}/memLoginForm.do"><span class="glyphicon glyphicon-log-in"></span> Login </a></li>
                <li><a href="${contextPath}/memJoinForm.do"><span class="glyphicon glyphicon-user"></span> Sign Up </a></li>
            </ul>
        </c:if>

        <!-- 로그인을 했을 때의 ui -->
        <c:if test="${!empty mvo}">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${contextPath}/memUpdateForm.do"><span class="glyphicon glyphicon-refresh"></span> 회원정보수정</a></li>
                <li><a href="${contextPath}/memLogout.do"><span class="glyphicon glyphicon-log-out"></span> 로그아웃</a></li>
                <c:if test="${empty mvo.memProfile}">
                    <li style="color: white; margin-right: 10px">
                        <img src="../../../images/20220911.png" style="width: 50px; height: 50px" class="img-circle"/> ${mvo.memName}님(${mvo.memAuth})
                    </li>
                </c:if>
                <c:if test="${!empty mvo.memProfile}">
                    <li style="color: white; margin-right: 10px">
                        <img src="../../../member_profile/${mvo.memProfile}" style="width: 50px; height: 50px" class="img-circle"/> ${mvo.memName}님(${mvo.memAuth})
                    </li>
                </c:if>
        </c:if>
    </div>
</nav>


</body>
</html>

















