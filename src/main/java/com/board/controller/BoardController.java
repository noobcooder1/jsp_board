package com.board.controller;

import com.board.dao.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class BoardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BoardController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = null;

        // URL에서 프로젝트 이름 뒷 부분의 문자열 얻어내기
        String uri = request.getRequestURI();
        String conPath = request.getContextPath();
        String com = uri.substring(conPath.length());

        // 주어진 URL에 따라 지정된 동작 수행
        if (com.equals("/list") || com.equals("/")) {
            request.setAttribute("msgList", new BoardDao().selectList());
            view = "list.jsp";

        } else if (com.equals("/view")){
            int num = Integer.parseInt(request.getParameter("num"));
            BoardDto dto = new BoardDao().selectOne(num, true);

            dto.setTitle(dto.getTitle().replace (" ",  "&nbsp;"));
            dto.setContent(dto.getContent().replace(" ",  "&nbsp;")
                    .replace("\n", "<br>"));

            request.setAttribute("msg", dto);
            view = "write.jsp";
        }

        // view에 담긴 문자열에 따라 포워딩 또는 리다이렉팅
        if (view.startsWith("redirect:")) {
            response.sendRedirect(view.substring(9));
        } else if (com.equals("/write")) {
            String tmp = request.getParameter("num");
            int num = (tmp != null && tmp.length() > 0)
                    ? Integer.parseInt(tmp) : 0;

            // 새 글쓰기 모드로 가정하고 변수값 설정
            BoardDto dto = new BoardDto();
            String action = "insert.jsp";

            // 글 번호가 주어졌으면 수정 모드
            if (num > 0) {
                dto = new BoardDao().selectOne(num, false);
                action = "update.jsp?num=" + num;
            }

            request.setAttribute("msg", dto);
            request.setAttribute("action", action);
            view = "write.jsp";
        }
        else if (com.equals("/insert")) {
            String writer = request.getParameter("writer");
            String title = request.getParameter("title");
            String content = request.getParameter("content");

            if (writer != null && writer.length() > 0 &&
                    title != null && title.length() > 0 &&
                    content != null && content.length() > 0) {

                BoardDto dto = new BoardDto();
                dto.setWriter(writer);
                dto.setTitle(title);
                dto.setContent(content);
                new BoardDao().insertOne(dto);

                view = "redirect:list";

            } else {
                request.setAttribute("errorMessage", "모든 항목이 비난 없이 입력되어야 합니다.");
                view = "errorBack.jsp";
            }

        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
