<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.board.dao.*" %>
<%@ page import="com.board.dto.*" %>
<%
    request.setCharacterEncoding("utf-8");

    // 양식에 입력되었던 값 읽기

%>
        <script>
            alert('모든 항목이 빈칸 없이 입력되어야 합니다.');
            history.back();
        </script>
<%
    // 목록보기 화면으로 돌아감
    response.sendRedirect("list.jsp");
%>

