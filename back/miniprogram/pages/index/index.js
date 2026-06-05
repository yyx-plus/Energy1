const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    banners: [],
    notices: [],
    quickStats: { freeGuns: 0, todayService: 0 }
  },

  onShow() {
    this.loadData()
  },

  onPullDownRefresh() {
    this.loadData().finally(() => wx.stopPullDownRefresh())
  },

  async loadData() {
    try {
      // 轮播图：从config表取name包含banner的记录，字段是value存图片url
      const configRes = await get('/config/list', { limit: 20, page: 1 }, false)
      const records = (configRes.data && (configRes.data.records || configRes.data.list)) || []
      const banners = records.filter(item => item.name && item.name.toLowerCase().indexOf('banner') !== -1)
      const rawList = banners.length > 0 ? banners : records.slice(0, 5)
      // value是相对路径时拼上baseUrl
      const baseUrl = app.globalData.baseUrl
      const finalBanners = rawList.map(item => ({
        ...item,
        value: item.value && item.value.startsWith('http') ? item.value : baseUrl + '/' + item.value
      }))
      this.setData({ banners: finalBanners })

      // 公告
      const noticeRes = await get('/gonggao/list', { limit: 5, page: 1 }, false)
      if (noticeRes.data) {
        const noticeList = noticeRes.data.records || noticeRes.data.list || []
        this.setData({ notices: noticeList })
      }
    } catch (e) {}
  },

  goMap() { wx.switchTab({ url: '/pages/map/map' }) },
  goRoute() { wx.switchTab({ url: '/pages/route-plan/route-plan' }) },
  goOrders() {
    if (!app.checkLogin()) return
    wx.navigateTo({ url: '/pages/order-list/order-list' })
  },
  goFeedback() {
    if (!app.checkLogin()) return
    wx.navigateTo({ url: '/pages/feedback/feedback' })
  },
  goNoticeDetail(e) {
    wx.navigateTo({ url: `/pages/notice-detail/notice-detail?id=${e.currentTarget.dataset.id}` })
  }
})
