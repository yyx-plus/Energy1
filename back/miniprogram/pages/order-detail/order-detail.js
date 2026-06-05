const { get, del } = require('../../utils/request')
const { getOrderStatusInfo, formatCountdown } = require('../../utils/util')

Page({
  data: { order: null, countdown: 0, countdownText: '', timer: null },

  onLoad(options) {
    this.loadOrder(options.id)
  },

  onUnload() { if (this.data.timer) clearInterval(this.data.timer) },

  async loadOrder(id) {
    const res = await get(`/wx/order/${id}`)
    const order = { ...res.data, statusInfo: getOrderStatusInfo(res.data.status) }
    this.setData({ order })
    if (order.status === 101 && order.reserveRemainSeconds > 0) {
      this.startCountdown(order.reserveRemainSeconds)
    }
  },

  startCountdown(seconds) {
    this.setData({ countdown: seconds, countdownText: formatCountdown(seconds) })
    const timer = setInterval(() => {
      let { countdown } = this.data
      countdown--
      this.setData({ countdown, countdownText: formatCountdown(countdown) })
      if (countdown <= 0) clearInterval(timer)
    }, 1000)
    this.setData({ timer })
  },

  goCharging() {
    wx.navigateTo({ url: `/pages/charging/charging?orderId=${this.data.order.id}` })
  },

  goChargingActive() {
    // 充电中状态直接进入充电页，恢复充电动画
    wx.navigateTo({ url: `/pages/charging/charging?orderId=${this.data.order.id}&phase=charging` })
  },

  async onCancel() {
    wx.showModal({ title: '确认取消预约？', success: async (res) => {
      if (!res.confirm) return
      await del(`/wx/order/${this.data.order.id}/cancel`)
      wx.showToast({ title: '已取消', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 1500)
    }})
  },

  goPay() {
    wx.navigateTo({ url: `/pages/pay/pay?orderId=${this.data.order.id}` })
  }
})
