(function ($) {
    var rotateLeft = function (lValue, iShiftBits) {
        return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
    };
    var addUnsigned = function (lX, lY) {
        var lX4, lY4, lX8, lY8, lResult;
        lX8 = (lX & 0x80000000);
        lY8 = (lY & 0x80000000);
        lX4 = (lX & 0x40000000);
        lY4 = (lY & 0x40000000);
        lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
        if (lX4 & lY4) return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
        if (lX4 | lY4) {
            if (lResult & 0x40000000) return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
            else return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
        } else {
            return (lResult ^ lX8 ^ lY8);
        }
    };
    var F = function (x, y, z) {
        return (x & y) | ((~x) & z);
    };
    var G = function (x, y, z) {
        return (x & z) | (y & (~z));
    };
    var H = function (x, y, z) {
        return (x ^ y ^ z);
    };
    var I = function (x, y, z) {
        return (y ^ (x | (~z)));
    };
    var FF = function (a, b, c, d, x, s, ac) {
        a = addUnsigned(a, addUnsigned(addUnsigned(F(b, c, d), x), ac));
        return addUnsigned(rotateLeft(a, s), b);
    };
    var GG = function (a, b, c, d, x, s, ac) {
        a = addUnsigned(a, addUnsigned(addUnsigned(G(b, c, d), x), ac));
        return addUnsigned(rotateLeft(a, s), b);
    };
    var HH = function (a, b, c, d, x, s, ac) {
        a = addUnsigned(a, addUnsigned(addUnsigned(H(b, c, d), x), ac));
        return addUnsigned(rotateLeft(a, s), b);
    };
    var II = function (a, b, c, d, x, s, ac) {
        a = addUnsigned(a, addUnsigned(addUnsigned(I(b, c, d), x), ac));
        return addUnsigned(rotateLeft(a, s), b);
    };
    var convertToWordArray = function (string) {
        var lWordCount;
        var lMessageLength = string.length;
        var lNumberOfWordsTempOne = lMessageLength + 8;
        var lNumberOfWordsTempTwo = (lNumberOfWordsTempOne - (lNumberOfWordsTempOne % 64)) / 64;
        var lNumberOfWords = (lNumberOfWordsTempTwo + 1) * 16;
        var lWordArray = Array(lNumberOfWords - 1);
        var lBytePosition = 0;
        var lByteCount = 0;
        while (lByteCount < lMessageLength) {
            lWordCount = (lByteCount - (lByteCount % 4)) / 4;
            lBytePosition = (lByteCount % 4) * 8;
            lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
            lByteCount++;
        }
        lWordCount = (lByteCount - (lByteCount % 4)) / 4;
        lBytePosition = (lByteCount % 4) * 8;
        lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
        lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
        lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
        return lWordArray;
    };
    var wordToHex = function (lValue) {
        var WordToHexValue = "",
            WordToHexValueTemp = "",
            lByte, lCount;
        for (lCount = 0; lCount <= 3; lCount++) {
            lByte = (lValue >>> (lCount * 8)) & 255;
            WordToHexValueTemp = "0" + lByte.toString(16);
            WordToHexValue = WordToHexValue + WordToHexValueTemp.substr(WordToHexValueTemp.length - 2, 2);
        }
        return WordToHexValue;
    };
    var uTF8Encode = function (string) {
        string = string.replace(/\x0d\x0a/g, "\x0a");
        var output = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                output += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                output += String.fromCharCode((c >> 6) | 192);
                output += String.fromCharCode((c & 63) | 128);
            } else {
                output += String.fromCharCode((c >> 12) | 224);
                output += String.fromCharCode(((c >> 6) & 63) | 128);
                output += String.fromCharCode((c & 63) | 128);
            }
        }
        return output;
    };
    $.md5 = function (string) {
        var x = Array();
        var k, AA, BB, CC, DD, a, b, c, d;
        var S11 = 7,
            S12 = 12,
            S13 = 17,
            S14 = 22;
        var S21 = 5,
            S22 = 9,
            S23 = 14,
            S24 = 20;
        var S31 = 4,
            S32 = 11,
            S33 = 16,
            S34 = 23;
        var S41 = 6,
            S42 = 10,
            S43 = 15,
            S44 = 21;
        string = uTF8Encode(string);
        x = convertToWordArray(string);
        a = 0x67452301;
        b = 0xEFCDAB89;
        c = 0x98BADCFE;
        d = 0x10325476;
        for (k = 0; k < x.length; k += 16) {
            AA = a;
            BB = b;
            CC = c;
            DD = d;
            a = FF(a, b, c, d, x[k + 0], S11, 0xD76AA478);
            d = FF(d, a, b, c, x[k + 1], S12, 0xE8C7B756);
            c = FF(c, d, a, b, x[k + 2], S13, 0x242070DB);
            b = FF(b, c, d, a, x[k + 3], S14, 0xC1BDCEEE);
            a = FF(a, b, c, d, x[k + 4], S11, 0xF57C0FAF);
            d = FF(d, a, b, c, x[k + 5], S12, 0x4787C62A);
            c = FF(c, d, a, b, x[k + 6], S13, 0xA8304613);
            b = FF(b, c, d, a, x[k + 7], S14, 0xFD469501);
            a = FF(a, b, c, d, x[k + 8], S11, 0x698098D8);
            d = FF(d, a, b, c, x[k + 9], S12, 0x8B44F7AF);
            c = FF(c, d, a, b, x[k + 10], S13, 0xFFFF5BB1);
            b = FF(b, c, d, a, x[k + 11], S14, 0x895CD7BE);
            a = FF(a, b, c, d, x[k + 12], S11, 0x6B901122);
            d = FF(d, a, b, c, x[k + 13], S12, 0xFD987193);
            c = FF(c, d, a, b, x[k + 14], S13, 0xA679438E);
            b = FF(b, c, d, a, x[k + 15], S14, 0x49B40821);
            a = GG(a, b, c, d, x[k + 1], S21, 0xF61E2562);
            d = GG(d, a, b, c, x[k + 6], S22, 0xC040B340);
            c = GG(c, d, a, b, x[k + 11], S23, 0x265E5A51);
            b = GG(b, c, d, a, x[k + 0], S24, 0xE9B6C7AA);
            a = GG(a, b, c, d, x[k + 5], S21, 0xD62F105D);
            d = GG(d, a, b, c, x[k + 10], S22, 0x2441453);
            c = GG(c, d, a, b, x[k + 15], S23, 0xD8A1E681);
            b = GG(b, c, d, a, x[k + 4], S24, 0xE7D3FBC8);
            a = GG(a, b, c, d, x[k + 9], S21, 0x21E1CDE6);
            d = GG(d, a, b, c, x[k + 14], S22, 0xC33707D6);
            c = GG(c, d, a, b, x[k + 3], S23, 0xF4D50D87);
            b = GG(b, c, d, a, x[k + 8], S24, 0x455A14ED);
            a = GG(a, b, c, d, x[k + 13], S21, 0xA9E3E905);
            d = GG(d, a, b, c, x[k + 2], S22, 0xFCEFA3F8);
            c = GG(c, d, a, b, x[k + 7], S23, 0x676F02D9);
            b = GG(b, c, d, a, x[k + 12], S24, 0x8D2A4C8A);
            a = HH(a, b, c, d, x[k + 5], S31, 0xFFFA3942);
            d = HH(d, a, b, c, x[k + 8], S32, 0x8771F681);
            c = HH(c, d, a, b, x[k + 11], S33, 0x6D9D6122);
            b = HH(b, c, d, a, x[k + 14], S34, 0xFDE5380C);
            a = HH(a, b, c, d, x[k + 1], S31, 0xA4BEEA44);
            d = HH(d, a, b, c, x[k + 4], S32, 0x4BDECFA9);
            c = HH(c, d, a, b, x[k + 7], S33, 0xF6BB4B60);
            b = HH(b, c, d, a, x[k + 10], S34, 0xBEBFBC70);
            a = HH(a, b, c, d, x[k + 13], S31, 0x289B7EC6);
            d = HH(d, a, b, c, x[k + 0], S32, 0xEAA127FA);
            c = HH(c, d, a, b, x[k + 3], S33, 0xD4EF3085);
            b = HH(b, c, d, a, x[k + 6], S34, 0x4881D05);
            a = HH(a, b, c, d, x[k + 9], S31, 0xD9D4D039);
            d = HH(d, a, b, c, x[k + 12], S32, 0xE6DB99E5);
            c = HH(c, d, a, b, x[k + 15], S33, 0x1FA27CF8);
            b = HH(b, c, d, a, x[k + 2], S34, 0xC4AC5665);
            a = II(a, b, c, d, x[k + 0], S41, 0xF4292244);
            d = II(d, a, b, c, x[k + 7], S42, 0x432AFF97);
            c = II(c, d, a, b, x[k + 14], S43, 0xAB9423A7);
            b = II(b, c, d, a, x[k + 5], S44, 0xFC93A039);
            a = II(a, b, c, d, x[k + 12], S41, 0x655B59C3);
            d = II(d, a, b, c, x[k + 3], S42, 0x8F0CCC92);
            c = II(c, d, a, b, x[k + 10], S43, 0xFFEFF47D);
            b = II(b, c, d, a, x[k + 1], S44, 0x85845DD1);
            a = II(a, b, c, d, x[k + 8], S41, 0x6FA87E4F);
            d = II(d, a, b, c, x[k + 15], S42, 0xFE2CE6E0);
            c = II(c, d, a, b, x[k + 6], S43, 0xA3014314);
            b = II(b, c, d, a, x[k + 13], S44, 0x4E0811A1);
            a = II(a, b, c, d, x[k + 4], S41, 0xF7537E82);
            d = II(d, a, b, c, x[k + 11], S42, 0xBD3AF235);
            c = II(c, d, a, b, x[k + 2], S43, 0x2AD7D2BB);
            b = II(b, c, d, a, x[k + 9], S44, 0xEB86D391);
            a = addUnsigned(a, AA);
            b = addUnsigned(b, BB);
            c = addUnsigned(c, CC);
            d = addUnsigned(d, DD);
        }
        var tempValue = wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d);
        return tempValue.toLowerCase();
    }

})(window.MD5 = {});

(function (owner) {
    var CONNECTION_TYPE = {};
    //service接口地址
    // owner.baseUrl = "http://36.148.23.209:8081/FRAUD_SERVICE/api/rest/";//k8s测试
    //owner.baseUrlService = "http://134.195.1.214:12080/FRAUD_SERVICE_TEST/api/rest/";//预上线
    //owner.baseUrl = "http://localhost:8083/FRAUD_SERVICE/api/rest/";//本地
    owner.baseUrl = "http://localhost:9010/FRAUD_SERVICE/api/rest/";//本地

    /*
     * 生成随机串
     */
    owner.randomUUID = function () {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";
        return s.join("");
    };

    owner.encryptedPwd = function (pwd) {
        var split_pwd = pwd.split('');
        var new_pwd = '';
        for (var i = pwd.length - 1; i >= 0; i--) {
            new_pwd += split_pwd[i];
        }
        return RSAUtils.encryptedString(new_pwd);
    };

    owner.sign = function (para) {
        var arr = [];
        for (var p in para) {
            arr.push(p);
        }
        arr.sort();
        var s = String();
        for (var i = 0; i < arr.length; i++) {
            s = s + arr[i] + para[arr[i]];
        }
        s = "f092a10dc2399f56ddea88cdad3241bd" + s + "f092a10dc2399f56ddea88cdad3241bd";
        s = MD5.md5(s);
        para["sign"] = s.toUpperCase();
        return para;
    };

    owner.alert = function (msg) {
        $.Huimodalalert(msg, 2000);
    };

    owner.getPage = function (isIndex, data) {
        var that = this;
        $.ajax({
            url: data.url,
            dataType: 'text',
            xhrFields: {
                withCredentials: true
            },
            data: data.param ? data.param : {},
            success: function (res) {
                if (isIndex) {
                    $(".content-page-main").css("background", "none");
                    $(".page-main").show();
                    $('.page-main').empty();
                    $('.page-main').append(res);
                    sessionStorage.setItem('history', data.history);
                } else {
                    data['content'] = res;
                    that.openModal(data);
                }
            }
        });
    };

    owner.locate = function (url, data) {
        var loginUser = JSON.parse(sessionStorage.getItem("loginUser"));
        if (!loginUser) {
            location.href = 'error/noLogin.html';
            return;
        }

        var his = '';
        if (url.indexOf('fnc=back') < 0) {
            var history = sessionStorage.getItem('history');
            if (history && history != 'undefined') {
                his = history + ',' + url;
            } else {
                his = url;
            }
        } else {
            his = url.split("?")[0];
        }
        var msg = {
            param: data,
            url: url,
            history: his
        };
        this.getPage(true, msg);
    };

    owner.back = function () {
        var his = sessionStorage.getItem('history');
        var historys = his.split(',');
        var url = historys[historys.length - 2] + '?fnc=back';
        this.locate(url);
    };
    owner.openModal = function (opt) {
        var hasTitle = opt.hasTitle ? opt.hasTitle : true;
        var hasFooter = opt.hasFooter ? opt.hasFooter : true;
        var modalId = opt.modalId ? opt.modalId : 'modal';

        $("#" + modalId).remove();
        var modal = '<div id="' + modalId + '" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content radius">';
        var title = '<div class="modal-header">'
            + '<h3 class="modal-title">' + opt.title + '</h3>'
            + '<a class="close" data-dismiss="modal" aria-hidden="true" href="javascript:;">×</a>'
            + '</div>';
        if (hasTitle) {
            modal += title;
        }

        var body = '<div class="modal-body">' + opt.content + '</div>';
        modal += body;

        var footer = '<div class="modal-footer">'
            + '<button type="button" class="btn btn-primary">确定</button>'
            + '<button class="btn btn-cancel" data-dismiss="modal" aria-hidden="true">关闭</button>'
            + '</div>';
        if (hasFooter) {
            modal += footer;
        }

        modal += '</div></div></div>';

        $('body').append(modal);

        if (opt.initFn) {
            opt.initFn();
        }

        if (opt.form) {
            sessionStorage.setItem('form', opt.form);
        }
        if (opt.row) {
            for (var key in opt.row) {
                var isSelect = $('#' + opt.form + ' select#' + key).length > 0 ? true : false;
                if (isSelect) {
                    $('#' + opt.form + ' #' + key + " option").each(function () {
                        if (opt.row[key] == $(this).val()) {
                            $(this).attr("selected", true);
                            return false;
                        }
                    });
                } else {
                    $('#' + opt.form + ' #' + key).val(opt.row[key]);
                }
            }
            if (opt.row_spe) {
                opt.row_spe_func(opt.row_spe);
            }
        }

        var confirmBtnFunc = opt.confirmBtnFunc ? opt.confirmBtnFunc : function () {
            $('#' + opt.form).submit();
        };
        var cancelBtnFunc = opt.cancelBtnFunc ? opt.cancelBtnFunc : null;
        $('#' + modalId + ' .btn-primary').click(confirmBtnFunc);
        if (cancelBtnFunc) {
            $('#' + modalId + ' .btn-cancel').click(cancelBtnFunc);
        }
        $('#' + modalId).modal("show");
    };

    owner.openModals = function (opt) {
        var hasTitle = opt.hasTitle ? opt.hasTitle : true;
        var hasFooter = opt.hasFooter ? opt.hasFooter : true;
        var modalId = opt.modalId ? opt.modalId : 'modal';

        $("#" + modalId).remove();
        var modal = '<div id="' + modalId + '" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content radius">';
        var title = '<div class="modal-header">'
            + '<h3 class="modal-title">' + opt.title + '</h3>'
            + '</div>';
        if (hasTitle) {
            modal += title;
        }

        var body = '<div class="modal-body">' + opt.content + '</div>';
        modal += body;

        var footer = '<div class="modal-footer">'
            + '<button type="button" class="btn btn-primary">确定</button>'
            + '<button class="btn btn-cancel" data-dismiss="modal" aria-hidden="true">关闭</button>'
            + '</div>';
        if (hasFooter) {
            modal += footer;
        }

        modal += '</div></div></div>';

        $('body').append(modal);

        if (opt.initFn) {
            opt.initFn();
        }

        if (opt.form) {
            sessionStorage.setItem('form', opt.form);
        }
        if (opt.row) {
            for (var key in opt.row) {
                var isSelect = $('#' + opt.form + ' select#' + key).length > 0 ? true : false;
                if (isSelect) {
                    $('#' + opt.form + ' #' + key + " option").each(function () {
                        if (opt.row[key] == $(this).val()) {
                            $(this).attr("selected", true);
                            return false;
                        }
                    });
                } else {
                    $('#' + opt.form + ' #' + key).val(opt.row[key]);
                }
            }
            if (opt.row_spe) {
                opt.row_spe_func(opt.row_spe);
            }
        }

        var confirmBtnFunc = opt.confirmBtnFunc ? opt.confirmBtnFunc : function () {
            $('#' + opt.form).submit();
        };
        var cancelBtnFunc = opt.cancelBtnFunc ? opt.cancelBtnFunc : null;
        $('#' + modalId + ' .btn-primary').click(confirmBtnFunc);
        if (cancelBtnFunc) {
            $('#' + modalId + ' .btn-cancel').click(cancelBtnFunc);
        }
        $('#' + modalId).modal("show");
    };

    owner.alertDialog = function (opt) {
        opt['modalId'] = 'alertDialog';
        this.openModal(opt);
    };

    owner.hideAlertDialog = function () {
        $('#alertDialog').modal('hide');
        $('#alertDialog').remove();
    };

    owner.hideModal = function (id) {
        var modalId = id ? id : 'modal';
        $('#' + modalId).modal('hide');
    };

    owner.getConfig = function (id) {
        var jsonStr = sessionStorage.getItem(id);
        return JSON.parse(jsonStr);
    };

    owner.showLoading = function () {
        $("body").mLoading("show");
    };

    owner.hideLoading = function () {
        $("body").mLoading("hide");
    };

    owner.num = function (obj) {
        obj.value = obj.value.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
        obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字
        obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
    }
}(window.base = {}));

$(function () {
    $.namespace = function () {
        var a = arguments, o = null, i, j, d;
        for (i = 0; i < a.length; i = i + 1) {
            d = a[i].split(".");
            o = window;
            for (j = 0; j < d.length; j = j + 1) {
                o[d[j]] = o[d[j]] || {};
                o = o[d[j]];
            }
        }
        return o;
    };
});

(function () {
    var originalUrl = window.location.href,
        toUrl = originalUrl.indexOf('#') != -1 && originalUrl.split('#')[1],
        reg = /^[\w\?&=\/]*$/;

    if (toUrl && reg.test(toUrl)) {
        window.location.href = toUrl;
    }
})();