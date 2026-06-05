const { get } = require('../../utils/request')
Page({
  data: { list: [], total: 0 },
  onShow() { this.load() },
  onPullDownRefresh() {
    this.load().finally(() => wx.stopPullDownRefresh())
  },
  async load() {
    try {
      const res = await get('/wx/user/integral')
      const data = res.data || {}
      this.setData({ list: data.list || [], total: data.total || 0 })
    } catch (e) {}
  }
})
