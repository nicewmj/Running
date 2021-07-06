// SetCharts类
function SetCharts() {
    this.name = new Array(['已下单', '待处理', '处理中', '处理成功'])
    this.default_num = 4
}

// 设置各节名称
SetCharts.prototype.setName = function (name_arr) {
    if (typeof(name_arr) === 'object') {
        this.name = []
        for (var i = 0; i < name_arr.length; i++) {
            this.name.push(name_arr[i])
        }
    } else {
        console.error("节点名称类型错误，应为'object',读取到'" + typeof(name_arr) + "'")
    }
    return this
}

// 设置节点数量
SetCharts.prototype.setNum = function (num) {
    this.default_num = num
    return this
}

// 改变状态
SetCharts.prototype.setState = function (num) {
    $('.stepss').removeClass('bg-messages')
    // 清除状态
    for (var i = 0; i <= num; i++) {
        if (i == 0) {
            $('.stepss:nth-of-type(' + (i + 1) + ')').addClass('bg-messages')
        } else {
            $('.stepss:nth-of-type(' + i + ')').addClass('bg-messages')
        }
    }
    return this
}

// 改变状态
SetCharts.prototype.setState2 = function (num) {
    $('.stepss').removeClass('bg-messages')
    // 清除状态
    for (var i = 0; i <= num; i++) {
        $('.stepss:nth-of-type(' + (i + 1) + ')').addClass('bg-messages')
    }
    return this
}

// 设置Step函数,如果未传入num则默认为4
SetCharts.prototype.setStep = function (num) {
    // 清除已有
    $('.flow-chart').empty()
    // 生成
    this.default_num = num == undefined ? this.default_num : num
    var step_str = ""
    for (i = 0; i < this.default_num; i++) {
        step_str += '<div id="step' + i + '" class="stepss bg-messages_gray min"><p>' + this.name[0][i] + '</p></div>'
    }
    $(".flow-chart").append(step_str);
    return this
}

// 全局和注册
var charts = new SetCharts();
