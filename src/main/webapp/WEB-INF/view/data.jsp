<%--
  Created by IntelliJ IDEA.
  User: cxkl
  Date: 2016-07-12
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" buffer="none" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <META HTTP-EQUIV="pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <META HTTP-EQUIV="expires" CONTENT="0">
    <title>DCS.OPC.ZXQLSSN</title>
    <link rel="stylesheet" href="http://g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
    <script type='text/javascript' src='<%=request.getContextPath()%>/js/jquery.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/js/sui.min.js' charset='utf-8'></script>

    <script type='text/javascript' src='http://g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='http://g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
    <script>
        var cycleTime = 500;
        var times = 1;
        var totalTimes = 30 * 60;
        var intervalId;
        function updateData() {
            $.get("ajaxReadData.do", {groupId: "${data.groupId}"}, function (result) {
                $.each(result.items, function () {
                    var id = "#" + this.id.replace(".","_");
                    var value = "" + this.value + this.unit;
                    $(id).text(value)
                });
                $.each(result.subGroups, function () {
                    $.each(this.items, function () {
                        var id = "#" + this.id.replace(".","_");
                        var value = "" + this.value + this.unit;
                        $(id).text(value)
                    });
                });
            })
            times = times +1;
            if (times>=totalTimes) {
                clearInterval(intervalId);
            }
        }

        $(function () {
            intervalId = setInterval('updateData()', cycleTime);
        });
    </script>
</head>
<body>

<div class="page-group">
    <div class="page page-current">
        <header class="bar bar-nav">
            <a class="icon icon-left pull-left back"></a>
            <h1 class='title'>${data.groupName}运行参数</h1>
        </header>
        <div class="content">
            <div class="list-block" style="margin:0rem">
                <ul>
                    <li class="item-content">
                        <div class="item-media">序号</div>
                        <div class="item-inner" align="center">
                            <div class="item-title"></div>
                            <div class="item-title">参数</div>
                            <div class="item-after">数值</div>
                        </div>
                    </li>
                    <c:forEach items="${data.items}" var="item" varStatus="vs">
                        <li class="item-content" style="height: 1.65rem;min-height: 1.6rem;">
                            <div class="item-media"
                                 style="width: 1.2rem;padding-top: .25rem;padding-bottom: .2rem">${item.order}</div>
                            <div class="item-inner"
                                 style="min-height: 1.2rem;padding-top: .25rem;padding-bottom: .2rem">
                                <div class="item-title" style="font-family: 黑体;font-size: .8rem;">${item.define}</div>
                                <div class="item-after" style="font-family: 黑体;font-size: .8rem;"
                                     id='${fn:replace(item.id,".","_")}'>${item.value}${item.unit}</div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="content-padded grid-demo">
                <c:forEach items="${data.subGroups}" var="group" varStatus="vs">
                    <div class="content-block-title">${group.groupName}</div>
                    <div class="list-block">
                        <ul>
                            <li class="item-content">
                                <div class="item-media">序号</div>
                                <div class="item-inner" align="center">
                                    <div class="item-title"></div>
                                    <div class="item-title">参数</div>
                                    <div class="item-after">数值</div>
                                </div>
                            </li>
                            <c:forEach items="${group.items}" var="item" varStatus="vs">
                                <li class="item-content" style="height: 1.65rem;min-height: 1.6rem;">
                                    <div class="item-media"
                                         style="width: 1.2rem;padding-top: .25rem;padding-bottom: .2rem">${item.order}</div>
                                    <div class="item-inner"
                                         style="min-height: 1.2rem;padding-top: .25rem;padding-bottom: .2rem">
                                        <div class="item-title" style="font-family: 黑体;font-size: .8rem;">${item.define}</div>
                                        <div class="item-after" style="font-family: 黑体;font-size: .8rem;"
                                             id='${fn:replace(item.id,".","_")}'>${item.value}${item.unit}</div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</body>
</html>
