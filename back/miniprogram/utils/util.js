// utils/util.js - 工具函数

/** 格式化日期 */
function formatDate(date, fmt = 'yyyy-MM-dd HH:mm') {
  if (!date) return ''
  const d = new Date(date)
  const o = {
    'M+': d.getMonth() + 1,
    'd+': d.getDate(),
    'H+': d.getHours(),
    'm+': d.getMinutes(),
    's+': d.getSeconds()
  }
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, String(d.getFullYear()).substr(4 - RegExp.$1.length))
  for (const k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(String(o[k]).length))
    }
  }
  return fmt
}

/** 枪头状态 */
function getGunStatusInfo(status) {
  const map = {
    1: { name: '空闲', color: '#4CAF50', cls: 'tag-free' },
    2: { name: '占用', color: '#FF9800', cls: 'tag-busy' },
    3: { name: '故障', color: '#E91E63', cls: 'tag-fault' },
    4: { name: '离线', color: '#9E9E9E', cls: 'tag-offline' }
  }
  return map[status] || { name: '未知', color: '#999', cls: '' }
}

/** 订单状态 */
function getOrderStatusInfo(status) {
  const map = {
    101: { name: '已预约', color: '#2196F3' },
    102: { name: '已取消', color: '#9E9E9E' },
    103: { name: '充电中', color: '#FF9800' },
    104: { name: '已完成', color: '#4CAF50' }
  }
  return map[status] || { name: '未知', color: '#999' }
}

/** 电价时段类型 */
function getPeriodTypeName(type) {
  return ['', '尖', '峰', '平', '谷'][type] || '平'
}

/** 倒计时格式化 */
function formatCountdown(seconds) {
  if (seconds <= 0) return '已过期'
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

module.exports = { formatDate, getGunStatusInfo, getOrderStatusInfo, getPeriodTypeName, formatCountdown }
