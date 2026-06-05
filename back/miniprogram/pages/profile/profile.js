const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: { userInfo: null, avatarLetter: '用' },

  onShow() {
    if (!app.globalData.token) {
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }
    this.loadUserInfo()
  },

  onPullDownRefresh() {
    this.loadUserInfo().finally(() => wx.stopPullDownRefresh())
  },

  async loadUserInfo() {
    try {
      const res = await get('/wx/user/info', {}, false)
      const u = res.data
      const letter = (u.yonghuName || u.username || '用').charAt(0)
      this.setData({ userInfo: u, avatarLetter: letter })
    } catch (e) {}
  },

  goOrders() { wx.navigateTo({ url: '/pages/order-list/order-list' }) },
  goRecharge() { wx.navigateTo({ url: '/pages/recharge/recharge' }) },
  goFeedback() { wx.navigateTo({ url: '/pages/feedback/feedback' }) },
  goIntegral() { wx.navigateTo({ url: '/pages/integral/integral' }) },
  goChat() { wx.navigateTo({ url: '/pages/chat/chat' }) },
  onLogout() {
    wx.showModal({ title: '确认退出？', success: (res) => { if (res.confirm) app.logout() } })
  }
})
