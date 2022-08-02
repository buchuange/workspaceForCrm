<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <title>Title</title>
    <base href="<%=basePath%>">
</head>
<body>
       $.ajax({
           url: "",
           data: {

           },
           type: "post",
           dataType: "json",
           success: function () {

           }
       })

       // 执行市场活动添加操作
       String id = UUIDUtil.getUUID();
       activity.setId(id);
       // 创建时间：当前系统时间
       String createTime = DateTimeUtil.getSysTime();
       activity.setCreateTime(createTime);
       // 创建人：当前登录用户
       String createBy = ((User) session.getAttribute("user")).getName();
       activity.setCreateBy(createBy);

       $.fn.datetimepicker.dates['zh-CN'] = {
       days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
       daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
       daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
       months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
       monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
       today: "今天",
       suffix: [],
       meridiem: ["上午", "下午"]
       };

       var rsc_bs_pag = {
       go_to_page_title: 'Go to page',
       rows_per_page_title: 'Rows per page',
       current_page_label: 'Page',
       current_page_abbr_label: 'p.',
       total_pages_label: 'of',
       total_pages_abbr_label: '/',
       total_rows_label: 'of',
       rows_info_records: 'records',
       go_top_text: '首页',
       go_prev_text: '上一页',
       go_next_text: '下一页',
       go_last_text: '末页'
       };

       $(".time").datetimepicker({
       minView: "month",
       language:  'zh-CN',
       format: 'yyyy-mm-dd',
       autoclose: true,
       todayBtn: true,
       pickerPosition: "bottom-left"
       });
</body>
</html>
