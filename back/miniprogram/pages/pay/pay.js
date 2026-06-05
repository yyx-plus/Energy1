const { get, post } = require('../../utils/request')

Page({
  data: { orderId: null, order: null, loading: false },

  onLoad(options) {
    this.setData({ orderId: options.orderId })
    this.loadOrder()
  },

  async loadOrder() {
    const res = await get(`/wx/order/${this.data.orderId}`)
    this.setData({ order: res.data })
  },

  async onPay() {
    this.setData({ loading: true })
    try {
      const res = await post('/wx/order/pay', { orderId: this.data.orderId })
      wx.showModal({
        title: '支付成功',
        content: `本次充电 ¥${res.data.paidFee}，获得 ${res.data.earnIntegral} 积分`,
        showCancel: false,
        success: () => wx.switchTab({ url: '/pages/profile/profile' })
      })
    } catch (e) {
    } finally {
      this.setData({ loading: false })
    }
  }
})
