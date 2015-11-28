var T, baidu = T = baidu || {
        version: "1.5.0"
    };
baidu.guid = "$BAIDU$";
window[baidu.guid] = window[baidu.guid] || {};
baidu.flash = baidu.flash || {};
baidu.dom = baidu.dom || {};
baidu.dom.g = function (a) {
    if ("string" == typeof a || a instanceof String) {
        return document.getElementById(a)
    } else {
        if (a && a.nodeName && (a.nodeType == 1 || a.nodeType == 9)) {
            return a
        }
    }
    return null
};
baidu.g = baidu.G = baidu.dom.g;
baidu.array = baidu.array || {};
baidu.each = baidu.array.forEach = baidu.array.each = function (g, e, b) {
    var d, f, c, a = g.length;
    if ("function" == typeof e) {
        for (c = 0; c < a; c++) {
            f = g[c];
            d = e.call(b || g, f, c);
            if (d === false) {
                break
            }
        }
    }
    return g
};
baidu.lang = baidu.lang || {};
baidu.lang.isFunction = function (a) {
    return "[object Function]" == Object.prototype.toString.call(a)
};
baidu.lang.isString = function (a) {
    return "[object String]" == Object.prototype.toString.call(a)
};
baidu.isString = baidu.lang.isString;
baidu.swf = baidu.swf || {};
baidu.swf.version = (function () {
    var h = navigator;
    if (h.plugins && h.mimeTypes.length) {
        var d = h.plugins["Shockwave Flash"];
        if (d && d.description) {
            return d.description.replace(/([a-zA-Z]|\s)+/, "").replace(/(\s)+r/, ".") + ".0"
        }
    } else {
        if (window.ActiveXObject && !window.opera) {
            for (var b = 12; b >= 2; b--) {
                try {
                    var g = new ActiveXObject("ShockwaveFlash.ShockwaveFlash." + b);
                    if (g) {
                        var a = g.GetVariable("$version");
                        return a.replace(/WIN/g, "").replace(/,/g, ".")
                    }
                } catch (f) {}
            }
        }
    }
})();
baidu.string = baidu.string || {};
baidu.string.encodeHTML = function (a) {
    return String(a).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#39;")
};
baidu.encodeHTML = baidu.string.encodeHTML;
baidu.swf.createHTML = function (s) {
    s = s || {};
    var j = baidu.swf.version,
        g = s.ver || "6.0.0",
        f, d, e, c, h, r, a = {}, o = baidu.string.encodeHTML;
    for (c in s) {
        a[c] = s[c]
    }
    s = a;
    if (j) {
        j = j.split(".");
        g = g.split(".");
        for (e = 0; e < 3; e++) {
            f = parseInt(j[e], 10);
            d = parseInt(g[e], 10);
            if (d < f) {
                break
            } else {
                if (d > f) {
                    return ""
                }
            }
        }
    } else {
        return ""
    }
    var m = s.vars,
        l = ["classid", "codebase", "id", "width", "height", "align"];
    s.align = s.align || "middle";
    s.classid = "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000";
    s.codebase = "http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0";
    s.movie = s.url || "";
    delete s.vars;
    delete s.url;
    if ("string" == typeof m) {
        s.flashvars = m
    } else {
        var p = [];
        for (c in m) {
            r = m[c];
            p.push(c + "=" + encodeURIComponent(r))
        }
        s.flashvars = p.join("&")
    }
    var n = ["<object "];
    for (e = 0, h = l.length; e < h; e++) {
        r = l[e];
        n.push(" ", r, '="', o(s[r]), '"')
    }
    n.push(">");
    var b = {
        wmode: 1,
        scale: 1,
        quality: 1,
        play: 1,
        loop: 1,
        menu: 1,
        salign: 1,
        bgcolor: 1,
        base: 1,
        allowscriptaccess: 1,
        allownetworking: 1,
        allowfullscreen: 1,
        seamlesstabbing: 1,
        devicefont: 1,
        swliveconnect: 1,
        flashvars: 1,
        movie: 1
    };
    for (c in s) {
        r = s[c];
        c = c.toLowerCase();
        if (b[c] && (r || r === false || r === 0)) {
            n.push('<param name="' + c + '" value="' + o(r) + '" />')
        }
    }
    s.src = s.movie;
    s.name = s.id;
    delete s.id;
    delete s.movie;
    delete s.classid;
    delete s.codebase;
    s.type = "application/x-shockwave-flash";
    s.pluginspage = "http://www.macromedia.com/go/getflashplayer";
    n.push("<embed");
    var q;
    for (c in s) {
        r = s[c];
        if (r || r === false || r === 0) {
            if ((new RegExp("^salign\x24", "i")).test(c)) {
                q = r;
                continue
            }
            n.push(" ", c, '="', o(r), '"')
        }
    }
    if (q) {
        n.push(' salign="', o(q), '"')
    }
    n.push("></embed></object>");
    return n.join("")
};
baidu.swf.create = function (a, c) {
    a = a || {};
    var b = baidu.swf.createHTML(a) || a.errorMessage || "";
    if (c && "string" == typeof c) {
        c = document.getElementById(c)
    }
    if (c) {
        c.innerHTML = b
    } else {
        document.write(b)
    }
};
baidu.browser = baidu.browser || {};
baidu.browser.ie = baidu.ie = /msie (\d+\.\d+)/i.test(navigator.userAgent) ? (document.documentMode || +RegExp["\x241"]) : undefined;
baidu.array.remove = function (c, b) {
    var a = c.length;
    while (a--) {
        if (a in c && c[a] === b) {
            c.splice(a, 1)
        }
    }
    return c
};
baidu.lang.isArray = function (a) {
    return "[object Array]" == Object.prototype.toString.call(a)
};
baidu.lang.toArray = function (b) {
    if (b === null || b === undefined) {
        return []
    }
    if (baidu.lang.isArray(b)) {
        return b
    }
    if (typeof b.length !== "number" || typeof b === "string" || baidu.lang.isFunction(b)) {
        return [b]
    }
    if (b.item) {
        var a = b.length,
            c = new Array(a);
        while (a--) {
            c[a] = b[a]
        }
        return c
    }
    return [].slice.call(b)
};
baidu.swf.getMovie = function (c) {
    var a = document[c],
        b;
    return baidu.browser.ie == 9 ? a && a.length ? (b = baidu.array.remove(baidu.lang.toArray(a), function (d) {
        return d.tagName.toLowerCase() != "embed"
    })).length == 1 ? b[0] : b : a : a || window[c]
};
baidu.flash._Base = (function () {
    var d = "bd__flash__";

    function b() {
        return d + Math.floor(Math.random() * 2147483648).toString(36)
    }
    function a(g) {
        if (typeof g !== "undefined" && typeof g.flashInit !== "undefined" && g.flashInit()) {
            return true
        } else {
            return false
        }
    }
    function c(h, i) {
        var g = null;
        h = h.reverse();
        baidu.each(h, function (j) {
            g = i.call(j.fnName, j.params);
            j.callBack(g)
        })
    }
    function f(g) {
        var h = "";
        if (baidu.lang.isFunction(g)) {
            h = b();
            window[h] = function () {
                g.apply(window, arguments)
            };
            return h
        } else {
            if (baidu.lang.isString) {
                return g
            }
        }
    }
    function e(h) {
        if (!h.id) {
            h.id = b()
        }
        var g = h.container || "";
        delete(h.container);
        baidu.swf.create(h, g);
        return baidu.swf.getMovie(h.id)
    }
    return function (q, i) {
        var n = this,
            h = (typeof q.autoRender !== "undefined" ? q.autoRender : true),
            p = q.createOptions || {}, l = null,
            m = false,
            k = [],
            g = null,
            i = i || [];
        n.render = function () {
            l = e(p);
            if (i.length > 0) {
                baidu.each(i, function (s, r) {
                    i[r] = f(q[s] || new Function())
                })
            }
            n.call("setJSFuncName", [i])
        };
        n.isReady = function () {
            return m
        };
        n.call = function (u, t, s) {
            if (!u) {
                return
            }
            s = s || new Function();
            var r = null;
            if (m) {
                r = l.call(u, t);
                s(r)
            } else {
                k.push({
                    fnName: u,
                    params: t,
                    callBack: s
                });
                (!g) && (g = setInterval(j, 200))
            }
        };
        n.createFunName = function (r) {
            return f(r)
        };

        function j() {
            if (a(l)) {
                clearInterval(g);
                g = null;
                o();
                m = true
            }
        }
        function o() {
            c(k, l);
            k = []
        }
        h && n.render()
    }
})();
baidu.flash.imageUploader = baidu.flash.imageUploader || function (a) {
    var b = this,
        a = a || {}, c = new baidu.flash._Base(a, ["selectFileCallback", "exceedFileCallback", "deleteFileCallback", "startUploadCallback", "uploadCompleteCallback", "uploadErrorCallback", "allCompleteCallback", "changeFlashHeight"]);
    b.upload = function () {
        c.call("upload")
    };
    b.pause = function () {
        c.call("pause")
    }
};