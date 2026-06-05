const { get } = require('../../utils/request')
const { getOrderStatusInfo } = require('../../utils/util')

Page({
  data: { orders: [], activeTab: 0, tabs: ['全部','已预约','充电中','已完成','已取消'] },

  onShow() { this.loadOrders() },

  onPullDownRefresh() {
    this.loadOrders().finally(() => wx.stopPullDownRefresh())
  },

  async loadOrders() {
    const statusMap = [null, 101, 103, 104, 102]
    const status = statusMap[this.data.activeTab]
    const params = { page: 1, limit: 50 }
    if (status !== null) params.status = status
    try {
      const res = await get('/wx/order/list', params)
      const orders = ((res.data && res.data.list) || []).map(o => ({
        ...o, statusInfo: getOrderStatusInfo(o.status)
      }))
      this.setData({ orders })
    } catch (e) {}
  },

  onTabChange(e) {
    this.setData({ activeTab: e.currentTarget.dataset.idx })
    this.loadOrders()
  },

  goDetail(e) {
    wx.navigateTo({ url: `/pages/order-detail/order-detail?id=${e.currentTarget.dataset.id}` })
  }
})
