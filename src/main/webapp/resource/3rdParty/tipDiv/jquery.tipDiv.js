/***
* Guo MengWen ��ʾ��Jquery���
* ��дʱ�䣺2013��8��25��
* version:jquery.tipDiv.js
***/
$(function () {
    $.fn.TipDiv = function (options) {
        var defaults = {
            Event: "mouseover", //������Ӧ�¼�
            html: ""//��ʾ����ʾ��html
        };
        var options = $.extend(defaults, options);
        var bid = parseInt(Math.random() * 100000);
        $("body").prepend("<div id='Tip" + bid + "' class='tipDivDialog'>" + options.html + "</div>");
        var $this = $(this);
        var $tipdiv = $("#Tip" + bid);
        $this.die().live(options.Event, function () {
            var h = $(this).height();
            var offset = $(this).offset();
            var docheight = $(document).height();
            if (docheight - offset.top > $tipdiv.height()) {
                $tipdiv.css({
                    "left": offset.left-40,
                    "top": offset.top + h
                }).show();
            } else {
                $tipdiv.css({
                    "left": offset.left - 40,
                    "top": offset.top - $tipdiv.height()
                }).show();
            }
        });
        $tipdiv.die().live(options.Event, function () {
            $tipdiv.show();
        }).live("mouseout", function () {
            $tipdiv.hide();
        });
        $(document).click(function () {
            $tipdiv.hide();
        });
    }

});