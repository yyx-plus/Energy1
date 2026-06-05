const { post } = require('../../utils/request')

Page({
  data: {
    stationId: null,
    gunId: null,
    reserveTime: '',
    loading: false
  },

  onLoad(options) {
    this.setData({ stationId: options.stationId, gunId: options.gunId })
    // 默认预约时间为当前时间
    const now = new Date()
    const fmt = `${now.getFullYear()}-${String(now.getMonth()+1).padStart(2,'0')}-${String(now.getDate()).padStart(2,'0')} ${String(now.getHours()).padStart(2,'0')}:${String(now.getMinutes()).padStart(2,'0')}`
    this.setData({ reserveTime: fmt })
  },

  async onConfirmReserve() {
    this.setData({ loading: true })
    try {
      const res = await post('/wx/order/reserve', {
        stationId: this.data.stationId,
        gunId: this.data.gunId
      })
      wx.showToast({ title: '预约成功', icon: 'success' })
      setTimeout(() => {
        wx.redirectTo({ url: `/pages/order-detail/order-detail?id=${res.data.orderId}` })
      }, 1500)
    } catch (e) {
    } finally {
      this.setData({ loading: false })
    }
  }
})
