const { get } = require('../../utils/request')
const { getGunStatusInfo } = require('../../utils/util')
const app = getApp()

Page({
  data: {
    id: null,
    station: null,
    guns: [],
    priceCurve: [],
    currentPrice: null,
    refreshTimer: null
  },

  onLoad(options) {
    this.setData({ id: options.id })
    this.loadDetail()
    this.loadPriceCurve()
    // 每30秒刷新枪头状态
    const timer = setInterval(() => this.loadDetail(), 30000)
    this.setData({ refreshTimer: timer })
  },

  onUnload() {
    if (this.data.refreshTimer) clearInterval(this.data.refreshTimer)
  },

  async loadDetail() {
    try {
      const res = await get(`/wx/station/${this.data.id}`, {}, false)
      const station = res.data
      const guns = (station.guns || []).map(g => ({
        ...g, statusInfo: getGunStatusInfo(g.status)
      }))
      this.setData({ station, guns, currentPrice: station.currentPrice })
    } catch (e) {}
  },

  async loadPriceCurve() {
    try {
      const res = await get(`/wx/station/${this.data.id}/price`, {}, false)
      this.setData({ priceCurve: res.data || [] })
    } catch (e) {}
  },

  onReserve(e) {
    if (!app.checkLogin()) return
    const gunId = e.currentTarget.dataset.gunid
    wx.navigateTo({ url: `/pages/reserve/reserve?stationId=${this.data.id}&gunId=${gunId}` })
  },

  onNavigate() {
    const { station } = this.data
    wx.openLocation({
      latitude: station.latitude,
      longitude: station.longitude,
      name: station.name,
      address: station.address
    })
  }
})
