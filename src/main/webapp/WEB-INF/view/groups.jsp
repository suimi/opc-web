<%--
  Created by IntelliJ IDEA.
  User: cxkl
  Date: 2016-07-12
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <title>DCS.OPC.ZXQLSSN</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/sm.min.css">
    <script type='text/javascript' src='http://g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='http://g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
    <script>
        function read(groupId) {
            var form = document.getElementById("readForm");
            form.groupId.value = groupId;
            form.submit();
        }
    </script>
</head>
<body>

<div class="page-group">
    <div class="page page-current">
        <header class="bar bar-nav" style="height: 3.2rem">
            <h1 class="title" style="color:#ffa03f;font-size: 1.1rem">漳县公司DCS在线实时数据</h1>
            </br>
            <h6 class="title" style="color: #bebebe;font-size: .5rem">Sinoma.Qls.漳县公司工程师站</h6>
        </header>
        <div class="content">
            <div class="content-padded grid-demo">
                <c:forEach items="${servers}" var="server" varStatus="vs">
                    <div class="content-block-title">${server.define}</div>
                    <div class="list-block">
                        <ul>
                            <c:forEach items="${server.groups}" var="group" varStatus="vs">
                                <li>
                                    <a class="item-link item-content" onclick="javascript:window.open('<%= request.getContextPath()%>/opc/readData.do?groupId=${group.id}')"
                                       href="#">
                                        <div class="item-media"><i class="icon icon-f7"></i></div>
                                        <div class="item-inner">
                                            <div class="item-title" style="font-family: 黑体">${group.name}</div>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
        <form action="<%= request.getContextPath()%>/opc/readData.do" id="readForm" method="post">
            <input id="groupId" name="groupId" style="display: none"/>
        </form>
    </div>
</div>
</body>
</html>
